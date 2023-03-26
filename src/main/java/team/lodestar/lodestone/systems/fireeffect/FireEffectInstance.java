package team.lodestar.lodestone.systems.fireeffect;

import team.lodestar.lodestone.handlers.FireEffectHandler;
import team.lodestar.lodestone.helpers.NBTHelper;
import team.lodestar.lodestone.registry.common.LodestoneFireEffectRegistry;
import team.lodestar.lodestone.capability.LodestoneEntityDataCapability;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

/**
 * A FireEffectInstance is a custom instance of a fire effect, functioning pretty much exactly as a normal fire effect would do
 * You must register a type and can manage a players fire effect through the {@link FireEffectHandler}
 */
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
        LodestoneEntityDataCapability.syncTrackingAndSelf(target, NBTHelper.create("fireEffect", "type", "duration").setWhitelist());
        return this;
    }

    public FireEffectInstance syncDuration(Entity target) {
        LodestoneEntityDataCapability.syncTrackingAndSelf(target, NBTHelper.create("fireEffect", "duration").setWhitelist());
        return this;
    }

    public void tick(Entity target) {
        if ((target.isInPowderSnow || target.isInWaterRainOrBubble())) {
            type.extinguish(this, target);
        }
        if (canDamageTarget(target)) {
            duration--;
            if (type.isValid(this) && duration % type.getTickInterval(this) == 0) {
                type.tick(this, target);
            }
        } else {
            duration -= 4;
        }
    }

    public void entityAttack() {

    }

    public boolean canDamageTarget(Entity target) {
        return !target.fireImmune();
    }

    public boolean isValid() {
        return type.isValid(this);
    }

    public void serializeNBT(CompoundTag tag) {
        CompoundTag fireTag = new CompoundTag();
        fireTag.putString("type", type.id);
        fireTag.putInt("duration", duration);
        tag.put("fireEffect", fireTag);
    }

    public static FireEffectInstance deserializeNBT(CompoundTag tag) {
        if (!tag.contains("fireEffect")) {
            return null;
        }
        CompoundTag fireTag = tag.getCompound("fireEffect");
        FireEffectInstance instance = new FireEffectInstance(LodestoneFireEffectRegistry.FIRE_TYPES.get(fireTag.getString("type")));
        instance.setDuration(fireTag.getInt("duration"));
        return instance;
    }
}