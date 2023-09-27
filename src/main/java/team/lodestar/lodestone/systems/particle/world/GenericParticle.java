package team.lodestar.lodestone.systems.particle.world;

import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.config.ClientConfig;
import team.lodestar.lodestone.helpers.RenderHelper;
import com.mojang.blaze3d.vertex.VertexConsumer;
import team.lodestar.lodestone.handlers.RenderHandler;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import team.lodestar.lodestone.systems.particle.SimpleParticleOptions;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
import team.lodestar.lodestone.systems.particle.options.*;
import team.lodestar.lodestone.systems.particle.render_types.*;

import java.awt.*;
import java.util.function.Consumer;

import static team.lodestar.lodestone.systems.particle.SimpleParticleOptions.ParticleDiscardFunctionType.ENDING_CURVE_INVISIBLE;
import static team.lodestar.lodestone.systems.particle.SimpleParticleOptions.ParticleDiscardFunctionType.INVISIBLE;
import static team.lodestar.lodestone.systems.particle.SimpleParticleOptions.ParticleSpritePicker.*;

public class GenericParticle extends TextureSheetParticle {
    private final ParticleRenderType renderType;
    protected final ParticleEngine.MutableSpriteSet spriteSet;
    protected final SimpleParticleOptions.ParticleSpritePicker spritePicker;
    protected final SimpleParticleOptions.ParticleDiscardFunctionType discardFunctionType;
    protected final ColorParticleData colorData;
    protected final GenericParticleData transparencyData;
    protected final GenericParticleData scaleData;
    protected final SpinParticleData spinData;
    protected final Consumer<GenericParticle> actor;

    private boolean reachedPositiveAlpha;
    private boolean reachedPositiveScale;

    float[] hsv1 = new float[3], hsv2 = new float[3];

    public GenericParticle(ClientLevel world, WorldParticleOptions options, ParticleEngine.MutableSpriteSet spriteSet, double x, double y, double z, double xd, double yd, double zd) {
        super(world, x, y, z);
        this.renderType = options.renderType == null ? LodestoneWorldParticleRenderType.ADDITIVE : options.renderType;
        this.spriteSet = spriteSet;

        this.spritePicker = options.spritePicker;
        this.discardFunctionType = options.discardFunctionType;
        this.colorData = options.colorData;
        this.transparencyData = options.transparencyData;
        this.scaleData = options.scaleData;
        this.spinData = options.spinData;
        this.actor = options.actor;
        this.roll = options.spinData.spinOffset + options.spinData.startingValue;
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.setLifetime(options.lifetimeSupplier.get());
        this.gravity = options.gravityStrengthSupplier.get();
        this.hasPhysics = !options.noClip;
        this.friction = 1;
        Color.RGBtoHSB((int) (255 * Math.min(1.0f, colorData.r1)), (int) (255 * Math.min(1.0f, colorData.g1)), (int) (255 * Math.min(1.0f, colorData.b1)), hsv1);
        Color.RGBtoHSB((int) (255 * Math.min(1.0f, colorData.r2)), (int) (255 * Math.min(1.0f, colorData.g2)), (int) (255 * Math.min(1.0f, colorData.b2)), hsv2);

        if (spriteSet != null) {
            if (getSpritePicker().equals(RANDOM_SPRITE)) {
                pickSprite(spriteSet);
            }
            if (getSpritePicker().equals(FIRST_INDEX) || getSpritePicker().equals(WITH_AGE)) {
                pickSprite(0);
            }
            if (getSpritePicker().equals(LAST_INDEX)) {
                pickSprite(spriteSet.sprites.size() - 1);
            }
        }
        updateTraits();
    }

    @Override
    protected int getLightColor(float pPartialTick) {
        return RenderHelper.FULL_BRIGHT;
    }

    @Override
    public void tick() {
        updateTraits();
        if (spriteSet != null) {
            if (getSpritePicker().equals(WITH_AGE)) {
                setSpriteFromAge(spriteSet);
            }
        }
        super.tick();
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        super.render(getVertexConsumer(consumer), camera, partialTicks);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return renderType;
    }

    public SimpleParticleOptions.ParticleSpritePicker getSpritePicker() {
        return spritePicker;
    }

    public VertexConsumer getVertexConsumer(VertexConsumer original) {
        VertexConsumer consumerToUse = original;
        if (ClientConfig.DELAYED_PARTICLE_RENDERING.getConfigValue() && renderType instanceof LodestoneWorldParticleRenderType renderType) {
            if (renderType.shouldBuffer()) {
                consumerToUse = RenderHandler.DELAYED_PARTICLE_RENDER.getBuffer(renderType.getRenderType());
            }
        }
        return consumerToUse;
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

    public Vec3 getPos() {
        return new Vec3(x, y, z);
    }

    public Vec3 getParticleSpeed() {
        return new Vec3(xd, yd, zd);
    }

    public void setParticleSpeed(Vec3 speed) {
        setParticleSpeed(speed.x, speed.y, speed.z);
    }
}