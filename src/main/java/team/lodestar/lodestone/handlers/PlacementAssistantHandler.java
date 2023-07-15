package team.lodestar.lodestone.handlers;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import team.lodestar.lodestone.helpers.DataHelper;
import team.lodestar.lodestone.systems.placementassistance.IPlacementAssistant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PlacementAssistantHandler {

    public static final ArrayList<IPlacementAssistant> ASSISTANTS = new ArrayList<>();
    public static int animationTick = 0;
    public static BlockHitResult target;

    public static void registerPlacementAssistants(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> DataHelper.getAll(new ArrayList<>(ForgeRegistries.BLOCKS.getValues()), b -> b instanceof IPlacementAssistant).forEach(i -> {
                            IPlacementAssistant assistant = (IPlacementAssistant) i;
                            ASSISTANTS.add(assistant);
                        }
                )
        );
    }

    public static void placeBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        Level level = player.level();
        if (level.isClientSide) {
            List<Pair<IPlacementAssistant, ItemStack>> assistants = findAssistants(level, player, event.getHitVec());
            for (Pair<IPlacementAssistant, ItemStack> pair : assistants) {
                IPlacementAssistant assistant = pair.getFirst();
                BlockState state = level.getBlockState(event.getPos());
                assistant.onPlaceBlock(player, level, event.getHitVec(), state, pair.getSecond());
            }
            animationTick = Math.max(0, animationTick - 5);
        }
    }

    public static void tick(Player player, HitResult hitResult) {
        Level level = player.level();
        List<Pair<IPlacementAssistant, ItemStack>> placementAssistants = findAssistants(level, player, hitResult);
        for (Pair<IPlacementAssistant, ItemStack> pair : placementAssistants) {
            IPlacementAssistant assistant = pair.getFirst();
            assistant.onHoldValidItem(player, level, pair.getSecond());
        }
        if (hitResult instanceof BlockHitResult blockHitResult && !blockHitResult.getType().equals(HitResult.Type.MISS)) {
            target = blockHitResult;
            for (Pair<IPlacementAssistant, ItemStack> pair : placementAssistants) {
                IPlacementAssistant assistant = pair.getFirst();
                BlockState state = level.getBlockState(blockHitResult.getBlockPos());
                assistant.onObserveBlock(player, level, blockHitResult, state, pair.getSecond());
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

    private static List<Pair<IPlacementAssistant, ItemStack>> findAssistants(Level level, Player player, HitResult hitResult) {
        if (!(hitResult instanceof BlockHitResult)) {
            return Collections.emptyList();
        }
        return findAssistants(level, player);
    }

    private static List<Pair<IPlacementAssistant, ItemStack>> findAssistants(Level level, Player player) {
        if (level == null || player == null || player.isShiftKeyDown()) {
            return Collections.emptyList();
        }
        List<Pair<IPlacementAssistant, ItemStack>> matchingAssistants = new ArrayList<>();
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack held = player.getItemInHand(hand);
            matchingAssistants.addAll(ASSISTANTS.stream().filter(s -> s.canAssist().test(held)).map(a -> Pair.of(a, held)).collect(Collectors.toCollection(ArrayList::new)));
        }
        return matchingAssistants;
    }

    public static float getCurrentAlpha() {
        return Math.min(animationTick / 10F, 1F);
    }
}