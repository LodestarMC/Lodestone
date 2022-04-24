package com.sammy.ortus.item.tools.magic;

import com.google.common.collect.ImmutableMultimap;
import com.sammy.ortus.item.tools.OrtusAxeItem;
import com.sammy.ortus.setup.OrtusAttributes;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;

public class MagicAxeItem extends OrtusAxeItem {

    public final float magicDamage;
    public MagicAxeItem(Tier material, float damage, float speed, float magicDamage, Item.Properties properties) {
        super(material, damage, speed, properties.durability(material.getUses()));
        this.magicDamage = magicDamage;
    }

    @Override
    public ImmutableMultimap.Builder<Attribute, AttributeModifier> createExtraAttributes() {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(OrtusAttributes.MAGIC_DAMAGE.get(), new AttributeModifier(OrtusAttributes.UUIDS.get(OrtusAttributes.MAGIC_DAMAGE), "Weapon magic damage", magicDamage, AttributeModifier.Operation.ADDITION));
        return builder;
    }
}