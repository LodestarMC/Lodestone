package team.lodestar.lodestone.systems.item.tools;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.ArrayList;
import java.util.List;

public class LodestoneHoeItem extends HoeItem {
    private Multimap<Attribute, AttributeModifier> attributes;

    public LodestoneHoeItem(Tier material, int damage, float speed, Properties properties) {
        super(material, properties.durability(material.getUses()).attributes(createAttributes(material, damage - 2, speed)));
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers() {
        ItemAttributeModifiers modifiers = super.getDefaultAttributeModifiers();
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();

        List<ItemAttributeModifiers.Entry> entries = modifiers.modifiers();
        for (ItemAttributeModifiers.Entry entry : entries) {
            builder.add(entry.attribute(), entry.modifier(), entry.slot());
        }

        List<ItemAttributeModifiers.Entry> extraEntries = createExtraAttributes();
        for (ItemAttributeModifiers.Entry entry : extraEntries) {
            builder.add(entry.attribute(), entry.modifier(), entry.slot());
        }

        return builder.build();
    }

    public List<ItemAttributeModifiers.Entry> createExtraAttributes() {
        return new ArrayList<>();
    }
}

