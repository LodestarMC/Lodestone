package com.sammy.ortus.helpers.placement;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.sammy.ortus.helpers.AngleHelper;
import com.sammy.ortus.helpers.MathHelpers.VecHelper;
import com.sammy.ortus.helpers.animation.LerpedFloat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber
public class PlacementHelpers {

    private static final List<IPlacementHelper> helpers = new ArrayList<>();
    private static int animationTick = 0;
    private static final LerpedFloat angle = new LerpedFloat(LerpedFloat.Interpolator.LINEAR).chase(0, 0.25f, LerpedFloat.Chaser.LINEAR);
    private static BlockPos target = null;
    private static BlockPos lastTarget = null;

    public static int register(IPlacementHelper helper) {
        helpers.add(helper);
        return helpers.size() - 1;
    }

    public static IPlacementHelper get(int id) {
        if (id < 0 || id >= helpers.size()) {
            throw new ArrayIndexOutOfBoundsException("Invalid placement helper id: " + id);
        }
        return helpers.get(id);
    }

    @OnlyIn(Dist.CLIENT)
    public static void tick() {
        setTarget(null);
        checkHelpers();

        if (target == null) {
            if (animationTick > 0)
                animationTick = Math.max(animationTick - 2, 0);
            return;
        }
        if (animationTick < 10)
            animationTick++;
    }

    @OnlyIn(Dist.CLIENT)
    private static void checkHelpers() {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        if (level == null) return;
        if(!(minecraft.hitResult instanceof BlockHitResult)) return;
        BlockHitResult hit = (BlockHitResult) minecraft.hitResult;
        if(minecraft.player == null) return;
        if(!minecraft.player.isShiftKeyDown()) return;
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack held = minecraft.player.getItemInHand(hand);

            List<IPlacementHelper> filterForHeldItem = helpers.stream().filter(h -> h.matchesItem(held)).collect(Collectors.toList());
            if (filterForHeldItem.isEmpty()) continue;

            BlockPos blockPos = hit.getBlockPos();
            BlockState blockState = level.getBlockState(blockPos);

            List<IPlacementHelper> filteredForState = filterForHeldItem.stream().filter(s -> s.matchesState(blockState)).collect(Collectors.toList());
            if(filteredForState.isEmpty()) continue;

            boolean oneMatch = false;

            for (IPlacementHelper helper : filteredForState) {
                PlacementOffset offset = helper.getOffset(minecraft.player, level, blockState, blockPos, hit, held);
                if (offset.isSuccessful()) {
                    helper.renderAt(blockPos, blockState, hit, offset);
                    setTarget(offset.getBlockPos());
                    oneMatch = true;
                    break;
                }
            }
            if (oneMatch) return;
        }
    }

    static void setTarget(BlockPos pos) {
        PlacementHelpers.target = pos;
        if(pos == null) return;
        if(lastTarget == null) {
            lastTarget = pos;
            return;
        }
        if (!lastTarget.equals(target)) lastTarget = target;
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void afterRenderOverlay(RenderGameOverlayEvent.PostLayer event){
        if (event.getOverlay() != ForgeIngameGui.CROSSHAIR_ELEMENT) return;
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player != null && animationTick > 0) {
            Window window = event.getWindow();
            float xCenter = window.getGuiScaledWidth() / 2f;
            float yCenter = window.getGuiScaledHeight() / 2f;
            float progress = getCurrentAlpha();

            drawDirectionIndicator(event.getMatrixStack(), event.getPartialTicks(), xCenter, yCenter, progress);
        }
    }

    public static float getCurrentAlpha() {
        return Math.min(animationTick / 10F, 1F);
    }

    @OnlyIn(Dist.CLIENT)
    private static void drawDirectionIndicator(PoseStack ps, float partialTicks, float xCenter, float yCenter, float progress) {
        float r = 0.8F;
        float g = 0.8F;
        float b = 0.8F;
        float a = progress * progress;

        Vec3 projectionTarget = VecHelper.projectToPlayerView(VecHelper.getCenterOf(lastTarget), partialTicks);
        Vec3 target = new Vec3(projectionTarget.x, projectionTarget.y, 0);
        if (projectionTarget.z > 0) {
            target = target.reverse();
        }

        Vec3 norm = target.normalize();
        Vec3 ref = new Vec3(0,1,0);
        float targetAngle = AngleHelper.deg(Math.acos(norm.dot(ref)));

        angle.updateSpeed(0.25f);

        if (norm.x < 0) {
            targetAngle = 360 - targetAngle;
        }
        if (animationTick < 10) angle.setValue(targetAngle);

        angle.updateChaseTarget(targetAngle);
        angle.tickChaser();

        float snapSize = 22.5F;
        float snapAngle = (snapSize * Math.round(angle.getCurrentValue(0f) / snapSize)) % 360F;

        float length = 10;

        fadedArrow(ps, xCenter, yCenter, length, r, g, b, a, length, snapAngle, angle);
    }

    public static void fadedArrow(PoseStack ps, float xCenter, float yCenter, float length, float r, float g, float b, float a, float arrowLength, float arrowAngle, LerpedFloat angle) {
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        ps.pushPose();
        ps.translate(xCenter, yCenter, 5);
        ps.mulPose(Vector3f.ZP.rotationDegrees(angle.getCurrentValue(0f)));
        double scale = 1;
        ps.scale((float) scale, (float) scale, 1);

        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);

        Matrix4f mat = ps.last().pose();

        bufferbuilder.vertex(mat, 0, -(10 + length), 0).color(r, g, b, a).endVertex();

        bufferbuilder.vertex(mat, -9, -3, 0).color(r, g, b, 0f).endVertex();
        bufferbuilder.vertex(mat, -6, -6, 0).color(r, g, b, 0f).endVertex();
        bufferbuilder.vertex(mat, -3, -8, 0).color(r, g, b, 0f).endVertex();
        bufferbuilder.vertex(mat, 0, -8.5f, 0).color(r, g, b, 0f).endVertex();
        bufferbuilder.vertex(mat, 3, -8, 0).color(r, g, b, 0f).endVertex();
        bufferbuilder.vertex(mat, 6, -6, 0).color(r, g, b, 0f).endVertex();
        bufferbuilder.vertex(mat, 9, -3, 0).color(r, g, b, 0f).endVertex();

        tessellator.end();
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
        ps.popPose();
    }



}
