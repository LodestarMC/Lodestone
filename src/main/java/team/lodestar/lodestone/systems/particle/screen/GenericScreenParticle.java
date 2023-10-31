package team.lodestar.lodestone.systems.particle.screen;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.math.Vector3d;
import team.lodestar.lodestone.handlers.screenparticle.ScreenParticleHandler;
import team.lodestar.lodestone.systems.particle.SimpleParticleOptions;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
import team.lodestar.lodestone.systems.particle.options.*;
import team.lodestar.lodestone.systems.particle.render_types.*;
import team.lodestar.lodestone.systems.particle.screen.base.TextureSheetScreenParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;

import java.awt.*;
import java.util.function.Consumer;

import static team.lodestar.lodestone.systems.particle.SimpleParticleOptions.ParticleDiscardFunctionType.ENDING_CURVE_INVISIBLE;
import static team.lodestar.lodestone.systems.particle.SimpleParticleOptions.ParticleDiscardFunctionType.INVISIBLE;
import static team.lodestar.lodestone.systems.particle.SimpleParticleOptions.ParticleSpritePicker.*;
import static team.lodestar.lodestone.systems.particle.SimpleParticleOptions.ParticleSpritePicker.LAST_INDEX;

public class GenericScreenParticle extends TextureSheetScreenParticle {
    private final LodestoneScreenParticleRenderType renderType;
    protected final ParticleEngine.MutableSpriteSet spriteSet;
    protected final SimpleParticleOptions.ParticleSpritePicker spritePicker;
    protected final SimpleParticleOptions.ParticleDiscardFunctionType discardFunctionType;
    protected final ColorParticleData colorData;
    protected final GenericParticleData transparencyData;
    protected final GenericParticleData scaleData;
    protected final SpinParticleData spinData;
    protected final Consumer<GenericScreenParticle> actor;
    private final boolean tracksStack;
    private final double stackTrackXOffset;
    private final double stackTrackYOffset;

    private boolean reachedPositiveAlpha;
    private boolean reachedPositiveScale;

    private int lifeDelay;

    float[] hsv1 = new float[3], hsv2 = new float[3];

    public GenericScreenParticle(ClientLevel world, ScreenParticleOptions options, ParticleEngine.MutableSpriteSet spriteSet, double x, double y, double xMotion, double yMotion) {
        super(world, x, y);
        this.renderType = options.renderType;
        this.spriteSet = spriteSet;
        this.spritePicker = options.spritePicker;
        this.discardFunctionType = options.discardFunctionType;
        this.colorData = options.colorData;
        this.transparencyData = options.transparencyData;
        this.scaleData = options.scaleData;
        this.spinData = options.spinData;
        this.actor = options.actor;
        this.tracksStack = options.tracksStack;
        this.stackTrackXOffset = options.stackTrackXOffset;
        this.stackTrackYOffset = options.stackTrackYOffset;
        this.roll = options.spinData.spinOffset + options.spinData.startingValue;
        this.xMotion = xMotion;
        this.yMotion = yMotion;

        this.setLifetime(options.lifetimeSupplier.get());
        this.lifeDelay = options.lifeDelaySupplier.get();
        this.gravity = options.gravityStrengthSupplier.get();
        this.friction = 1;
        Color.RGBtoHSB((int) (255 * Math.min(1.0f, colorData.r1)), (int) (255 * Math.min(1.0f, colorData.g1)), (int) (255 * Math.min(1.0f, colorData.b1)), hsv1);
        Color.RGBtoHSB((int) (255 * Math.min(1.0f, colorData.r2)), (int) (255 * Math.min(1.0f, colorData.g2)), (int) (255 * Math.min(1.0f, colorData.b2)), hsv2);
        updateTraits();
        if (getSpritePicker().equals(RANDOM_SPRITE)) {
            pickSprite(spriteSet);
        }
        if (getSpritePicker().equals(FIRST_INDEX) || getSpritePicker().equals(WITH_AGE)) {
            pickSprite(0);
        }
        if (getSpritePicker().equals(LAST_INDEX)) {
            pickSprite(spriteSet.sprites.size() - 1);
        }
        updateTraits();
    }

    public SimpleParticleOptions.ParticleSpritePicker getSpritePicker() {
        return spritePicker;
    }

    public void pickSprite(int spriteIndex) {
        if (spriteIndex < spriteSet.sprites.size() && spriteIndex >= 0) {
            setSprite(spriteSet.sprites.get(spriteIndex));
        }
    }

    public void pickColor(float colorCoeff) {
        float h = Mth.rotLerp(colorCoeff, 360f * hsv1[0], 360f * hsv2[0]) / 360f;
        float s = Mth.lerp(colorCoeff, hsv1[1], hsv2[1]);
        float v = Mth.lerp(colorCoeff, hsv1[2], hsv2[2]);
        int packed = Color.HSBtoRGB(h, s, v);
        float r = FastColor.ARGB32.red(packed) / 255.0f;
        float g = FastColor.ARGB32.green(packed) / 255.0f;
        float b = FastColor.ARGB32.blue(packed) / 255.0f;
        setColor(r, g, b);
    }

    public float getCurve(float multiplier) {
        return Mth.clamp((age * multiplier) / (float) lifetime, 0, 1);
    }

    protected void updateTraits() {
        boolean shouldAttemptRemoval = discardFunctionType == INVISIBLE;
        if (discardFunctionType == ENDING_CURVE_INVISIBLE) {

            if (scaleData.getProgress(age, lifetime) > 0.5f || transparencyData.getProgress(age, lifetime) > 0.5f) {
                shouldAttemptRemoval = true;
            }
        }
        if (shouldAttemptRemoval) {
            if ((reachedPositiveAlpha && alpha <= 0) || (reachedPositiveScale && quadSize <= 0)) {
                remove();
                return;
            }
        }

        if (!reachedPositiveAlpha && alpha > 0) {
            reachedPositiveAlpha = true;
        }
        if (!reachedPositiveScale && quadSize > 0) {
            reachedPositiveScale = true;
        }
        pickColor(colorData.colorCurveEasing.ease(colorData.getProgress(age, lifetime), 0, 1, 1));

        quadSize = scaleData.getValue(age, lifetime);
        alpha = transparencyData.getValue(age, lifetime);
        oRoll = roll;
        roll += spinData.getValue(age, lifetime);

        if (actor != null) {
            actor.accept(this);
        }
    }

    @Override
    public void render(BufferBuilder bufferBuilder) {
        if (lifeDelay > 0) {
            return;
        }
        if (tracksStack) {
            x = ScreenParticleHandler.currentItemX+stackTrackXOffset+xMoved;
            y = ScreenParticleHandler.currentItemY+stackTrackYOffset+yMoved;
        }
        super.render(bufferBuilder);
    }

    @Override
    public void tick() {
        if (lifeDelay > 0) {
            lifeDelay--;
            return;
        }
        updateTraits();
        if (getSpritePicker().equals(WITH_AGE)) {
            setSpriteFromAge(spriteSet);
        }
        super.tick();
    }

    @Override
    public LodestoneScreenParticleRenderType getRenderType() {
        return renderType;
    }

    public void setParticleSpeed(Vector3d speed) {
        setParticleSpeed(speed.x, speed.y);
    }
}