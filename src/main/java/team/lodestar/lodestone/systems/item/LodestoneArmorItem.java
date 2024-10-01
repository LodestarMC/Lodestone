package team.lodestar.lodestone.systems.item;

import com.google.common.collect.*;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class LodestoneArmorItem extends ArmorItem {
    protected Multimap<Attribute, AttributeModifier> attributes;

    public LodestoneArmorItem(Holder<ArmorMaterial> pMaterial, Type pType, Properties pProperties) {
        super(pMaterial, pType, pProperties);
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

    public abstract List<ItemAttributeModifiers.Entry> createExtraAttributes();
}