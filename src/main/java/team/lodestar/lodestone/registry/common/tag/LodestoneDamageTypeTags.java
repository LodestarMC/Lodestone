package team.lodestar.lodestone.registry.common.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public class LodestoneDamageTypeTags {

    public static final TagKey<DamageType> IS_MAGIC = forgeTag("is_magic");

    public static TagKey<DamageType> forgeTag(String path) {
        return TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("c", path));
    }
}