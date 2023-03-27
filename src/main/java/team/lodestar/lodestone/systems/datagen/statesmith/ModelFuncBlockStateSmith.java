package team.lodestar.lodestone.systems.datagen.statesmith;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import team.lodestar.lodestone.LodestoneLib;

import java.util.Collection;

public class ModelFuncBlockStateSmith<T extends Block> extends AbstractBlockStateSmith<T> {

    public final ModelFuncSmithStateSupplier<T> stateSupplier;

    protected ModelFuncBlockStateSmith(Class<T> blockClass, ModelFuncSmithStateSupplier<T> stateSupplier) {
        super(blockClass);
        this.stateSupplier = stateSupplier;
    }

    @SafeVarargs
    public final void act(StateSmithData data, StateFunction<T> actor, RegistryObject<Block>... blocks) {
        for (RegistryObject<Block> block : blocks) {
            act(data, actor, block);
        }
    }

    public void act(StateSmithData data, StateFunction<T> actor, Collection<RegistryObject<Block>> blocks) {
        blocks.forEach(r -> act(data, actor, r));
    }

    private void act(StateSmithData data, StateFunction<T> actor, RegistryObject<Block> block) {
        if (blockClass.isInstance(block)) {
            stateSupplier.act(blockClass.cast(block.get()), data.provider, data.texturePath, actor);
            data.consumer.accept(block);
        } else {
            LodestoneLib.LOGGER.warn("Block does not match the state smith it was assigned: " + ForgeRegistries.BLOCKS.getKey(block.get()));
        }
    }

    interface ModelFuncSmithStateSupplier<T extends Block> {
        void act(T block, BlockStateProvider provider, String texturePath, StateFunction<T> actor);
    }

    public interface StateFunction<T extends Block> {
        void act(T block, ModelFile modelFile);
    }
}