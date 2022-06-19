package com.sammy.ortus.helpers;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Objects;

public class EntityHelper {

    public static void amplifyEffect(MobEffectInstance instance, LivingEntity target, int addedAmplifier, int cap) {
        target.effectsDirty = true;
        instance.amplifier = Math.min(cap, instance.getAmplifier() + addedAmplifier);
        target.onEffectUpdated(instance, true, target);
    }

    public static void amplifyEffect(MobEffectInstance instance, LivingEntity target, int addedAmplifier) {
        target.effectsDirty = true;
        instance.amplifier = instance.getAmplifier() + addedAmplifier;
        target.onEffectUpdated(instance, true, target);
    }

    public static void extendEffect(MobEffectInstance instance, LivingEntity target, int addedDuration, int cap) {
        target.effectsDirty = true;
        instance.duration = Math.min(cap, instance.getDuration() + addedDuration);
        target.onEffectUpdated(instance, true, target);
    }

    public static void extendEffect(MobEffectInstance instance, LivingEntity target, int addedDuration) {
        target.effectsDirty = true;
        instance.duration = instance.getDuration() + addedDuration;
        target.onEffectUpdated(instance, true, target);
    }

    public static void shortenEffect(MobEffectInstance instance, LivingEntity target, int removedDuration) {
        target.effectsDirty = true;
        instance.duration = instance.getDuration() - removedDuration;
        target.onEffectUpdated(instance, true, target);
    }


    /**
     * Tracks the travel path of an entity or other object
     *
     * @param pastPositions     An ArrayList that houses all the past positions.
     * @param currentPosition   The current position to be added to the list.
     * @param distanceThreshold the minimum distance from the latest PastPos before a new position is added.
     */
    public static void trackPastPositions(ArrayList<PastPosition> pastPositions, Vec3 currentPosition, float distanceThreshold) {
        for (PastPosition pastPosition : pastPositions) {
            pastPosition.time++;
        }
        if (!pastPositions.isEmpty()) {
            PastPosition latest = pastPositions.get(pastPositions.size() - 1);
            float distance = (float) latest.position.distanceTo(currentPosition);
            if (distance > distanceThreshold) {
                pastPositions.add(new PastPosition(currentPosition, 0));
            }
        } else {
            pastPositions.add(new PastPosition(currentPosition, 0));
        }
    }

    public static class PastPosition {
        public Vec3 position;
        public int time;

        public PastPosition(Vec3 position, int time) {
            this.position = position;
            this.time = time;
        }
    }
}