package team.lodestar.lodestone.systems.datagen.statesmith;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.registries.RegistryObject;
import team.lodestar.lodestone.LodestoneLib;

import java.util.Collection;

public class BlockStateSmith<T extends Block> extends AbstractBlockStateSmith<T> {

    public final SmithStateSupplier<T> stateSupplier;

    public BlockStateSmith(Class<T> blockClass, SmithStateSupplier<T> stateSupplier) {
        super(blockClass);
        this.stateSupplier = stateSupplier;
    }

    public void act(BlockStateProvider provider, Collection<RegistryObject<Block>> blocks) {
        blocks.forEach(r -> act(provider, r.get()));
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