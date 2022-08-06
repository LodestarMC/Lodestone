package team.lodestar.lodestone.systems.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;

import java.util.ArrayList;
import java.util.List;

public class MultiBlockStructure {
    public final ArrayList<StructurePiece> structurePieces;

    public MultiBlockStructure(ArrayList<StructurePiece> structurePieces) {
        this.structurePieces = structurePieces;
    }

    public boolean canPlace(BlockPlaceContext context) {
        return structurePieces.stream().allMatch(p -> p.canPlace(context));
    }

    public void place(BlockPlaceContext context) {
        structurePieces.forEach(s -> s.place(context.getClickedPos(), context.getLevel()));
    }

    public static MultiBlockStructure of(StructurePiece... pieces) {
        return new MultiBlockStructure(new ArrayList<>(List.of(pieces)));
    }

    public static class StructurePiece {
        public final Vec3i offset;
        public final BlockState state;

        public StructurePiece(int xOffset, int yOffset, int zOffset, BlockState state) {
            this.offset = new Vec3i(xOffset, yOffset, zOffset);
            this.state = state;
        }

        public boolean canPlace(BlockPlaceContext context) {
            Level level = context.getLevel();
            Player player = context.getPlayer();
            BlockPos pos = context.getClickedPos().offset(offset);
            BlockState existingState = context.getLevel().getBlockState(pos);
            CollisionContext collisioncontext = player == null ? CollisionContext.empty() : CollisionContext.of(player);
            return existingState.getMaterial().isReplaceable() && level.isUnobstructed(state, pos, collisioncontext);
        }

        public void place(BlockPos core, Level level) {
            place(core, level, state);
        }

        public void place(BlockPos core, Level level, BlockState state) {
            BlockPos pos = core.offset(offset);
            level.setBlock(pos, state, 3);
            if (level.getBlockEntity(pos) instanceof MultiBlockComponentEntity component) {
                component.corePos = core;
            }
        }
    }
}