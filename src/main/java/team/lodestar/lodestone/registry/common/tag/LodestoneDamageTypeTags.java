package team.lodestar.lodestone.registry.common.tag;

import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.tags.*;
import net.minecraft.world.damagesource.*;

public class LodestoneDamageTypeTags {

    public static final TagKey<DamageType> IS_MAGIC = forgeTag("is_magic");

    public static TagKey<DamageType> forgeTag(String path) {
        return TagKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath("forge", path));
    }
}