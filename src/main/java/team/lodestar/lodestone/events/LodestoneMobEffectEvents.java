package team.lodestar.lodestone.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public interface LodestoneMobEffectEvents {

    Event<Applicable> APPLICABLE = EventFactory.createArrayBacked(Applicable.class, callbacks -> ((livingEntity, effect) -> {
        for (Applicable e : callbacks) {
            return e.canBeAffected(livingEntity, effect);
        }
        return true;
    }));

    Event<Added> ADDED = EventFactory.createArrayBacked(Added.class, callbacks -> ((livingEntity, oldEffect, newEffect, source) -> {
        for (Added e : callbacks) {
            return e.canBeAffected(livingEntity, oldEffect, newEffect, source);
        }
        return true;
    }));

    @FunctionalInterface
    interface Applicable {
        boolean canBeAffected(LivingEntity livingEntity, MobEffectInstance effect);
    }

    @FunctionalInterface
    interface Added {
        boolean canBeAffected(LivingEntity livingEntity, MobEffectInstance oldEffect, MobEffectInstance newEffect, Entity source);
    }
}
