package team.lodestar.lodestone.toolkit.screenshake;

import net.minecraft.world.phys.*;
import team.lodestar.lodestone.core.easing.Easing;

import java.util.*;

public class ScreenshakeBuilder {

    public static ScreenshakeBuilder create() {
        return new ScreenshakeBuilder();
    }

    protected int duration = 20;
    protected float startingStrength = 1f;
    protected float middleStrength = 0f;
    protected float endingStrength = 0f;
    protected Easing startingCurve = Easing.LINEAR;
    protected Easing endingCurve = Easing.LINEAR;
    protected float coefficient = 1.0f;

    protected ScreenshakeInstance.ScreenshakePositionData positionData;

    public ScreenshakeBuilder setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public ScreenshakeBuilder setStrength(float strength) {
        return setStrength(strength, strength);
    }

    public ScreenshakeBuilder setStrength(float startingStrength, float endingStrength) {
        return setStrength(startingStrength, endingStrength, endingStrength);
    }

    public ScreenshakeBuilder setStrength(float startingStrength, float middleStrength, float endingStrength) {
        return setStartingStrength(startingStrength).setMiddleStrength(middleStrength).setEndingStrength(endingStrength);
    }

    protected ScreenshakeBuilder setStartingStrength(float startingStrength) {
        this.startingStrength = startingStrength;
        return this;
    }

    protected ScreenshakeBuilder setMiddleStrength(float middleStrength) {
        this.middleStrength = middleStrength;
        return this;
    }

    protected ScreenshakeBuilder setEndingStrength(float endingStrength) {
        this.endingStrength = endingStrength;
        return this;
    }

    public ScreenshakeBuilder setEasing(Easing curve) {
        return setEasing(curve, curve);
    }

    public ScreenshakeBuilder setEasing(Easing startingCurve, Easing endingCurve) {
        return setStartingEasing(startingCurve).setEndingEasing(endingCurve);
    }

    protected ScreenshakeBuilder setStartingEasing(Easing startingCurve) {
        this.startingCurve = startingCurve;
        return this;
    }

    protected ScreenshakeBuilder setEndingEasing(Easing endingCurve) {
        this.endingCurve = endingCurve;
        return this;
    }

    public ScreenshakeBuilder setCoefficient(float coefficient) {
        this.coefficient = coefficient;
        return this;
    }

    public ScreenshakeBuilder placedAt(Vec3 center, float falloffDistance) {
        return placedAt(center, falloffDistance, Easing.LINEAR);
    }

    public ScreenshakeBuilder placedAt(Vec3 center, float falloffDistance, Easing falloffCurve) {
        return placedAt(new ScreenshakeInstance.ScreenshakePositionData(center, falloffDistance, falloffCurve));
    }

    public ScreenshakeBuilder placedAt(ScreenshakeInstance.ScreenshakePositionData positionData) {
        this.positionData = positionData;
        return this;
    }

    public ScreenshakeInstance build() {
        return new ScreenshakeInstance(duration, startingStrength, middleStrength, endingStrength, startingCurve, endingCurve, coefficient, Optional.ofNullable(positionData));
    }
}