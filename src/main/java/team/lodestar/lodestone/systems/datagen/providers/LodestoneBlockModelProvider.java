package team.lodestar.lodestone.systems.datagen.providers;

import com.google.common.base.Preconditions;
import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import io.github.fabricators_of_create.porting_lib.models.generators.BlockModelBuilder;
import io.github.fabricators_of_create.porting_lib.models.generators.BlockModelProvider;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * It's the exact same thing as BlockModelProvider, except with a different factory and by extension, a different model builder.
 * We do this to more-easily allow for directory changes across several blocks, which basically just allows us to easily sort our block textures into separate folders within the block texture directory.
 */
public final class LodestoneBlockModelProvider extends BlockModelProvider {

    final Function<ResourceLocation, LodestoneBlockModelBuilder> factory;

    /**
     * Stores the textures used by the most recently generated block. Used for more easily generating item models based off of blocks which have weirdly specific custom item models, like walls.
     */
    public static final HashMap<String, ResourceLocation> BLOCK_TEXTURE_CACHE = new HashMap<>();


    public LodestoneBlockModelProvider(LodestoneBlockStateProvider provider, PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
        this.factory = l -> new LodestoneBlockModelBuilder(provider, l, existingFileHelper);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return super.run(cache);
    }

    @Override
    protected void registerModels() {

    }

    @Override
    public BlockModelBuilder getBuilder(String path) {
        Preconditions.checkNotNull(path, "Path must not be null");
        ResourceLocation outputLoc = extendWithFolder(path.contains(":") ? ResourceLocation.parse(path) : ResourceLocation.fromNamespaceAndPath(modid, path));
        this.existingFileHelper.trackGenerated(outputLoc, MODEL);
        return generatedModels.computeIfAbsent(outputLoc, factory);
    }

    @Override
    public BlockModelBuilder nested() {
        return factory.apply(ResourceLocation.parse("dummy:dummy"));
    }

    public ResourceLocation extendWithFolder(ResourceLocation rl) {
        if (rl.getPath().contains("/")) {
            return rl;
        }
        return ResourceLocation.fromNamespaceAndPath(rl.getNamespace(), folder + "/" + rl.getPath());
    }

    private static class LodestoneBlockModelBuilder extends BlockModelBuilder {

        public final LodestoneBlockStateProvider provider;

        public LodestoneBlockModelBuilder(LodestoneBlockStateProvider provider, ResourceLocation outputLocation, ExistingFileHelper existingFileHelper) {
            super(outputLocation, existingFileHelper);
            this.provider = provider;
            BLOCK_TEXTURE_CACHE.clear();
        }

        @Override
        public BlockModelBuilder texture(String key, ResourceLocation texture) {
            ResourceLocation actualLocation = texture;
            if (!texture.getNamespace().equals(ResourceLocation.DEFAULT_NAMESPACE) && !provider.staticTextures.contains(texture)) {
                String actualPath = texture.getPath().replace("block/", "block/" + LodestoneBlockStateProvider.getTexturePath());
                actualLocation = ResourceLocation.fromNamespaceAndPath(texture.getNamespace(), actualPath);
            }
            BLOCK_TEXTURE_CACHE.put(key, actualLocation);
            return super.texture(key, actualLocation);
        }
    }
}