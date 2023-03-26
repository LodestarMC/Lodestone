package team.lodestar.lodestone.systems.rendering.ghost;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.RandomSource;
import net.minecraftforge.client.model.data.ModelData;
import team.lodestar.lodestone.handlers.PlacementAssistantHandler;
import team.lodestar.lodestone.handlers.RenderHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public abstract class GhostBlockRenderer {

    public static final GhostBlockRenderer STANDARD = new DefaultGhostBlockRenderer();

    public abstract void render(PoseStack ps, GhostBlockOptions params);

    private static class DefaultGhostBlockRenderer extends GhostBlockRenderer {
        @Override
        public void render(PoseStack ps, GhostBlockOptions options) {
            ps.pushPose();
            RenderSystem.enableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            Minecraft minecraft = Minecraft.getInstance();
            BlockRenderDispatcher dispatch = minecraft.getBlockRenderer();
            BakedModel bakedModel = dispatch.getBlockModel(options.blockState);
            RenderType renderType = options.renderType;
            VertexConsumer consumer = RenderHandler.DELAYED_RENDER.getBuffer(renderType);
            BlockPos pos = options.blockPos;
            float scale = options.scaleSupplier.get();

            ps.translate(pos.getX(), pos.getY(), pos.getZ());
            ps.translate(0.5D, 0.5D, 0.5D);
            ps.scale(scale, scale, scale);
            ps.translate(-0.5D, -0.5D, -0.5D);

            float alpha = options.alphaSupplier.get();
            renderModel(ps.last(), consumer, options.blockState, bakedModel, options.red, options.green, options.blue, alpha, LevelRenderer.getLightColor(minecraft.level, pos), OverlayTexture.NO_OVERLAY, ModelData.EMPTY, minecraft.level.getRandom(), renderType);

            RenderSystem.disableBlend();
            RenderSystem.disableDepthTest();
            ps.popPose();
        }

        //TODO: maybe move these two into some render helper
        public static void renderModel(PoseStack.Pose pose, VertexConsumer consumer, BlockState state, BakedModel model, float red, float green, float blue, float alpha, int packedLight, int packedOverlay, ModelData extraData, RandomSource random, RenderType renderType) {
            for (Direction direction : Direction.values()) {
                random.setSeed(42L);
                renderQuadList(pose, consumer, red, green, blue, alpha, model.getQuads(state, direction, random, extraData, renderType), packedLight, packedOverlay);
            }
            random.setSeed(42L);
            renderQuadList(pose, consumer, red, green, blue, alpha, model.getQuads(state, null, random, extraData, renderType), packedLight, packedOverlay);
        }

        public static void renderQuadList(PoseStack.Pose pose, VertexConsumer consumer, float red, float green, float blue, float alpha, List<BakedQuad> quads, int packedLight, int packedOverlay) {
            for (BakedQuad quad : quads) {
                float r = Mth.clamp(red, 0.0F, 1.0F);
                float g = Mth.clamp(green, 0.0F, 1.0F);
                float b = Mth.clamp(blue, 0.0F, 1.0F);
                consumer.putBulkData(pose, quad, r, g, b, alpha, packedLight, packedOverlay, false);
            }
        }
    }
}