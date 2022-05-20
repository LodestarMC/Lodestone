package com.sammy.ortus.handlers;

import com.sammy.ortus.helpers.DataHelper;
import com.sammy.ortus.systems.placementassistance.IPlacementAssistant;
import com.sammy.ortus.systems.rendering.particle.screen.emitter.ItemParticleEmitter;
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
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber
public class PlacementAssistantHandler {

    public static final ArrayList<IPlacementAssistant> ASSISTANTS = new ArrayList<>();
    private static int animationTick = 0;
    private static BlockPos target;

    public static void registerPlacementAssistants(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> DataHelper.getAll(new ArrayList<>(ForgeRegistries.BLOCKS.getValues()), b -> b instanceof IPlacementAssistant).forEach(i -> {
                            IPlacementAssistant assistant = (IPlacementAssistant) i;
                            ASSISTANTS.add(assistant);
                        }
                )
        );
    }

    @OnlyIn(Dist.CLIENT)
    public static void clientTick() {
        findTarget();

        if (target == null) {
            if (animationTick > 0)
                animationTick = Math.max(animationTick - 2, 0);
            return;
        }
        if (animationTick < 10)
            animationTick++;
    }

    @OnlyIn(Dist.CLIENT)
    private static void findTarget() {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        if (level == null || !(minecraft.hitResult instanceof BlockHitResult hit) || minecraft.player == null || minecraft.player.isShiftKeyDown()) {
            return;
        }
        BlockPos blockPos = target = hit.getBlockPos();
        BlockState blockState = level.getBlockState(blockPos);
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack held = minecraft.player.getItemInHand(hand);

            ArrayList<IPlacementAssistant> matchingAssistants = ASSISTANTS.stream().filter(s -> s.shouldRenderSimple().test(held)).collect(Collectors.toCollection(ArrayList::new));

            for (IPlacementAssistant matchingAssistant : matchingAssistants) {
                matchingAssistant.displayGhost(level, hit, blockState);
            }
//            List<IPlacementHelper> filteredForState = filterForHeldItem.stream().filter(s -> s.matchesState(blockState)).collect(Collectors.toList());
//            if (filteredForState.isEmpty()) continue;
//
//            boolean oneMatch = false;
//
//            for (IPlacementHelper helper : filteredForState) {
//                PlacementOffset offset = helper.getOffset(minecraft.player, level, blockState, blockPos, hit, held);
//                if (offset.isSuccessful()) {
//                    helper.renderAt(blockPos, blockState, hit, offset);
//                    setTarget(offset.getBlockPos());
//                    oneMatch = true;
//                    break;
//                }
//            }
//            if (oneMatch) return;
        }
    }

    public static float getCurrentAlpha() {
        return Math.min(animationTick / 10F, 1F);
    }
}