package team.lodestar.lodestone.systems.rendering.particle;

import team.lodestar.lodestone.systems.easing.Easing;
import net.minecraft.client.particle.ParticleRenderType;

public class SimpleParticleOptions {
    public enum Animator {
        FIRST_INDEX, LAST_INDEX, WITH_AGE, RANDOM_SPRITE
    }
    public enum MotionStyle {
        START_TO_END, CURRENT_TO_END
    }
    public enum SpecialRemovalProtocol {
        NONE, INVISIBLE, ENDING_CURVE_INVISIBLE
    }

    public Animator animator = Animator.FIRST_INDEX;
    public SpecialRemovalProtocol removalProtocol = SpecialRemovalProtocol.NONE;
    public float r1 = 1, g1 = 1, b1 = 1, r2 = 1, g2 = 1, b2 = 1;
    public float colorCoefficient = 1f;
    public Easing colorCurveEasing = Easing.LINEAR;

    public float scale1 = 1, scale2 = 0, scale3 = 0;
    public float scaleCoefficient = 1f;
    public Easing scaleCurveStartEasing = Easing.LINEAR, scaleCurveEndEasing = Easing.LINEAR;

    public float alpha1 = 1, alpha2 = 0, alpha3 = 0;
    public float alphaCoefficient = 1f;
    public Easing alphaCurveStartEasing = Easing.LINEAR, alphaCurveEndEasing = Easing.LINEAR;

    public boolean forcedMotion = false;
    public MotionStyle motionStyle = MotionStyle.START_TO_END;
    public float motionCoefficient = 1f;
    public Easing motionEasing = Easing.LINEAR;

    public float spin1 = 0, spin2 = 0, spin3 = 0;
    public float spinCoefficient = 1f, spinOffset = 0;
    public Easing spinCurveStartEasing = Easing.LINEAR, spinCurveEndEasing = Easing.LINEAR;

    public int lifetime = 20;
    public float gravity = 0f;
    public boolean noClip = false;

    public SimpleParticleOptions() {
    }
    public boolean isTrinaryScale()
    {
        return scale2 != scale3;
    }
    public boolean isTrinaryAlpha()
    {
        return alpha2 != alpha3;
    }
    public boolean isTrinarySpin()
    {
        return spin2 != spin3;
    }
}