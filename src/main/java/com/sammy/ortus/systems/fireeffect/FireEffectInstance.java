package com.sammy.ortus.systems.fireeffect;

import com.sammy.ortus.registry.OrtusFireEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

public class FireEffectInstance {
    public int duration;
    public final FireEffectType type;

    public FireEffectInstance(FireEffectType type) {
        this.type = type;
    }

    public FireEffectInstance extendDuration(int increase) {
        this.duration += increase;
        return this;
    }

    public FireEffectInstance setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public void tick(Entity target) {
        if ((target.isInPowderSnow || target.isInWaterRainOrBubble())) {
            type.extinguish(this, target);
        }
        if (target.fireImmune()) {
            duration -= 4;
        } else {
            duration--;
            if (type.isValid(this) && duration % type.getTickInterval(this) == 0) {
                type.tick(this, target);
            }
        }
    }

    public boolean isValid() {
        return type.isValid(this);
    }

    public CompoundTag serializeNBT(CompoundTag tag) {
        tag.putString("type", type.id);
        tag.putInt("duration", duration);
        return tag;
    }

    public static FireEffectInstance deserializeNBT(CompoundTag tag) {
        FireEffectInstance instance = new FireEffectInstance(OrtusFireEffects.FIRE_TYPES.get(tag.getString("type")));
        instance.setDuration(tag.getInt("duration"));
        return instance;
    }
}