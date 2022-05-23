package com.sammy.ortus.systems.fireeffect;

import com.sammy.ortus.capability.OrtusEntityDataCapability;
import com.sammy.ortus.setup.OrtusFireEffectRegistry;
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

    public FireEffectInstance sync(Entity target) {
        OrtusEntityDataCapability.syncTrackingAndSelf(target, "fireEffect");
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

    public void serializeNBT(CompoundTag tag) {
        CompoundTag fireTag = new CompoundTag();
        tag.putString("type", type.id);
        tag.putInt("duration", duration);
        tag.put("fireEffect", fireTag);
    }

    public static FireEffectInstance deserializeNBT(CompoundTag tag) {
        CompoundTag fireTag = tag.getCompound("fireEffect");
        FireEffectInstance instance = new FireEffectInstance(OrtusFireEffectRegistry.FIRE_TYPES.get(fireTag.getString("type")));
        instance.setDuration(fireTag.getInt("duration"));
        return instance;
    }
}