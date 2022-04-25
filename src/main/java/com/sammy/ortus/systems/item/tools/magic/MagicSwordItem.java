package com.sammy.ortus.systems.item.tools.magic;

import com.google.common.collect.ImmutableMultimap;
import com.sammy.ortus.systems.item.tools.OrtusSwordItem;
import com.sammy.ortus.setup.OrtusAttributes;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;

public class MagicSwordItem extends OrtusSwordItem {
    public final float magicDamage;

    public MagicSwordItem(Tier material, int attackDamage, float attackSpeed, float magicDamage, Item.Properties properties) {
        super(material, attackDamage, attackSpeed, properties.durability(material.getUses()));
        this.magicDamage = magicDamage;
    }

    @Override
    public ImmutableMultimap.Builder<Attribute, AttributeModifier> createExtraAttributes() {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(OrtusAttributes.MAGIC_DAMAGE.get(), new AttributeModifier(OrtusAttributes.UUIDS.get(OrtusAttributes.MAGIC_DAMAGE), "Weapon magic damage", magicDamage, AttributeModifier.Operation.ADDITION));
        return builder;
    }
}
