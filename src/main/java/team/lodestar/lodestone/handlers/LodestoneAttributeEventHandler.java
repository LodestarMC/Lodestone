package team.lodestar.lodestone.handlers;

import io.github.fabricators_of_create.porting_lib.entity.events.living.LivingHurtEvent;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.level.*;
import team.lodestar.lodestone.registry.common.LodestoneAttributeRegistry;
import team.lodestar.lodestone.registry.common.tag.*;


/**
 * A handler for common attributes I use in my mods.
 */
public class LodestoneAttributeEventHandler {
    public static void processAttributes(LivingHurtEvent event) {
        if (event.isCanceled() || event.getAmount() <= 0) {
            return;
        }
        DamageSource source = event.getSource();
        LivingEntity target = event.getEntity();
        if (source.typeHolder().is(LodestoneDamageTypeTags.IS_MAGIC)) {
            float amount = event.getAmount();
            if (source.getEntity() instanceof LivingEntity attacker) {
                AttributeInstance magicProficiency = attacker.getAttribute(LodestoneAttributeRegistry.MAGIC_PROFICIENCY.get());
                if (magicProficiency != null && magicProficiency.getValue() > 0) {
                    amount *= (1 + magicProficiency.getValue() * 0.1f);
                }
            }
            AttributeInstance magicResistance = target.getAttribute(LodestoneAttributeRegistry.MAGIC_RESISTANCE.get());
            if (magicResistance != null && magicResistance.getValue() > 0) {
                amount *= applyMagicResistance(magicResistance.getValue());
            }
            event.setAmount(amount);
        }
        if (source.getEntity() instanceof LivingEntity attacker) {
            if (!source.typeHolder().is(LodestoneDamageTypeTags.IS_MAGIC)) {
                AttributeInstance magicDamage = attacker.getAttribute(LodestoneAttributeRegistry.MAGIC_DAMAGE.get());
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
        return (1 - (0.75 * (1 / (0.2 * (magicResistance + 1))))) * 0.8;
    }
}