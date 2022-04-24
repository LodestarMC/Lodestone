package com.sammy.ortus.helpers;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class EntityHelper {

    public static void giveAmplifyingEffect(MobEffect effect, LivingEntity target, int duration, int amplifier, int cap) {
        MobEffectInstance instance = target.getEffect(effect);
        if (instance != null) {
            amplifier += instance.getAmplifier() + 1;
        }
        target.addEffect(new MobEffectInstance(effect, duration, Math.min(amplifier, cap)));
    }

    public static void giveStackingEffect(MobEffect effect, LivingEntity target, int duration, int amplifier) {
        MobEffectInstance instance = target.getEffect(effect);
        if (instance != null) {
            duration += instance.getDuration();
        }
        target.addEffect(new MobEffectInstance(effect, duration, amplifier));
    }
}