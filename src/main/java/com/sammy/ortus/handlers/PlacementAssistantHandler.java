package com.sammy.ortus.handlers;

import com.sammy.ortus.systems.placementassistance.IPlacementHelper;
import com.sammy.ortus.systems.placementassistance.PlacementOffset;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber
public class PlacementAssistantHandler {

    private static final List<IPlacementHelper> helpers = new ArrayList<>();
    private static int animationTick = 0;
    private static BlockPos target;
    private static BlockPos lastTarget;

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
        if (!(minecraft.hitResult instanceof BlockHitResult hit)) return;
        if (minecraft.player == null) return;
        if (!minecraft.player.isShiftKeyDown()) return;
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack held = minecraft.player.getItemInHand(hand);

            List<IPlacementHelper> filterForHeldItem = helpers.stream().filter(h -> h.matchesItem(held)).collect(Collectors.toList());
            if (filterForHeldItem.isEmpty()) continue;

            BlockPos blockPos = hit.getBlockPos();
            BlockState blockState = level.getBlockState(blockPos);

            List<IPlacementHelper> filteredForState = filterForHeldItem.stream().filter(s -> s.matchesState(blockState)).collect(Collectors.toList());
            if (filteredForState.isEmpty()) continue;

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
        PlacementAssistantHandler.target = pos;
        if (pos == null) return;
        if (lastTarget == null) {
            lastTarget = pos;
            return;
        }
        if (!lastTarget.equals(target)) lastTarget = target;
    }

    public static float getCurrentAlpha() {
        return Math.min(animationTick / 10F, 1F);
    }
}