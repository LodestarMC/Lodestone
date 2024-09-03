package team.lodestar.lodestone.handlers;

import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.level.*;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import team.lodestar.lodestone.registry.common.LodestoneAttributes;
import team.lodestar.lodestone.registry.common.tag.*;

/**
 * A handler for common attributes I use in my mods.
 */
public class LodestoneAttributeEventHandler {
    public static void processAttributes(LivingDamageEvent.Pre event) {
        if (event.getOriginalDamage() <= 0) return;

        DamageSource source = event.getSource();
        LivingEntity target = event.getEntity();
        if (source.typeHolder().is(LodestoneDamageTypeTags.IS_MAGIC)) {
            float amount = event.getOriginalDamage();
            if (source.getEntity() instanceof LivingEntity attacker) {
                AttributeInstance magicProficiency = attacker.getAttribute(LodestoneAttributes.MAGIC_PROFICIENCY);
                if (magicProficiency != null && magicProficiency.getValue() > 0) {
                    amount *= (float) (1 + magicProficiency.getValue() * 0.1f);
                }
            }
            AttributeInstance magicResistance = target.getAttribute(LodestoneAttributes.MAGIC_RESISTANCE);
            if (magicResistance != null && magicResistance.getValue() > 0) {
                amount *= (float) applyMagicResistance(magicResistance.getValue());
            }
            event.setNewDamage(amount);
        }
        if (source.getEntity() instanceof LivingEntity attacker) {
            if (!source.typeHolder().is(LodestoneDamageTypeTags.IS_MAGIC)) {
                AttributeInstance magicDamage = attacker.getAttribute(LodestoneAttributes.MAGIC_DAMAGE);
                if (magicDamage != null) {
                    if (magicDamage.getValue() > 0 && !target.isDeadOrDying()) {
                        final Level level = source.getEntity().level();
                        final Holder.Reference<DamageType> holderOrThrow = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC);
                        DamageSource magic = new DamageSource(holderOrThrow, attacker, attacker);
                        target.invulnerableTime = 0;
                        target.hurt(magic, (float) magicDamage.getValue());
                    }
                }
            }
        }
    }

    public static double applyMagicResistance(double magicResistance) {
        if (magicResistance >= 20) {
            return Math.max(0.25f, 0.5f - (magicResistance-20)*0.0125f);
        }
        else {
            return 1 - magicResistance * 0.025f;
        }
    }
}