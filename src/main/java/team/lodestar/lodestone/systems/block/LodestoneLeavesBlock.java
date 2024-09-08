package team.lodestar.lodestone.systems.block;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.util.Mth;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import team.lodestar.lodestone.systems.easing.Easing;

import java.awt.*;

public abstract class LodestoneLeavesBlock extends LeavesBlock {

    public final Color minColor;
    public final Color maxColor;

    public LodestoneLeavesBlock(Properties properties, Color minColor, Color maxColor) {
        super(properties);
        this.minColor = minColor;
        this.maxColor = maxColor;
        registerDefaultState(defaultBlockState().setValue(getColorProperty(), 0));
    }

    public abstract IntegerProperty getColorProperty();

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DISTANCE, PERSISTENT, WATERLOGGED, getColorProperty());
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(getColorProperty(), 0);
    }

    public static void registerSimpleGradientColors(BlockColors blockColors, LodestoneLeavesBlock leavesBlock) {
        blockColors.register((s, l, p, c) -> {
            final IntegerProperty colorProperty = leavesBlock.getColorProperty();
            float colorMax = colorProperty.getPossibleValues().size();
            float color = s.getValue(colorProperty);
            float pct = (colorMax - (color / colorMax));
            float value = Easing.SINE_IN_OUT.ease(pct, 0, 1, 1);
            int red = (int) Mth.lerp(value, leavesBlock.minColor.getRed(), leavesBlock.maxColor.getRed());
            int green = (int) Mth.lerp(value, leavesBlock.minColor.getGreen(), leavesBlock.maxColor.getGreen());
            int blue = (int) Mth.lerp(value, leavesBlock.minColor.getBlue(), leavesBlock.maxColor.getBlue());
            return red << 16 | green << 8 | blue;
        }, leavesBlock);
    }
}