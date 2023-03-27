package team.lodestar.lodestone.systems.datagen.statesmith;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import team.lodestar.lodestone.LodestoneLib;

import java.util.Collection;
import java.util.function.Function;

public class ModularBlockStateSmith<T extends Block> extends AbstractBlockStateSmith<T> {

    public final ModularSmithStateSupplier<T> stateSupplier;

    protected ModularBlockStateSmith(Class<T> blockClass, ModularSmithStateSupplier<T> stateSupplier) {
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
            stateSupplier.act(blockClass.cast(block), data.provider, data.getTexturePath(), actor, modelFileSupplier);
            data.consumer.accept(registryObject);
        } else {
            LodestoneLib.LOGGER.warn("Block does not match the state smith it was assigned: " + ForgeRegistries.BLOCKS.getKey(block));
        }
    }

    interface ModularSmithStateSupplier<T extends Block> {
        void act(T block, BlockStateProvider provider, String texturePath, StateFunction<T> actor, ModelFileSupplier modelFileSupplier);
    }

    public interface ModelFileSupplier {
        ModelFile generateModel(Block block, BlockStateProvider provider, Function<String, ResourceLocation> textureGetter);
    }
}