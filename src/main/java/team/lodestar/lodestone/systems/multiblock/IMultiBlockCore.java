package team.lodestar.lodestone.systems.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * The core of a multiblock structure, tracks the positions of all the components in the structure.
 */
public interface IMultiBlockCore {

    ArrayList<BlockPos> getComponentPositions();

    /**
     * @return The multiblock structure created upon placement. Return null to disable.
     */
    @Nullable
    MultiBlockStructure getStructure();

    default void setupMultiblock(BlockPos pos) {
        if (getStructure() == null) {
            return;
        }
        getStructure().structurePieces.forEach(p -> {
            Vec3i offset = p.offset;
            getComponentPositions().add(pos.offset(offset));
        });
    }

    default boolean isModular() {
        return false;
    }

    default void destroyMultiblock(@Nullable Player player, Level level, BlockPos pos) {
        if (isModular()) {
            return;
        }
        getComponentPositions().forEach(p -> {
            if (level.getBlockEntity(p) instanceof MultiBlockComponentEntity) {
                level.destroyBlock(p, false);
            }
        });
        boolean dropBlock = player == null || !player.isCreative();
        if (level.getBlockEntity(pos) instanceof MultiBlockCoreEntity) {
            level.destroyBlock(pos, dropBlock);
        }
    }
}