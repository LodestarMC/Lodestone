package team.lodestar.lodestone.toolkit.block;

import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class LodestoneLogBlock extends RotatedPillarBlock {
    @Nullable
    public final Supplier<Block> stripped;

    public LodestoneLogBlock(Properties properties, @Nullable Supplier<Block> stripped) {
        super(properties);
        this.stripped = stripped;
    }

    public LodestoneLogBlock(Properties properties) {
        this(properties, null);
    }

    @Override
    public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility itemAbility, boolean simulate) {
        if (stripped != null) {
            if (itemAbility.equals(ItemAbilities.AXE_STRIP)) {
                return stripped.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
            }
        }
        return super.getToolModifiedState(state, context, itemAbility, simulate);
    }
}