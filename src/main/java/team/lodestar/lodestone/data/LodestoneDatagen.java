package team.lodestar.lodestone.data;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import team.lodestar.lodestone.registry.common.tag.LodestoneItemTags;

public class LodestoneDatagen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
        var pack = dataGenerator.createPack();

        var blockTags = pack.addProvider(LodestoneBlockTagDatagen::new);

        pack.addProvider((output, provider) -> new LodestoneItemTagDatagen(output, provider, blockTags));
        pack.addProvider(LodestoneLangDatagen::new);
        pack.addProvider(LodestoneDamageTypeDatagen::new);
    }
}
