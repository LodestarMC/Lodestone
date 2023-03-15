package team.lodestar.lodestone.systems.datagen.statesmith;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.registries.RegistryObject;
import team.lodestar.lodestone.LodestoneLib;

import java.util.Collection;

public class ModelFuncBlockStateSmith<T extends Block> extends AbstractBlockStateSmith<T> {

    public final ModelFuncSmithStateSupplier<T> stateSupplier;

    protected ModelFuncBlockStateSmith(Class<T> blockClass, ModelFuncSmithStateSupplier<T> stateSupplier) {
        super(blockClass);
        this.stateSupplier = stateSupplier;
    }

    public void act(BlockStateProvider provider, Collection<RegistryObject<Block>> blocks, StateFunction<T> actor) {
        blocks.forEach(r -> act(provider, r.get(), actor));
    }

    public void act(BlockStateProvider provider, Block block, StateFunction<T> actor) {
        if (blockClass.isInstance(block)) {
            stateSupplier.act(blockClass.cast(block), provider, actor);
        } else {
            LodestoneLib.LOGGER.warn("Block does not match the state smith it was assigned: " + block.getRegistryName());
        }
    }

    interface ModelFuncSmithStateSupplier<T extends Block> {
        void act(T block, BlockStateProvider provider, StateFunction<T> actor);
    }

    public interface StateFunction<T extends Block> {
        void act(T block, BlockStateProvider provider, ModelFile modelFile);
    }
}