package team.lodestar.lodestone.systems.placementassistance;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.Predicate;

/**
 * A placement assistant is a helpful client-sided system which allows you to create systems that preview unique block placement mechanics.
 */
public interface IPlacementAssistant {

    void onPlaceBlock(Player player, Level level, BlockHitResult hit, BlockState blockState, ItemStack stack);

    void onObserveBlock(Player player, Level level, BlockHitResult hit, BlockState blockState, ItemStack stack);

    void onHoldValidItem(Player player, Level level, ItemStack stack);

    public Predicate<ItemStack> canAssist();
}
