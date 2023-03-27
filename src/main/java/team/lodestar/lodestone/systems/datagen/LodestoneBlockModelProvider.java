package team.lodestar.lodestone.systems.datagen;

import com.google.common.base.Preconditions;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Function;

/**
 * It's the exact same thing as BlockModelProvider, except with a different factory and by extension, a different model builder.
 * We do this to more-easily allow for directory changes across several blocks, which basically just allows us to easily sort our block textures into separate folders within the block texture directory.
 */
public class LodestoneBlockModelProvider extends BlockModelProvider {
    protected final Function<ResourceLocation, LodestoneBlockModelBuilder> factory;

    public LodestoneBlockModelProvider(LodestoneBlockStateProvider provider, DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
        super(generator, modid, existingFileHelper);
        this.factory = l -> new LodestoneBlockModelBuilder(provider, l, existingFileHelper);
    }

    @Override
    public void run(CachedOutput cache) {
    }

    @Override
    protected void registerModels() {

    }

    @Override
    public BlockModelBuilder getBuilder(String path) {
        Preconditions.checkNotNull(path, "Path must not be null");
        ResourceLocation outputLoc = extendWithFolder(path.contains(":") ? new ResourceLocation(path) : new ResourceLocation(modid, path));
        this.existingFileHelper.trackGenerated(outputLoc, MODEL);
        return generatedModels.computeIfAbsent(outputLoc, factory);
    }

    @Override
    public BlockModelBuilder nested() {
        return factory.apply(new ResourceLocation("dummy:dummy"));
    }

    public ResourceLocation extendWithFolder(ResourceLocation rl) {
        if (rl.getPath().contains("/")) {
            return rl;
        }
        return new ResourceLocation(rl.getNamespace(), folder + "/" + rl.getPath());
    }

    private static class LodestoneBlockModelBuilder extends BlockModelBuilder {
        public final LodestoneBlockStateProvider provider;
        public LodestoneBlockModelBuilder(LodestoneBlockStateProvider provider, ResourceLocation outputLocation, ExistingFileHelper existingFileHelper) {
            super(outputLocation, existingFileHelper);
            this.provider = provider;
        }

        @Override
        public BlockModelBuilder texture(String key, ResourceLocation texture) {
            String actualPath = texture.getPath().replace("block/", "block/"+provider.getTexturePath());
            ResourceLocation actualLocation = new ResourceLocation(texture.getNamespace(), actualPath);
            return super.texture(key, actualLocation);
        }
    }
}