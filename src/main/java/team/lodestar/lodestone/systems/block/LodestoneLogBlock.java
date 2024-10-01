package team.lodestar.lodestone.systems.block;

import io.github.fabricators_of_create.porting_lib.tool.ItemAbilities;
import io.github.fabricators_of_create.porting_lib.tool.ItemAbility;
import io.github.fabricators_of_create.porting_lib.tool.extensions.BlockStateExtensions;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class LodestoneLogBlock extends RotatedPillarBlock implements BlockStateExtensions {
    public final Supplier<Block> stripped;

    public LodestoneLogBlock(Properties properties, Supplier<Block> stripped) {
        super(properties);
        this.stripped = stripped;
    }

    @Override
    public @Nullable BlockState getToolModifiedState(UseOnContext context, ItemAbility itemAbility, boolean simulate) {
        if (itemAbility.equals(ItemAbilities.AXE_STRIP)) {
            return stripped.get().defaultBlockState().setValue(AXIS, context.getPlayer().level().getBlockState(context.getClickedPos()).getValue(AXIS));
        }
        return BlockStateExtensions.super.getToolModifiedState(context, itemAbility, simulate);
    }
}
