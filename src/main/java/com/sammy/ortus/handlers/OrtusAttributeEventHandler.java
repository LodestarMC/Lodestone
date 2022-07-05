package com.sammy.ortus.handlers;

import com.sammy.ortus.setup.OrtusAttributeRegistry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

/**
 * A handler for common attributes I use in my mods.
 */
public class OrtusAttributeEventHandler {
    public static void processAttributes(LivingHurtEvent event) {
        if (event.isCanceled() || event.getAmount() <= 0) {
            return;
        }
        DamageSource source = event.getSource();
        LivingEntity target = event.getEntityLiving();
        if (source.isMagic()) {
            float amount = event.getAmount();
            if (source.getEntity() instanceof LivingEntity attacker) {
                AttributeInstance magicProficiency = attacker.getAttribute(OrtusAttributeRegistry.MAGIC_PROFICIENCY.get());
                if (magicProficiency != null && magicProficiency.getValue() > 0) {
                    amount += magicProficiency.getValue() * 0.5f;
                }
            }
            AttributeInstance magicResistance = target.getAttribute(OrtusAttributeRegistry.MAGIC_RESISTANCE.get());
            if (magicResistance != null && magicResistance.getValue() > 0) {
                amount *= applyMagicResistance(magicResistance.getValue());
            }
            event.setAmount(amount);
        }
        if (source.getEntity() instanceof LivingEntity attacker) {
            if (!source.isMagic()) {
                AttributeInstance magicDamage = attacker.getAttribute(OrtusAttributeRegistry.MAGIC_DAMAGE.get());
                if (magicDamage != null) {
                    if (magicDamage.getValue() > 0 && target.isAlive()) {
                        target.invulnerableTime = 0;
                        target.hurt(new EntityDamageSource(DamageSource.MAGIC.getMsgId(), attacker).setMagic(), (float) magicDamage.getValue());
                    }
                }
            }
        }
    }

    public static double applyMagicResistance(double magicResistance) {
        return (1 - (0.75 * (1 / (0.2 * (magicResistance+1))))) * 0.8;
    }
}