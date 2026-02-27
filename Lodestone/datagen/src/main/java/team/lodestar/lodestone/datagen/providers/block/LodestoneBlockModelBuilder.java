package team.lodestar.lodestone.datagen.providers.block;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import team.lodestar.lodestone.datagen.DatagenSystemCommons;

@SuppressWarnings("NullableProblems")
public class LodestoneBlockModelBuilder extends BlockModelBuilder {

    public LodestoneBlockModelBuilder(ResourceLocation outputLocation, ExistingFileHelper existingFileHelper) {
        super(outputLocation, existingFileHelper);
    }

    @Override
    public BlockModelBuilder texture(String key, ResourceLocation path) {
        var modified = DatagenSystemCommons.modifyTexturePath(path);
        return super.texture(key, modified);
    }

    @Override
    public BlockModelBuilder parent(ModelFile parent) {
        var location = parent.getLocation();
        var modified = DatagenSystemCommons.modifyModelPath(location);
        return super.parent(new UncheckedModelFile(modified));
    }
}
