package team.lodestar.lodestone.systems.datagen.statesmith;

import net.minecraft.world.level.block.*;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import team.lodestar.lodestone.LodestoneLib;

import java.util.Collection;
import java.util.function.Consumer;

public class BlockStateSmith<T extends Block> extends AbstractBlockStateSmith<T> {

    public final SmithStateSupplier<T> stateSupplier;

    public BlockStateSmith(Class<T> blockClass, SmithStateSupplier<T> stateSupplier) {
        super(blockClass);
        this.stateSupplier = stateSupplier;
    }

    @SafeVarargs
    public final void act(StateSmithData data, RegistryObject<Block>... blocks) {
        for (RegistryObject<Block> block : blocks) {
            act(data, block);
        }
    }

    public void act(StateSmithData data, Collection<RegistryObject<Block>> blocks) {
        blocks.forEach(r -> act(data, r));
    }

    private void act(StateSmithData data, RegistryObject<Block> block) {
        if (blockClass.isInstance(block)) {
            stateSupplier.act(blockClass.cast(block.get()), data.provider, data.texturePath);
            data.consumer.accept(block);
        } else {
            LodestoneLib.LOGGER.warn("Block does not match the state smith it was assigned: " + ForgeRegistries.BLOCKS.getKey(block.get()));
        }
    }

    interface SmithStateSupplier<T extends Block> {
        void act(T block, BlockStateProvider provider, String texturePath);
    }
}