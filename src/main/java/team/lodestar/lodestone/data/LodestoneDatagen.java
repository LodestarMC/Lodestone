package team.lodestar.lodestone.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

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
