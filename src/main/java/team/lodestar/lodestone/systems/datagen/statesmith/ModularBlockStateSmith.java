package team.lodestar.lodestone.systems.datagen.statesmith;

import io.github.fabricators_of_create.porting_lib.models.generators.ModelFile;
import net.minecraft.world.level.block.Block;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.systems.datagen.ItemModelSmithTypes;
import team.lodestar.lodestone.systems.datagen.itemsmith.ItemModelSmith;
import team.lodestar.lodestone.systems.datagen.providers.LodestoneBlockStateProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class ModularBlockStateSmith<T extends Block> extends AbstractBlockStateSmith<T> {

    public final ModularSmithStateSupplier<T> stateSupplier;

    public ModularBlockStateSmith(Class<T> blockClass, ModularSmithStateSupplier<T> stateSupplier) {
        super(blockClass);
        this.stateSupplier = stateSupplier;
    }

    @SafeVarargs
    public final void act(StateSmithData data, StateFunction<T> actor, ModelFileSupplier modelFileSupplier, Supplier<? extends Block>... blocks) {
        act(data, ItemModelSmithTypes.BLOCK_MODEL_ITEM, actor, modelFileSupplier, blocks);
    }

    @SafeVarargs
    public final void act(StateSmithData data, ItemModelSmith itemModelSmith, StateFunction<T> actor, ModelFileSupplier modelFileSupplier, Supplier<? extends Block>... blocks) {
        for (Supplier<? extends Block> block : blocks) {
            act(data, itemModelSmith, actor, modelFileSupplier, block);
        }
        List.of(blocks).forEach(data.consumer);
    }

    public void act(StateSmithData data, StateFunction<T> actor, ModelFileSupplier modelFileSupplier, Collection<Supplier<? extends Block>> blocks) {
        act(data, ItemModelSmithTypes.BLOCK_MODEL_ITEM, actor, modelFileSupplier, blocks);
    }

    public void act(StateSmithData data, ItemModelSmith itemModelSmith, StateFunction<T> actor, ModelFileSupplier modelFileSupplier, Collection<Supplier<? extends Block>> blocks) {
        blocks.forEach(r -> act(data, itemModelSmith, actor, modelFileSupplier, r));
        new ArrayList<>(blocks).forEach(data.consumer);
    }

    private void act(StateSmithData data, ItemModelSmith itemModelSmith, StateFunction<T> actor, ModelFileSupplier modelFileSupplier, Supplier<? extends Block> registryObject) {
        Block block = registryObject.get();
        if (blockClass.isInstance(block)) {
            stateSupplier.act(blockClass.cast(block), data.provider, actor, modelFileSupplier);
            itemModelSmith.act(block::asItem, data.provider.itemModelProvider);
        } else {
            LodestoneLib.LOGGER.warn("Block does not match the state smith it was assigned: " + registryObject.get().toString());
        }
    }

    public interface ModularSmithStateSupplier<T extends Block> {
        void act(T block, LodestoneBlockStateProvider provider, StateFunction<T> actor, ModelFileSupplier modelFileSupplier);
    }

    public interface ModelFileSupplier {
        ModelFile generateModel(Block block);
    }
}