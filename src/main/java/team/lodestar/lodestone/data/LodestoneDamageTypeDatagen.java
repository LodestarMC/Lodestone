package team.lodestar.lodestone.data;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.minecraft.core.*;
import net.minecraft.core.HolderLookup.*;
import net.minecraft.data.*;
import net.minecraft.data.tags.*;
import net.minecraft.world.damagesource.*;
import team.lodestar.lodestone.*;
import team.lodestar.lodestone.registry.common.tag.*;

import java.util.concurrent.*;

public class LodestoneDamageTypeDatagen extends DamageTypeTagsProvider {

    public LodestoneDamageTypeDatagen(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider) {
        super(pOutput, pProvider);
    }

    @Override
    protected void addTags(Provider pProvider) {
        tag(LodestoneDamageTypeTags.IS_MAGIC).add(DamageTypes.MAGIC, DamageTypes.INDIRECT_MAGIC);
    }
}
