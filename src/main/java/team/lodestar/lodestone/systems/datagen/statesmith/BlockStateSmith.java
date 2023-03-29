package team.lodestar.lodestone.systems.datagen.statesmith;

import net.minecraft.world.level.block.*;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.systems.datagen.providers.LodestoneBlockStateProvider;

import java.util.Collection;
import java.util.function.Supplier;

public class BlockStateSmith<T extends Block> extends AbstractBlockStateSmith<T> {

    public final SmithStateSupplier<T> stateSupplier;

    public BlockStateSmith(Class<T> blockClass, SmithStateSupplier<T> stateSupplier) {
        super(blockClass);
        this.stateSupplier = stateSupplier;
    }

    @SafeVarargs
    public final void act(StateSmithData data, Supplier<Block>... blocks) {
        for (Supplier<Block> block : blocks) {
            act(data, block);
        }
    }

    public void act(StateSmithData data, Collection<Supplier<Block>> blocks) {
        blocks.forEach(r -> act(data, r));
    }

    private void act(StateSmithData data, Supplier<Block> registryObject) {
        Block block = registryObject.get();
        if (blockClass.isInstance(block)) {
            stateSupplier.act(blockClass.cast(block), data.provider);
            data.consumer.accept(registryObject);
        } else {
            LodestoneLib.LOGGER.warn("Block does not match the state smith it was assigned: " + ForgeRegistries.BLOCKS.getKey(block));
        }
    }

    public interface SmithStateSupplier<T extends Block> {
        void act(T block, LodestoneBlockStateProvider provider);
    }
}