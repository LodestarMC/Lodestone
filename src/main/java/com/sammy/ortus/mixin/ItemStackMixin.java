package com.sammy.ortus.mixin;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.sammy.ortus.setup.OrtusAttributeRegistry;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Map;

import static com.sammy.ortus.setup.OrtusAttributeRegistry.MAGIC_DAMAGE;
import static com.sammy.ortus.setup.OrtusAttributeRegistry.UUIDS;
import static net.minecraft.world.item.Item.BASE_ATTACK_DAMAGE_UUID;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Unique
    private AttributeModifier attributeModifier;

    @ModifyVariable(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;getId()Ljava/util/UUID;", ordinal = 0), index = 13)
    private AttributeModifier getTooltip(AttributeModifier value) {
        this.attributeModifier = value;
        return value;
    }

    @ModifyVariable(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;getOperation()Lnet/minecraft/world/entity/ai/attributes/AttributeModifier$Operation;", ordinal = 0), index = 16)
    private boolean getTooltip(boolean value, @Nullable Player player, TooltipFlag flag) {
        if (player != null) {
            if (attributeModifier.getId().equals(UUIDS.get(OrtusAttributeRegistry.MAGIC_DAMAGE))) {
                return true;
            }
        }
        return value;
    }

    @ModifyVariable(method = "getTooltipLines", at = @At("STORE"))
    private Multimap<Attribute, AttributeModifier> getTooltip(Multimap<Attribute, AttributeModifier> map, @Nullable Player player, TooltipFlag flag) {
        if (player != null) {
            Multimap<Attribute, AttributeModifier> copied = LinkedHashMultimap.create();
            for (Map.Entry<Attribute, AttributeModifier> entry : map.entries()) {
                Attribute key = entry.getKey();
                AttributeModifier modifier = entry.getValue();
                double amount = modifier.getAmount();
                if (modifier.getId().equals(UUIDS.get(MAGIC_DAMAGE))) {
                    AttributeInstance instance = player.getAttribute(OrtusAttributeRegistry.MAGIC_DAMAGE.get());
                    if (instance != null && instance.getValue() > 0) {
                        amount += instance.getValue();
                    }
                    instance = player.getAttribute(OrtusAttributeRegistry.MAGIC_PROFICIENCY.get());
                    if (instance != null && instance.getValue() > 0) {
                        amount += instance.getValue() * 0.5f;
                    }
                    copied.put(key, new AttributeModifier(
                            modifier.getId(), modifier.getName(), amount, modifier.getOperation()
                    ));
                } else {
                    copied.put(key, modifier);
                }
            }

            return copied;
        }
        return map;
    }
}