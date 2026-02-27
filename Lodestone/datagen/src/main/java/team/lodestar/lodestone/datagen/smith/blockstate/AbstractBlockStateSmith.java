package team.lodestar.lodestone.datagen.smith.blockstate;

import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import team.lodestar.lodestone.datagen.ItemModelSmithTypes;
import team.lodestar.lodestone.datagen.smith.itemmodel.ItemModelSmith;
import team.lodestar.lodestone.datagen.providers.block.LodestoneBlockStateSystem;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class AbstractBlockStateSmith<T extends Block> {

    public final Class<T> blockClass;

    public AbstractBlockStateSmith(Class<T> blockClass) {
        this.blockClass = blockClass;
    }

    protected final void tryAct(StateSmithData data, ItemModelSmith itemModelSmith, Supplier<? extends Block> registryObject, BiConsumer<T, LodestoneBlockStateSystem> actor) {
        var block = registryObject.get();
        if (blockClass.isInstance(block)) {
            var provider = data.provider();
            actor.accept(blockClass.cast(registryObject), provider);
            makeItemModel(data, itemModelSmith, block);
            data.consumer().accept(registryObject);
        } else {
            throw new IllegalArgumentException("Block does not match the state smith it was assigned: " + block.toString());
        }
    }

    protected final void makeItemModel(StateSmithData data, ItemModelSmith itemModelSmith, Block block) {
        if (!itemModelSmith.equals(ItemModelSmithTypes.NO_DATAGEN)) {
            itemModelSmith.act(data.provider().itemModelProvider, block::asItem);
        }
    }

    public record StateSmithData(LodestoneBlockStateSystem provider, Consumer<Supplier<? extends Block>> consumer) {
    }

    public interface StateFunction<T extends Block> {
        void act(T block, ModelFile modelFile);
    }
}