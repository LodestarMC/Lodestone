package team.lodestar.lodestone.systems.particle;

import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.data.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.SpinParticleData;

public class SimpleParticleOptions {

    public static final ColorParticleData DEFAULT_COLOR = new ColorParticleData(1, 1, 1, 0, 0, 0);
    public static final SpinParticleData DEFAULT_SPIN = new SpinParticleData(0, 1, 0);
    public static final GenericParticleData DEFAULT_GENERIC = new GenericParticleData(1, 0);

    public enum ParticleSpritePicker {
        FIRST_INDEX, LAST_INDEX, WITH_AGE, RANDOM_SPRITE
    }

    public enum ParticleDiscardType {
        NONE, INVISIBLE, ENDING_CURVE_INVISIBLE
    }

    public ParticleSpritePicker spritePicker = ParticleSpritePicker.FIRST_INDEX;
    public ParticleDiscardType removalProtocol = ParticleDiscardType.NONE;

    public ColorParticleData colorData = DEFAULT_COLOR;
    public GenericParticleData transparencyData = DEFAULT_GENERIC;
    public GenericParticleData scaleData = DEFAULT_GENERIC;
    public SpinParticleData spinData = DEFAULT_SPIN;

    public int lifetime = 20;
    public float gravity = 0f;

}