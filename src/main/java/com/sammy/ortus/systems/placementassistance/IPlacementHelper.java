package com.sammy.ortus.systems.placementassistance;

import com.sammy.ortus.handlers.GhostBlockHandler;
import com.sammy.ortus.helpers.VecHelper;
import com.sammy.ortus.helpers.util.Iterate;
import com.sammy.ortus.helpers.util.Pair;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@MethodsReturnNonnullByDefault
public interface IPlacementHelper {
    /**
     * used as an identifier in SuperGlueHandler to skip blocks placed by helpers
     */
    BlockState ID = new BlockState(Blocks.AIR, null, null);

    /**
     * @return a predicate that gets tested with the items held in the players hands<br>
     * should return true if this placement helper is active with the given item
     */
    Predicate<ItemStack> getItemPredicate();

    /**
     * @return a predicate that gets tested with the blockstate the player is looking at<br>
     * should return true if this placement helper is active with the given blockstate
     */
    Predicate<BlockState> getStatePredicate();

    /**
     *
     * @param player the player that activated the placement helper
     * @param world the world that the placement helper got activated in
     * @param state the Blockstate of the Block that the player is looking at or clicked on
     * @param pos the position of the Block the player is looking at or clicked on
     * @param ray the exact raytrace result
     *
     * @return the PlacementOffset object describing where to place the new block.<br>
     *     Use {@link PlacementOffset#fail} when no new position could be found.<br>
     */
    PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray);

    //sets the offset's ghost state with the default state of the held block item, this is used in PlacementHelpers and can be ignored in most cases
    default PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray, ItemStack heldItem) {
        PlacementOffset offset = getOffset(player, world, state, pos, ray);
        if (heldItem.getItem() instanceof BlockItem blockItem) {
            offset = offset.withGhostState(blockItem.getBlock().defaultBlockState());
        }
        return offset;
    }

    /**
     * overwrite this method if your placement helper needs a different rendering than the default ghost state
     *
     * @param pos the position of the Block the player is looking at or clicked on
     * @param state the Blockstate of the Block that the player is looking at or clicked on
     * @param ray the exact raytrace result
     *               the offset will always be successful if this method is called
     */
    default void renderAt(BlockPos pos, BlockState state, BlockHitResult ray, PlacementOffset offset) {
        displayGhost(offset);
    }

    default void displayGhost(PlacementOffset offset) {
        if (!offset.hasGhostState())
            return;

        GhostBlockHandler.addGhost(this, offset.getTransform().apply(offset.getGhostState()))
                .at(offset.getBlockPos());
    }
    static List<Direction> orderedByDistance(BlockPos pos, Vec3 hit, Predicate<Direction> includeDirection) {
        Vec3 centerToHit = hit.subtract(VecHelper.getCenterOf(pos));
        return Arrays.stream(Iterate.directions)
                .filter(includeDirection)
                .map(dir -> Pair.of(dir, Vec3.atLowerCornerOf(dir.getNormal()).distanceTo(centerToHit)))
                .sorted(Comparator.comparingDouble(Pair::getSecond))
                .map(Pair::getFirst)
                .collect(Collectors.toList());
    }

    default boolean matchesItem(ItemStack item) {
        return getItemPredicate().test(item);
    }

    default boolean matchesState(BlockState state) {
        return getStatePredicate().test(state);
    }
}
