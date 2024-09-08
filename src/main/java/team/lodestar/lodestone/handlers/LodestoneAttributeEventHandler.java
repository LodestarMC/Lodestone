package team.lodestar.lodestone.handlers;

import io.github.fabricators_of_create.porting_lib.entity.events.living.LivingHurtEvent;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.level.Level;
import team.lodestar.lodestone.registry.common.LodestoneAttributeRegistry;
import team.lodestar.lodestone.registry.common.tag.LodestoneDamageTypeTags;


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
        if (magicResistance >= 20) {
            return Math.max(0.25f, 0.5f - (magicResistance - 20) * 0.0125f);
        } else {
            return 1 - magicResistance * 0.025f;
        }
    }
}