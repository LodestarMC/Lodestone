package com.sammy.ortus.helpers.animation;

import com.sammy.ortus.helpers.AngleHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;

public class LerpedFloat {

    protected Interpolator interpolator;
    protected float previousValue;
    protected float currentValue;

    protected Chaser chaser;
    protected float chaseTarget;
    protected float chaseSpeed;

    protected boolean forcedSync;

    public LerpedFloat(Interpolator interpolator) {
        this.interpolator = interpolator;
        startWithValue(0);
        forcedSync = true;
    }

    public static LerpedFloat linear() {
        return new LerpedFloat((p, c, t) -> (float) Mth.lerp(p, c, t));
    }

    public static LerpedFloat angular() {
        return new LerpedFloat(AngleHelper::angleLerp);
    }

    public LerpedFloat startWithValue(double value) {
        float f = (float) value;
        this.previousValue = f;
        this.currentValue = f;
        this.chaseTarget = f;
        return this;
    }

    public LerpedFloat chase(double target, double speed, Chaser chaser) {
        this.chaseTarget = (float) target;
        this.chaseSpeed = (float) speed;
        this.chaser = chaser;
        return this;
    }

    public void updateChaseTarget(float target) {
        this.chaseTarget = target;
    }

    public void updateSpeed(float speed) {
        this.chaseSpeed = speed;
    }

    public boolean updateChaseSpeed(double speed) {
        float previousSpeed = this.chaseSpeed;
        this.chaseSpeed = (float) speed;
        return !Mth.equal(previousSpeed, speed);
    }

    public void tickChaser() {
        previousValue = currentValue;
        if (chaser == null) return;
        if (Mth.equal((double) currentValue, (double) chaseTarget)) {
            currentValue = chaseTarget;
            return;
        }
        currentValue = chaser.chase(currentValue, chaseSpeed, chaseTarget);
    }

    public void setValue(double value) {
        this.previousValue = this.currentValue;
        this.currentValue = (float) value;
    }

    public float getCurrentValue() {
        return getCurrentValue(1);
    }

    public float getCurrentValue(float partialTicks) {
        return Mth.lerp(partialTicks, previousValue, currentValue);
    }

    public boolean settled() {
        return Mth.equal((double) previousValue, currentValue);
    }

    public float getChaseTarget() {
        return chaseTarget;
    }

    public void forceNextSync() {
        forcedSync = true;
    }

    public CompoundTag writeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("Speed", chaseSpeed);
        tag.putFloat("Target", chaseTarget);
        tag.putFloat("Value", currentValue);
        if (forcedSync)
            tag.putBoolean("ForcedSync", true);
        return tag;
    }

    public void readNBT(CompoundTag tag, boolean forcedSync) {
        if (!forcedSync || tag.contains("ForcedSync"))
            startWithValue(tag.getFloat("Value"));
        readChaseNBT(tag);
    }

    protected void readChaseNBT(CompoundTag tag) {
        chaseSpeed = tag.getFloat("Speed");
        chaseTarget = tag.getFloat("Target");
    }

    @FunctionalInterface
    public interface Interpolator {
        Interpolator LINEAR = (p, c, t) -> (float) Mth.lerp(p, c, t);

        float interpolate(double t, float a, float b);
    }

    @FunctionalInterface
    public interface Chaser {
        Chaser IDLE = (c, s, t) -> (float) c;
        Chaser EXP = exp(Double.MAX_VALUE);
        Chaser LINEAR = (c, s, t) -> (float) (c + Mth.clamp(t - c, -s, s));

        static Chaser exp(double maxEffectiveSpeed) {
            return (c, s, t) -> (float) (c + Mth.clamp((t - c) * s, -maxEffectiveSpeed, maxEffectiveSpeed));
        }

        float chase(float current, float speed, float target);
    }
}