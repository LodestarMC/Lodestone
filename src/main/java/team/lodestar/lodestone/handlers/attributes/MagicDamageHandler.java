package team.lodestar.lodestone.handlers.attributes;

import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.neoforged.neoforge.event.entity.living.*;
import team.lodestar.lodestone.helpers.*;
import team.lodestar.lodestone.registry.common.*;
import team.lodestar.lodestone.registry.common.tag.*;

public class MagicDamageHandler {

    public static void processAttributes(LivingDamageEvent.Pre event) {
        if (event.getOriginalDamage() <= 0) {
            return;
        }
        var source = event.getSource();
        var target = event.getEntity();
        var damageType = source.typeHolder();

        float amount = event.getOriginalDamage();


        if (damageType.is(LodestoneDamageTypeTags.AFFECTED_BY_MAGIC_RESISTANCE)) {
            var magicResistance = target.getAttribute(LodestoneAttributes.MAGIC_RESISTANCE);
            if (magicResistance != null) {
                amount /= (float) Math.max(magicResistance.getValue(), 0.01f);
            }
        }
        if (damageType.is(LodestoneDamageTypeTags.AFFECTED_BY_MAGIC_PROFICIENCY)) {
            if (source.getEntity() instanceof LivingEntity attacker) {
                var magicProficiency = attacker.getAttribute(LodestoneAttributes.MAGIC_PROFICIENCY);
                if (magicProficiency != null) {
                    amount *= (float) magicProficiency.getValue();
                }
            }
        }
        event.setNewDamage(amount);
    }

    public static void triggerMagicDamage(LivingDamageEvent.Post event) {
        if (event.getNewDamage() <= 0) {
            return;
        }
        var source = event.getSource();
        var target = event.getEntity();
        var damageType = source.typeHolder();
        if (target.isDeadOrDying()) {
            return;
        }
        if (!(source.getEntity() instanceof LivingEntity attacker)) {
            return;
        }
        if (!damageType.is(LodestoneDamageTypeTags.CAN_TRIGGER_MAGIC_DAMAGE)) {
            return;
        }
        var attribute = attacker.getAttribute(LodestoneAttributes.MAGIC_DAMAGE);
        if (attribute == null || attribute.getValue() <= 0) {
            return;
        }
        float damage = (float) attribute.getValue();
        if (attacker instanceof Player player) {
            if (!damageType.is(LodestoneDamageTypeTags.IGNORES_MAGIC_ATTACK_COOLDOWN_SCALAR)) {
                damage *= player.getAttackStrengthScale(0);
            }
        }
        var magic = DamageTypeHelper.create(DamageTypes.MAGIC, attacker);
        target.invulnerableTime = 0;
        target.hurt(magic, damage);
    }
}