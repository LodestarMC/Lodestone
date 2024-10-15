package team.lodestar.lodestone.handlers;

import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.resources.*;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.level.*;
import net.neoforged.neoforge.event.entity.living.*;
import team.lodestar.lodestone.helpers.*;
import team.lodestar.lodestone.registry.common.*;
import team.lodestar.lodestone.registry.common.tag.*;

import javax.annotation.*;

/**
 * A handler for common attributes I use in my mods.
 */
public class LodestoneAttributeEventHandler {
    public static void processAttributes(LivingDamageEvent.Pre event) {
        if (event.getOriginalDamage() <= 0) {
            return;
        }
        var source = event.getSource();
        var target = event.getEntity();

        float amount = event.getOriginalDamage();
        if (source.typeHolder().is(LodestoneDamageTypeTags.IS_MAGIC)) {
            var magicResistance = target.getAttribute(LodestoneAttributes.MAGIC_RESISTANCE);
            if (magicResistance != null) {
                amount /= (float) Math.max(magicResistance.getValue(), 0.01f);
            }
        }
        if (source.getEntity() instanceof LivingEntity attacker) {
            if (source.typeHolder().is(LodestoneDamageTypeTags.IS_MAGIC)) {
                var magicProficiency = attacker.getAttribute(LodestoneAttributes.MAGIC_PROFICIENCY);
                if (magicProficiency != null) {
                    amount *= (float) magicProficiency.getValue();
                }
                event.setNewDamage(amount);
            }
            else if (source.typeHolder().is(LodestoneDamageTypeTags.CAN_TRIGGER_MAGIC)) {
                AttributeInstance magicDamage = attacker.getAttribute(LodestoneAttributes.MAGIC_DAMAGE);
                if (magicDamage != null) {
                    if (magicDamage.getValue() > 0 && !target.isDeadOrDying()) {
                        var magic = DamageTypeHelper.create(DamageTypes.MAGIC, attacker);
                        target.invulnerableTime = 0;
                        target.hurt(magic, (float) magicDamage.getValue());
                    }
                }
            }
        }
    }
}