package com.sammy.ortus.helpers;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;

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
    /**
     * Tracks the travel path of of an entity or other object
     * @param pastPositions An ArrayList that houses all the past positions.
     * @param currentPosition The current position to be added to the list.
     * @param distanceThreshold the minimum distance from the latest PastPos before a new position is added.
     */
    public static void trackPastPositions(ArrayList<Vec3> pastPositions, Vec3 currentPosition, float distanceThreshold) {
        if (!pastPositions.isEmpty()) {
            Vec3 latest = pastPositions.get(pastPositions.size() - 1);
            float distance = (float) latest.distanceTo(currentPosition);
            if (distance > distanceThreshold) {
                pastPositions.add(currentPosition);
            }
        } else {
            pastPositions.add(currentPosition);
        }
    }
}

