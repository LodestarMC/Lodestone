package team.lodestar.lodestone.systems.datagen;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import team.lodestar.lodestone.LodestoneLib;

import java.util.Collection;

public class BlockStateSmith<T extends Block> {

    public final Class<T> blockClass;
    public final SmithStateSupplier<T> stateSupplier;

    protected BlockStateSmith(Class<T> blockClass, SmithStateSupplier<T> stateSupplier) {
        this.blockClass = blockClass;
        this.stateSupplier = stateSupplier;
    }

    public void act(BlockStateProvider provider, Collection<Block> blocks) {
        blocks.forEach(block -> act(provider, block));
    }
    public void act(BlockStateProvider provider, Block block) {
        if (blockClass.isInstance(block)) {
            stateSupplier.act(blockClass.cast(block), provider);
        } else {
            LodestoneLib.LOGGER.warn("Block does not match the state smith it was assigned: " + block.getRegistryName());
        }
    }

    interface SmithStateSupplier<T extends Block> {
        void act(T block, BlockStateProvider provider);
    }
}