package team.lodestar.lodestone.systems.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import team.lodestar.lodestone.systems.block.LodestoneBlockProperties;

import java.util.Collection;

public abstract class LodestoneBlockStateProvider extends BlockStateProvider {
    public LodestoneBlockStateProvider(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
        super(gen, modid, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

    }

    public void buildBlockStatesFromBlockProperties(Collection<RegistryObject<Block>> blocks) {
        blocks.stream().map(RegistryObject::get).forEach(b -> {
            LodestoneBlockProperties properties = (LodestoneBlockProperties) b.properties;
            LodestoneDatagenBlockData data = properties.getDatagenData();
            BlockStateSmith<?> stateSmith = data.getStateSmith();
            stateSmith.act(this, b);
        });
    }
}
