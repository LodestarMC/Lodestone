package team.lodestar.lodestone.systems.datagen.statesmith;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.systems.datagen.providers.LodestoneBlockStateProvider;

import java.util.Collection;

public class ModularBlockStateSmith<T extends Block> extends AbstractBlockStateSmith<T> {

    public final ModularSmithStateSupplier<T> stateSupplier;

    public ModularBlockStateSmith(Class<T> blockClass, ModularSmithStateSupplier<T> stateSupplier) {
        super(blockClass);
        this.stateSupplier = stateSupplier;
    }

    @SafeVarargs
    public final void act(StateSmithData data, StateFunction<T> actor, ModelFileSupplier modelFileSupplier, RegistryObject<Block>... blocks) {
        for (RegistryObject<Block> block : blocks) {
            act(data, actor, modelFileSupplier, block);
        }
    }

    public void act(StateSmithData data, StateFunction<T> actor, ModelFileSupplier modelFileSupplier, Collection<RegistryObject<Block>> blocks) {
        blocks.forEach(r -> act(data, actor, modelFileSupplier, r));
    }

    private void act(StateSmithData data, StateFunction<T> actor, ModelFileSupplier modelFileSupplier, RegistryObject<Block> registryObject) {
        Block block = registryObject.get();
        if (blockClass.isInstance(block)) {
            stateSupplier.act(blockClass.cast(block), data.provider, actor, modelFileSupplier);
            data.consumer.accept(registryObject);
        } else {
            LodestoneLib.LOGGER.warn("Block does not match the state smith it was assigned: " + ForgeRegistries.BLOCKS.getKey(block));
        }
    }

    public interface ModularSmithStateSupplier<T extends Block> {
        void act(T block, LodestoneBlockStateProvider provider, StateFunction<T> actor, ModelFileSupplier modelFileSupplier);
    }

    public interface ModelFileSupplier {
        ModelFile generateModel(Block block);
    }
}