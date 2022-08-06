package team.lodestar.lodestone.systems.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.ArrayList;

public interface IMultiBlockCore {

    ArrayList<BlockPos> getComponentPositions();

    MultiBlockStructure getStructure();

    default void setupMultiblock(BlockPos pos) {
        getStructure().structurePieces.forEach(p -> {
            Vec3i offset = p.offset;
            getComponentPositions().add(pos.offset(offset));
        });
    }

    default void destroyMultiblock(@Nullable Player player, Level level, BlockPos pos) {
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