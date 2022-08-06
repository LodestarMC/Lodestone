package team.lodestar.lodestone.systems.rendering.particle.screen;

import team.lodestar.lodestone.systems.rendering.particle.screen.base.TextureSheetScreenParticle;
import team.lodestar.lodestone.handlers.ScreenParticleHandler;
import team.lodestar.lodestone.systems.rendering.particle.SimpleParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;

import java.awt.*;

public class GenericScreenParticle extends TextureSheetScreenParticle {
    public ScreenParticleOptions data;
    private final ParticleRenderType renderType;
    protected final ParticleEngine.MutableSpriteSet spriteSet;
    private final Vec2 startingMotion;
    float[] hsv1 = new float[3], hsv2 = new float[3];

    public GenericScreenParticle(ClientLevel world, ScreenParticleOptions data, ParticleEngine.MutableSpriteSet spriteSet, double x, double y, double xMotion, double yMotion) {
        super(world, x, y);
        this.data = data;
        this.renderType = data.renderType;
        this.spriteSet = spriteSet;
        this.roll = data.spinOffset + data.spin1;
        this.xMotion = xMotion;
        this.yMotion = yMotion;

        this.setRenderOrder(data.renderOrder);
        this.setLifetime(data.lifetime);
        this.gravity = data.gravity;
        this.friction = 1;
        this.startingMotion = data.motionStyle == SimpleParticleOptions.MotionStyle.START_TO_END ? data.startingMotion : new Vec2((float)xMotion, (float)yMotion);
        Color.RGBtoHSB((int) (255 * Math.min(1.0f, data.r1)), (int) (255 * Math.min(1.0f, data.g1)), (int) (255 * Math.min(1.0f, data.b1)), hsv1);
        Color.RGBtoHSB((int) (255 * Math.min(1.0f, data.r2)), (int) (255 * Math.min(1.0f, data.g2)), (int) (255 * Math.min(1.0f, data.b2)), hsv2);
        updateTraits();
        if (getAnimator().equals(SimpleParticleOptions.Animator.RANDOM_SPRITE)) {
            pickSprite(spriteSet);
        }
        if (getAnimator().equals(SimpleParticleOptions.Animator.FIRST_INDEX) || getAnimator().equals(SimpleParticleOptions.Animator.WITH_AGE)) {
            pickSprite(0);
        }
        if (getAnimator().equals(SimpleParticleOptions.Animator.LAST_INDEX)) {
            pickSprite(spriteSet.sprites.size() - 1);
        }
        updateTraits();
    }

    public SimpleParticleOptions.Animator getAnimator() {
        return data.animator;
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
        pickColor(data.colorCurveEasing.ease(getCurve(data.colorCoefficient), 0, 1, 1));
        if (data.isTrinaryScale()) {
            float trinaryAge = getCurve(data.scaleCoefficient);
            if (trinaryAge >= 0.5f) {
                quadSize = Mth.lerp(data.scaleCurveEndEasing.ease(trinaryAge - 0.5f, 0, 1, 0.5f), data.scale2, data.scale3);
            } else {
                quadSize = Mth.lerp(data.scaleCurveStartEasing.ease(trinaryAge, 0, 1, 0.5f), data.scale1, data.scale2);
            }
        } else {
            quadSize = Mth.lerp(data.scaleCurveStartEasing.ease(getCurve(data.scaleCoefficient), 0, 1, 1), data.scale1, data.scale2);
        }
        if (data.isTrinaryAlpha()) {
            float trinaryAge = getCurve(data.alphaCoefficient);
            if (trinaryAge >= 0.5f) {
                alpha = Mth.lerp(data.alphaCurveEndEasing.ease(trinaryAge - 0.5f, 0, 1, 0.5f), data.alpha2, data.alpha3);
            } else {
                alpha = Mth.lerp(data.alphaCurveStartEasing.ease(trinaryAge, 0, 1, 0.5f), data.alpha1, data.alpha2);
            }
        } else {
            alpha = Mth.lerp(data.alphaCurveStartEasing.ease(getCurve(data.alphaCoefficient), 0, 1, 1), data.alpha1, data.alpha2);
        }
        oRoll = roll;

        if (data.isTrinarySpin()) {
            float trinaryAge = getCurve(data.spinCoefficient);
            if (trinaryAge >= 0.5f) {
                roll += Mth.lerp(data.spinCurveEndEasing.ease(trinaryAge - 0.5f, 0, 1, 0.5f), data.spin2, data.spin3);
            } else {
                roll += Mth.lerp(data.spinCurveStartEasing.ease(trinaryAge, 0, 1, 0.5f), data.spin1, data.spin2);
            }
        } else {
            roll += Mth.lerp(data.spinCurveStartEasing.ease(getCurve(data.alphaCoefficient), 0, 1, 1), data.spin1, data.spin2);
        }
        if (data.forcedMotion) {
            float motionAge = getCurve(data.motionCoefficient);
            Vec2 currentMotion = data.motionStyle == SimpleParticleOptions.MotionStyle.START_TO_END ? startingMotion : new Vec2((float) xMotion, (float) yMotion);
            xMotion = Mth.lerp(data.motionEasing.ease(motionAge, 0, 1, 1), currentMotion.x, data.endingMotion.x);
            yMotion = Mth.lerp(data.motionEasing.ease(motionAge, 0, 1, 1), currentMotion.y, data.endingMotion.y);
        } else {
            xMotion *= data.motionCoefficient;
            yMotion *= data.motionCoefficient;
        }
    }

    @Override
    public void tick() {
        updateTraits();
        if (data.animator.equals(SimpleParticleOptions.Animator.WITH_AGE)) {
            setSpriteFromAge(spriteSet);
        }
        super.tick();
    }

    public void trackStack() {
        for (ScreenParticleHandler.StackTracker renderedStack : ScreenParticleHandler.RENDERED_STACKS) {
            //&& renderedStack.xOrigin() == data.xOrigin && renderedStack.yOrigin() == data.yOrigin
            if (renderedStack.stack().equals(data.stack) && renderedStack.order().equals(data.renderOrder)) {
                x = renderedStack.xOrigin() + data.xOffset + xMoved;
                y = renderedStack.yOrigin() + data.yOffset + yMoved;
                break;
            }
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return renderType;
    }
}