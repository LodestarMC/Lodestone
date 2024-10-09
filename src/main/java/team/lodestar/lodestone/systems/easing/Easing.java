package team.lodestar.lodestone.systems.easing;

import com.mojang.serialization.Codec;
import net.minecraft.util.Mth;

import java.util.HashMap;

/**
 * <p>The Easing class holds a set of general-purpose motion
 * tweening functions by Robert Penner. This class is
 * essentially a port from Penner's ActionScript utility,
 * with a few added tweaks.
 * <p>Examples:<pre>
 *    //no tween
 *    Easing e1 = Easing.LINEAR;
 *
 *    //backOut tween, the overshoot is Easing.Back.DEFAULT_OVERSHOOT
 *    Easing e2 = Easing.BACK_OUT;
 *
 *    //backOut tween, the overshoot is 1.85f
 *    Easing.Back e3 = new Easing.BackOut(1.85f);
 * </pre>
 * <a href="http://www.robertpenner.com/easing/">Robert Penner's Easing Functions</a>
 *
 * @author Robert Penner (functions)
 * @author davedes (java port)
 */
public abstract class Easing {

    public static final Codec<Easing> CODEC = Codec.STRING.xmap(Easing::valueOf, e -> e.name);

    public static final HashMap<String, Easing> EASINGS = new HashMap<>();
    public final String name;

    public Easing(String name) {
        this.name = name;
        EASINGS.put(name, this);
    }

    public static Easing valueOf(String name) {
        return EASINGS.get(name);
    }

    /**
     * The basic function for easing.
     *
     * @param value the time (either frames or in seconds/milliseconds)
     * @param min the beginning value
     * @param max the value changed
     * @param time the duration time
     * @return the eased value
     */
    public abstract float ease(float value, float min, float max, float time);

    public float ease(double value, double min, double max, double time) {
        return ease((float)value, (float)min, (float)max, (float)time);
    }

    public float ease(float value, float min, float max) {
        return ease(value, min, max, 1);
    }

    public float ease(double value, double min, double max) {
        return ease(value, min, max, 1);
    }

    public float clamped(float value, float min, float max, float time) {
        return ease(Mth.clamp(value, 0, time), min, max, time);
    }

    public float clamped(double value, double min, double max, double time) {
        return clamped((float)value, (float)min, (float)max, (float)time);
    }

    public float clamped(float value, float min, float max) {
        return clamped(value, min, max, 1);
    }

    public float clamped(double value, double min, double max) {
        return clamped(value, min, max, 1);
    }

    /**
     * Simple linear tweening - no easing.
     */
    public static final Easing LINEAR = new Easing("linear") {
        public float ease(float value, float min, float max, float time) {
            return max * value / time + min;
        }
    };

    ///////////// QUADRATIC EASING: t^2 ///////////////////

    /**
     * Quadratic easing in - accelerating from zero velocity.
     */
    public static final Easing QUAD_IN = new Easing("quadIn") {
        public float ease(float value, float min, float max, float time) {
            return max * (value /= time) * value + min;
        }
    };


    /**
     * Quadratic easing out - decelerating to zero velocity.
     */
    public static final Easing QUAD_OUT = new Easing("quadOut") {
        public float ease(float value, float min, float max, float time) {
            return -max * (value /= time) * (value - 2) + min;
        }
    };

    /**
     * Quadratic easing in/out - acceleration until halfway, then deceleration
     */
    public static final Easing QUAD_IN_OUT = new Easing("quadInOut") {
        public float ease(float value, float min, float max, float time) {
            if ((value /= time / 2) < 1) return max / 2 * value * value + min;
            return -max / 2 * ((--value) * (value - 2) - 1) + min;
        }
    };


    ///////////// CUBIC EASING: t^3 ///////////////////////

    /**
     * Cubic easing in - accelerating from zero velocity.
     */
    public static final Easing CUBIC_IN = new Easing("cubicIn") {
        public float ease(float value, float min, float max, float time) {
            return max * (value /= time) * value * value + min;
        }
    };

    /**
     * Cubic easing out - decelerating to zero velocity.
     */
    public static final Easing CUBIC_OUT = new Easing("cubicOut") {
        public float ease(float value, float min, float max, float time) {
            return max * ((value = value / time - 1) * value * value + 1) + min;
        }
    };

    /**
     * Cubic easing in/out - acceleration until halfway, then deceleration.
     */
    public static final Easing CUBIC_IN_OUT = new Easing("cubicInOut") {
        public float ease(float value, float min, float max, float time) {
            if ((value /= time / 2) < 1) return max / 2 * value * value * value + min;
            return max / 2 * ((value -= 2) * value * value + 2) + min;
        }
    };

    ///////////// QUARTIC EASING: t^4 /////////////////////

    /**
     * Quartic easing in - accelerating from zero velocity.
     */
    public static final Easing QUARTIC_IN = new Easing("quarticIn") {
        public float ease(float value, float min, float max, float time) {
            return max * (value /= time) * value * value * value + min;
        }
    };

    /**
     * Quartic easing out - decelerating to zero velocity.
     */
    public static final Easing QUARTIC_OUT = new Easing("quarticOut") {
        public float ease(float value, float min, float max, float time) {
            return -max * ((value = value / time - 1) * value * value * value - 1) + min;
        }
    };

    /**
     * Quartic easing in/out - acceleration until halfway, then deceleration.
     */
    public static final Easing QUARTIC_IN_OUT = new Easing("quarticInOut") {
        public float ease(float value, float min, float max, float time) {
            if ((value /= time / 2) < 1) return max / 2 * value * value * value * value + min;
            return -max / 2 * ((value -= 2) * value * value * value - 2) + min;
        }
    };

    ///////////// QUINTIC EASING: t^5  ////////////////////

    /**
     * Quintic easing in - accelerating from zero velocity.
     */
    public static final Easing QUINTIC_IN = new Easing("quinticIn") {
        public float ease(float value, float min, float max, float time) {
            return max * (value /= time) * value * value * value * value + min;
        }
    };

    /**
     * Quintic easing out - decelerating to zero velocity.
     */
    public static final Easing QUINTIC_OUT = new Easing("quinticOut") {
        public float ease(float value, float min, float max, float time) {
            return max * ((value = value / time - 1) * value * value * value * value + 1) + min;
        }
    };

    /**
     * Quintic easing in/out - acceleration until halfway, then deceleration.
     */
    public static final Easing QUINTIC_IN_OUT = new Easing("quinticInOut") {
        public float ease(float value, float min, float max, float time) {
            if ((value /= time / 2) < 1) return max / 2 * value * value * value * value * value + min;
            return max / 2 * ((value -= 2) * value * value * value * value + 2) + min;
        }
    };


    ///////////// SINUSOIDAL EASING: sin(t) ///////////////

    /**
     * Sinusoidal easing in - accelerating from zero velocity.
     */
    public static final Easing SINE_IN = new Easing("sineIn") {
        public float ease(float value, float min, float max, float time) {
            return -max * (float) Math.cos(value / time * (Math.PI / 2)) + max + min;
        }
    };

    /**
     * Sinusoidal easing out - decelerating to zero velocity.
     */
    public static final Easing SINE_OUT = new Easing("sineOut") {
        public float ease(float value, float min, float max, float time) {
            return max * (float) Math.sin(value / time * (Math.PI / 2)) + min;
        }
    };

    /**
     * Sinusoidal easing in/out - accelerating until halfway, then decelerating.
     */
    public static final Easing SINE_IN_OUT = new Easing("sineInOut") {
        public float ease(float value, float min, float max, float time) {
            return -max / 2 * ((float) Math.cos(Math.PI * value / time) - 1) + min;
        }
    };

    ///////////// EXPONENTIAL EASING: 2^t /////////////////

    /**
     * Exponential easing in - accelerating from zero velocity.
     */
    public static final Easing EXPO_IN = new Easing("expoIn") {
        public float ease(float value, float min, float max, float time) {
            return (value == 0) ? min : max * (float) Math.pow(2, 10 * (value / time - 1)) + min;
        }
    };

    /**
     * Exponential easing out - decelerating to zero velocity.
     */
    public static final Easing EXPO_OUT = new Easing("expoOut") {
        public float ease(float value, float min, float max, float time) {
            return (value == time) ? min + max : max * (-(float) Math.pow(2, -10 * value / time) + 1) + min;
        }
    };

    /**
     * Exponential easing in/out - accelerating until halfway, then decelerating.
     */
    public static final Easing EXPO_IN_OUT = new Easing("expoInOut") {
        public float ease(float value, float min, float max, float time) {
            if (value == 0) return min;
            if (value == time) return min + max;
            if ((value /= time / 2) < 1) return max / 2 * (float) Math.pow(2, 10 * (value - 1)) + min;
            return max / 2 * (-(float) Math.pow(2, -10 * --value) + 2) + min;
        }
    };


    /////////// CIRCULAR EASING: sqrt(1-t^2) //////////////

    /**
     * Circular easing in - accelerating from zero velocity.
     */
    public static final Easing CIRC_IN = new Easing("circIn") {
        public float ease(float value, float min, float max, float time) {
            return -max * ((float) Math.sqrt(1 - (value /= time) * value) - 1) + min;
        }
    };

    /**
     * Circular easing out - decelerating to zero velocity.
     */
    public static final Easing CIRC_OUT = new Easing("circOut") {
        public float ease(float value, float min, float max, float time) {
            return max * (float) Math.sqrt(1 - (value = value / time - 1) * value) + min;
        }
    };

    /**
     * Circular easing in/out - acceleration until halfway, then deceleration.
     */
    public static final Easing CIRC_IN_OUT = new Easing("circInOut") {
        public float ease(float value, float min, float max, float time) {
            if ((value /= time / 2) < 1) return -max / 2 * ((float) Math.sqrt(1 - value * value) - 1) + min;
            return max / 2 * ((float) Math.sqrt(1 - (value -= 2) * value) + 1) + min;
        }
    };

    /////////// ELASTIC EASING: exponentially decaying sine wave  //////////////

    /**
     * A base class for elastic easings.
     */
    public static abstract class Elastic extends Easing {
        private float amplitude;
        private float period;

        /**
         * Creates a new Elastic easing with the specified settings.
         *
         * @param amplitude the amplitude for the elastic function
         * @param period    the period for the elastic function
         */
        public Elastic(String name, float amplitude, float period) {
            super(name);
            this.amplitude = amplitude;
            this.period = period;
        }

        /**
         * Creates a new Elastic easing with default settings (-1f, 0f).
         */
        public Elastic(String name) {
            this(name, -1f, 0f);
        }

        /**
         * Returns the period.
         *
         * @return the period for this easing
         */
        public float getPeriod() {
            return period;
        }

        /**
         * Sets the period to the given value.
         *
         * @param period the new period
         */
        public void setPeriod(float period) {
            this.period = period;
        }

        /**
         * Returns the amplitude.
         *
         * @return the amplitude for this easing
         */
        public float getAmplitude() {
            return amplitude;
        }

        /**
         * Sets the amplitude to the given value.
         *
         * @param amplitude the new amplitude
         */
        public void setAmplitude(float amplitude) {
            this.amplitude = amplitude;
        }
    }

    /**
     * An EasingIn instance using the default values.
     */
    public static final Elastic ELASTIC_IN = new ElasticIn();

    /**
     * An Elastic easing used for ElasticIn functions.
     */
    public static class ElasticIn extends Elastic {
        public ElasticIn(float amplitude, float period) {
            super("elasticIn", amplitude, period);
        }

        public ElasticIn() {
            super("elasticIn");
        }

        public float ease(float value, float min, float max, float time) {
            float a = getAmplitude();
            float p = getPeriod();
            if (value == 0) return min;
            if ((value /= time) == 1) return min + max;
            if (p == 0) p = time * .3f;
            float s = 0;
            if (a < Math.abs(max)) {
                a = max;
                s = p / 4;
            } else s = p / (float) (2 * Math.PI) * (float) Math.asin(max / a);
            return -(a * (float) Math.pow(2, 10 * (value -= 1)) * (float) Math.sin((value * time - s) * (2 * Math.PI) / p)) + min;
        }
    }

    /**
     * An ElasticOut instance using the default values.
     */
    public static final Elastic ELASTIC_OUT = new ElasticOut();

    /**
     * An Elastic easing used for ElasticOut functions.
     */
    public static class ElasticOut extends Elastic {
        public ElasticOut(float amplitude, float period) {
            super("elasticOut", amplitude, period);
        }

        public ElasticOut() {
            super("elasticOut");
        }

        public float ease(float value, float min, float max, float time) {
            float a = getAmplitude();
            float p = getPeriod();
            if (value == 0) return min;
            if ((value /= time) == 1) return min + max;
            if (p == 0) p = time * .3f;
            float s = 0;
            if (a < Math.abs(max)) {
                a = max;
                s = p / 4;
            } else s = p / (float) (2 * Math.PI) * (float) Math.asin(max / a);
            return a * (float) Math.pow(2, -10 * value) * (float) Math.sin((value * time - s) * (2 * Math.PI) / p) + max + min;
        }
    }

    /**
     * An ElasticInOut instance using the default values.
     */
    public static final Elastic ELASTIC_IN_OUT = new ElasticInOut();

    /**
     * An Elastic easing used for ElasticInOut functions.
     */
    public static class ElasticInOut extends Elastic {
        public ElasticInOut(float amplitude, float period) {
            super("elasticInOut", amplitude, period);
        }

        public ElasticInOut() {
            super("elasticInOut");
        }

        public float ease(float value, float min, float max, float time) {
            float a = getAmplitude();
            float p = getPeriod();
            if (value == 0) return min;
            if ((value /= time / 2) == 2) return min + max;
            if (p == 0) p = time * (.3f * 1.5f);
            float s = 0;
            if (a < Math.abs(max)) {
                a = max;
                s = p / 4f;
            } else s = p / (float) (2 * Math.PI) * (float) Math.asin(max / a);
            if (value < 1)
                return -.5f * (a * (float) Math.pow(2, 10 * (value -= 1)) * (float) Math.sin((value * time - s) * (2 * Math.PI) / p)) + min;
            return a * (float) Math.pow(2, -10 * (value -= 1)) * (float) Math.sin((value * time - s) * (2 * Math.PI) / p) * .5f + max + min;
        }
    }

    /////////// BACK EASING: overshooting cubic easing: (s+1)*t^3 - s*t^2  //////////////

    /**
     * A base class for Back easings.
     */
    public static abstract class Back extends Easing {
        /**
         * The default overshoot is 10% (1.70158).
         */
        public static final float DEFAULT_OVERSHOOT = 1.70158f;

        private float overshoot;

        /**
         * Creates a new Back instance with the default overshoot (1.70158).
         */
        public Back(String name) {
            this(name, DEFAULT_OVERSHOOT);
        }

        /**
         * Creates a new Back instance with the specified overshoot.
         *
         * @param overshoot the amount to overshoot by -- higher number
         *                  means more overshoot and an overshoot of 0 results in
         *                  cubic easing with no overshoot
         */
        public Back(String name, float overshoot) {
            super(name);
            this.overshoot = overshoot;
        }

        /**
         * Sets the overshoot to the given value.
         *
         * @param overshoot the new overshoot
         */
        public void setOvershoot(float overshoot) {
            this.overshoot = overshoot;
        }

        /**
         * Returns the overshoot for this easing.
         *
         * @return this easing's overshoot
         */
        public float getOvershoot() {
            return overshoot;
        }
    }

    /**
     * An instance of BackIn using the default overshoot.
     */
    public static final Back BACK_IN = new BackIn();

    /**
     * Back easing in - backtracking slightly, then reversing direction and moving to target.
     */
    public static class BackIn extends Back {
        public BackIn() {
            super("backIn");
        }

        public BackIn(float overshoot) {
            super("backIn", overshoot);
        }

        public float ease(float value, float min, float max, float time) {
            float s = getOvershoot();
            return max * (value /= time) * value * ((s + 1) * value - s) + min;
        }
    }

    /**
     * An instance of BackOut using the default overshoot.
     */
    public static final Back BACK_OUT = new BackOut();

    /**
     * Back easing out - moving towards target, overshooting it slightly, then reversing and coming back to target.
     */
    public static class BackOut extends Back {
        public BackOut() {
            super("backOut");
        }

        public BackOut(float overshoot) {
            super("backOut", overshoot);
        }

        public float ease(float value, float min, float max, float time) {
            float s = getOvershoot();
            return max * ((value = value / time - 1) * value * ((s + 1) * value + s) + 1) + min;
        }
    }

    /**
     * An instance of BackInOut using the default overshoot.
     */
    public static final Back BACK_IN_OUT = new BackInOut();

    /**
     * Back easing in/out - backtracking slightly, then reversing direction and moving to target,
     * then overshooting target, reversing, and finally coming back to target.
     */
    public static class BackInOut extends Back {
        public BackInOut() {
            super("backInOut");
        }

        public BackInOut(float overshoot) {
            super("backInOut", overshoot);
        }

        public float ease(float value, float min, float max, float time) {
            float s = getOvershoot();
            if ((value /= time / 2) < 1) return max / 2 * (value * value * (((s *= (1.525)) + 1) * value - s)) + min;
            return max / 2 * ((value -= 2) * value * (((s *= (1.525)) + 1) * value + s) + 2) + min;
        }
    }

    /////////// BOUNCE EASING: exponentially decaying parabolic bounce  //////////////

    /**
     * Bounce easing in.
     */
    public static final Easing BOUNCE_IN = new Easing("bounceIn") {
        public float ease(float value, float min, float max, float time) {
            return max - Easing.BOUNCE_OUT.ease(time - value, 0, max, time) + min;
        }
    };

    /**
     * Bounce easing out.
     */
    public static final Easing BOUNCE_OUT = new Easing("bounceOut") {
        public float ease(float value, float min, float max, float time) {
            if ((value /= time) < (1 / 2.75f)) {
                return max * (7.5625f * value * value) + min;
            } else if (value < (2 / 2.75f)) {
                return max * (7.5625f * (value -= (1.5f / 2.75f)) * value + .75f) + min;
            } else if (value < (2.5f / 2.75f)) {
                return max * (7.5625f * (value -= (2.25f / 2.75f)) * value + .9375f) + min;
            } else {
                return max * (7.5625f * (value -= (2.625f / 2.75f)) * value + .984375f) + min;
            }
        }
    };

    /**
     * Bounce easing in/out.
     */
    public static final Easing BOUNCE_IN_OUT = new Easing("bounceInOut") {
        public float ease(float value, float min, float max, float time) {
            if (value < time / 2) return Easing.BOUNCE_IN.ease(value * 2, 0, max, time) * .5f + min;
            return Easing.BOUNCE_OUT.ease(value * 2 - time, 0, max, time) * .5f + max * .5f + min;
        }
    };
}