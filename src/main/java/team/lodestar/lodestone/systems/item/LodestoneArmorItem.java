package team.lodestar.lodestone.systems.item;

import com.google.common.collect.*;
import io.github.fabricators_of_create.porting_lib.item.ArmorTextureItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class LodestoneArmorItem extends ArmorItem implements ArmorTextureItem {
    protected Multimap<Attribute, AttributeModifier> attributes;

    public LodestoneArmorItem(ArmorMaterial materialIn, ArmorItem.Type type, Properties builder) {
        super(materialIn, type, builder);
    }

    public abstract Multimap<Attribute, AttributeModifier> createExtraAttributes(ArmorItem.Type type);

    @Override
    @NotNull
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot equipmentSlot) {
        if (attributes == null) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> attributeBuilder = new ImmutableMultimap.Builder<>();
            attributeBuilder.putAll(defaultModifiers);
            attributeBuilder.putAll(createExtraAttributes(type));
            this.attributes = attributeBuilder.build();
        }
        return equipmentSlot == this.type.getSlot() ? this.attributes : ImmutableMultimap.of();
    }

    public String getTexture() {
        return null;
    }

    public String getTextureLocation() {
        return null;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return getTextureLocation() + getTexture() + ".png";
    }
}