package team.lodestar.lodestone.systems.datagen.statesmith;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.registries.RegistryObject;
import team.lodestar.lodestone.LodestoneLib;

import java.util.Collection;
import java.util.function.Consumer;

public abstract class AbstractBlockStateSmith<T extends Block> {

    public final Class<T> blockClass;

    public AbstractBlockStateSmith(Class<T> blockClass) {
        this.blockClass = blockClass;
    }

    public static class StateSmithData {
        public final BlockStateProvider provider;
        public final Consumer<RegistryObject<Block>> consumer;
        public String texturePath = "";

        public StateSmithData(BlockStateProvider provider, Consumer<RegistryObject<Block>> consumer) {
            this.provider = provider;
            this.consumer = consumer;
        }
    }
}