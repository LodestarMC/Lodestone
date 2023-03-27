package team.lodestar.lodestone.systems.datagen.statesmith;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
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

        private String texturePath = "";

        public StateSmithData(BlockStateProvider provider, Consumer<RegistryObject<Block>> consumer) {
            this.provider = provider;
            this.consumer = consumer;
        }

        public String getTexturePath() {
            return texturePath;
        }

        public void setTexturePath(String texturePath) {
            this.texturePath = texturePath;
        }
    }

    public interface StateFunction<T extends Block> {
        void act(T block, ModelFile modelFile);
    }
}