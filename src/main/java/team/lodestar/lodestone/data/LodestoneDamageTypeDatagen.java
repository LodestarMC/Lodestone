package team.lodestar.lodestone.data;


import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import team.lodestar.lodestone.registry.common.tag.LodestoneDamageTypeTags;

import java.util.concurrent.CompletableFuture;

public class LodestoneDamageTypeDatagen extends FabricTagProvider<DamageType> {

    public LodestoneDamageTypeDatagen(FabricDataOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider) {
        super(pOutput, Registries.DAMAGE_TYPE, pProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        getOrCreateTagBuilder(LodestoneDamageTypeTags.IS_MAGIC).add(DamageTypes.MAGIC).add(DamageTypes.INDIRECT_MAGIC);
    }
}
