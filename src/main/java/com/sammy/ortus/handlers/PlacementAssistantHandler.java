package com.sammy.ortus.handlers;

import com.sammy.ortus.helpers.DataHelper;
import com.sammy.ortus.systems.placementassistance.IPlacementAssistant;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber
public class PlacementAssistantHandler {

    public static final ArrayList<IPlacementAssistant> ASSISTANTS = new ArrayList<>();
    public static int animationTick = 0;
    public static BlockPos target;

    public static void registerPlacementAssistants(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> DataHelper.getAll(new ArrayList<>(ForgeRegistries.BLOCKS.getValues()), b -> b instanceof IPlacementAssistant).forEach(i -> {
                            IPlacementAssistant assistant = (IPlacementAssistant) i;
                            ASSISTANTS.add(assistant);
                        }
                )
        );
    }

    public static void placeBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getPlayer();
        Level level = player.level;
        List<IPlacementAssistant> assistants = findAssistants(level, player, event.getHitVec());
        for (IPlacementAssistant assistant : assistants) {
            BlockState state = level.getBlockState(event.getPos());
            assistant.onPlace(level, event.getHitVec(), state);
        }
    }
    @OnlyIn(Dist.CLIENT)
    public static void clientTick() {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        List<IPlacementAssistant> assistants = findAssistants(level, minecraft.player, minecraft.hitResult);
        if (minecraft.hitResult instanceof BlockHitResult blockHitResult && !blockHitResult.getType().equals(HitResult.Type.MISS)) {
            target = blockHitResult.getBlockPos();
            for (IPlacementAssistant assistant : assistants) {
                BlockState state = minecraft.level.getBlockState(blockHitResult.getBlockPos());
                assistant.assist(level, blockHitResult, state);
                assistant.showAssistance(level, blockHitResult, state);
            }
        } else {
            target = null;
        }
        if (target == null) {
            if (animationTick > 0) {
                animationTick = Math.max(animationTick - 2, 0);
            }
            return;
        }
        if (animationTick < 10) {
            animationTick++;
        }
    }

    private static List<IPlacementAssistant> findAssistants(Level level, Player player, HitResult hitResult) {
        if (level == null || !(hitResult instanceof BlockHitResult) || player == null || player.isShiftKeyDown()) {
            return Collections.emptyList();
        }
        ArrayList<IPlacementAssistant> matchingAssistants = new ArrayList<>();
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack held = player.getItemInHand(hand);
            matchingAssistants.addAll(ASSISTANTS.stream().filter(s -> s.canAssist().test(held)).collect(Collectors.toCollection(ArrayList::new)));
        }
        return matchingAssistants;
    }

    public static float getCurrentAlpha() {
        return Math.min(animationTick / 10F, 1F);
    }
}