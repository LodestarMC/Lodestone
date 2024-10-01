package team.lodestar.lodestone.data;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator.Pack;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator.Pack.Factory;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator.Pack.RegistryDependentFactory;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;

import java.util.concurrent.CompletableFuture;

public class LodestoneDatagen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator gen) {
        FabricDataGenerator.Pack pack = gen.createPack();
        ExistingFileHelper efh = ExistingFileHelper.withResourcesFromArg();
        var generator = new ProviderProvider(pack, efh);

        var blocktags = generator.addProvider(LodestoneBlockTagDatagen::new);
        generator.addProvider((output, provider, helper) -> new LodestoneItemTagDatagen(output, provider, blocktags));
        generator.addProvider(LodestoneDamageTypeDatagen::new);
        generator.addProvider(LodestoneLangDatagen::new);
        generator.addProvider(LodestoneDamageTypeDatagen::new);
    }

    private record ProviderProvider(Pack pack, ExistingFileHelper helper) {
        public <T extends DataProvider> T addProvider(Factory<T> factory) {
            return pack.addProvider(factory);
        }

        public <T extends DataProvider> T addProvider(RegistryDependentFactory<T> factory) {
            return pack.addProvider(factory);
        }

        public <T extends DataProvider> T addProvider(EFHDependentFactory<T> factory) {
            return pack.addProvider((FabricDataOutput output) -> factory.create(output, helper));
        }

        public <T extends DataProvider> T addProvider(EFHRegistryDependentFactory<T> factory) {
            return pack.addProvider((output, registries) -> factory.create(output, registries, helper));
        }
    }

    @FunctionalInterface
    public interface EFHDependentFactory<T extends DataProvider> {
        T create(FabricDataOutput output, ExistingFileHelper helper);
    }

    @FunctionalInterface
    public interface EFHRegistryDependentFactory<T extends DataProvider> {
        T create(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper helper);
    }

}