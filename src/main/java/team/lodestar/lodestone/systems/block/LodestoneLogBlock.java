package team.lodestar.lodestone.systems.block;

import io.github.fabricators_of_create.porting_lib.tool.ToolAction;
import io.github.fabricators_of_create.porting_lib.tool.ToolActions;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class LodestoneLogBlock extends RotatedPillarBlock {
    public final Supplier<Block> stripped;

    public LodestoneLogBlock(Properties properties, Supplier<Block> stripped) {
        super(properties);
        this.stripped = stripped;
        StrippableBlockRegistry.register(this, stripped.get());
    }

    @Override
    public @org.jetbrains.annotations.Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
        if (toolAction.equals(ToolActions.AXE_STRIP)) {
            return stripped.get().defaultBlockState().setValue(AXIS, state.getValue(AXIS));
        }
        return super.getToolModifiedState(state, context, toolAction, simulate);
    }
}
