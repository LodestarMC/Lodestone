package team.lodestar.lodestone.data;


import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.world.damagesource.DamageTypes;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.registry.common.tag.LodestoneDamageTypeTags;

import java.util.concurrent.*;

public class LodestoneDamageTypeDatagen extends DamageTypeTagsProvider {

    public LodestoneDamageTypeDatagen(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider) {
        super(pOutput, pProvider );
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(LodestoneDamageTypeTags.IS_MAGIC).add(DamageTypes.MAGIC, DamageTypes.INDIRECT_MAGIC);
    }
}
