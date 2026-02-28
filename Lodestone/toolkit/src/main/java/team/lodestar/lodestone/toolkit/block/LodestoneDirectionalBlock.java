package team.lodestar.lodestone.toolkit.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

@SuppressWarnings("NullableProblems")
public class LodestoneDirectionalBlock extends DirectionalBlock {

    public static final MapCodec<LodestoneDirectionalBlock> CODEC = simpleCodec(LodestoneDirectionalBlock::new);

    public LodestoneDirectionalBlock(Properties builder) {
        super(builder);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        var direction = context.getClickedFace();
        if (context.getPlayer() != null && context.getPlayer().isShiftKeyDown()) {
            direction = direction.getOpposite();
        }
        return this.defaultBlockState().setValue(FACING, direction);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.setValue(FACING, mirrorIn.mirror(state.getValue(FACING)));
    }
}