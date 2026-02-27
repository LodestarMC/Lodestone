package team.lodestar.lodestone.datagen.implementation;

import net.minecraft.core.*;
import net.minecraft.core.HolderLookup.*;
import net.minecraft.data.*;
import net.minecraft.data.tags.*;
import net.minecraft.world.damagesource.*;
import net.neoforged.neoforge.common.data.*;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.registry.common.tag.*;

import java.util.concurrent.*;

public class LodestoneDamageTypeDatagen extends DamageTypeTagsProvider {

    public LodestoneDamageTypeDatagen(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider, ExistingFileHelper existingFileHelper) {
        super(pOutput, pProvider, LodestoneLib.LODESTONE, existingFileHelper);
    }

    @Override
    protected void addTags(Provider pProvider) {
        tag(LodestoneDamageTypeTags.IS_MAGIC).add(DamageTypes.MAGIC, DamageTypes.INDIRECT_MAGIC);
        tag(LodestoneDamageTypeTags.AFFECTED_BY_MAGIC_RESISTANCE).addTag(LodestoneDamageTypeTags.IS_MAGIC);
        tag(LodestoneDamageTypeTags.AFFECTED_BY_MAGIC_PROFICIENCY).addTag(LodestoneDamageTypeTags.IS_MAGIC);
        tag(LodestoneDamageTypeTags.CAN_TRIGGER_MAGIC_DAMAGE).add(DamageTypes.PLAYER_ATTACK);
        tag(LodestoneDamageTypeTags.IGNORES_MAGIC_ATTACK_COOLDOWN_SCALAR);
    }
}
