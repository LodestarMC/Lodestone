package team.lodestar.lodestone.systems.particle.world;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.fabric.impl.client.particle.FabricSpriteProviderImpl;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.*;
import team.lodestar.lodestone.config.ClientConfig;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.helpers.RenderHelper;
import team.lodestar.lodestone.systems.particle.SimpleParticleOptions;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
import team.lodestar.lodestone.systems.particle.world.options.WorldParticleOptions;
import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType;
import team.lodestar.lodestone.systems.particle.world.behaviors.*;

import java.awt.*;
import java.util.Collection;
import java.util.function.Consumer;

import static team.lodestar.lodestone.systems.particle.SimpleParticleOptions.ParticleDiscardFunctionType.ENDING_CURVE_INVISIBLE;
import static team.lodestar.lodestone.systems.particle.SimpleParticleOptions.ParticleDiscardFunctionType.INVISIBLE;
import static team.lodestar.lodestone.systems.particle.SimpleParticleOptions.ParticleSpritePicker.*;

public class LodestoneWorldParticle extends TextureSheetParticle {
    protected final ParticleRenderType renderType;
    protected final LodestoneParticleBehavior behavior;

    protected final RenderHandler.LodestoneRenderLayer renderLayer;
    protected final boolean shouldCull;
    protected final FabricSpriteProviderImpl spriteSet;
    protected final SimpleParticleOptions.ParticleSpritePicker spritePicker;
    protected final SimpleParticleOptions.ParticleDiscardFunctionType discardFunctionType;
    protected final ColorParticleData colorData;
    protected final GenericParticleData transparencyData;
    protected final GenericParticleData scaleData;
    protected final SpinParticleData spinData;
    protected final Collection<Consumer<LodestoneWorldParticle>> tickActors;
    protected final Collection<Consumer<LodestoneWorldParticle>> renderActors;

    private boolean reachedPositiveAlpha;
    private boolean reachedPositiveScale;

    protected int lifeDelay;

    float[] hsv1 = new float[3], hsv2 = new float[3];

    public LodestoneWorldParticle(ClientLevel world, WorldParticleOptions options, FabricSpriteProviderImpl spriteSet, double x, double y, double z, double xd, double yd, double zd) {
        super(world, x, y, z);
        this.renderType = options.renderType;
        this.behavior = options.behavior;
        this.renderLayer = options.renderLayer;
        this.shouldCull = options.shouldCull;
        this.spriteSet = spriteSet;
        this.spritePicker = options.spritePicker;
        this.discardFunctionType = options.discardFunctionType;
        this.colorData = options.colorData;
        this.transparencyData = GenericParticleData.constrictTransparency(options.transparencyData);
        this.scaleData = options.scaleData;
        this.spinData = options.spinData;
        this.tickActors = options.tickActors;
        this.renderActors = options.renderActors;
        this.roll = options.spinData.spinOffset + options.spinData.startingValue;
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.setLifetime(options.lifetimeSupplier.get());
        this.lifeDelay = options.lifeDelaySupplier.get();
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
                pickSprite(spriteSet.getSprites().size() - 1);
            }
        }
        options.spawnActors.forEach(actor -> actor.accept(this));
        updateTraits();
    }

    public SimpleParticleOptions.ParticleSpritePicker getSpritePicker() {
        return spritePicker;
    }

    public VertexConsumer getVertexConsumer(VertexConsumer original) {
        VertexConsumer consumerToUse = original;
        if (ClientConfig.DELAYED_PARTICLE_RENDERING.getConfigValue() && renderType instanceof LodestoneWorldParticleRenderType lodestoneRenderType) {
            consumerToUse = renderLayer.getParticleTarget().getBuffer(lodestoneRenderType.renderType);
        }
        return consumerToUse;
    }

    public void pickSprite(int spriteIndex) {
        if (spriteIndex < spriteSet.getSprites().size() && spriteIndex >= 0) {
            setSprite(spriteSet.getSprites().get(spriteIndex));
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

        if (!tickActors.isEmpty()) {
            tickActors.forEach(a -> a.accept(this));
        }
    }

    @Override
    public int getLightColor(float pPartialTick) {
        return RenderHelper.FULL_BRIGHT;
    }

    @Override
    public void tick() {
        if (lifeDelay > 0) {
            lifeDelay--;
            return;
        }
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
        if (lifeDelay > 0) {
            return;
        }
        renderActors.forEach(actor -> actor.accept(this));
        if (behavior != null) {
            behavior.render(this, consumer, camera, partialTicks);
            return;
        }
        super.render(getVertexConsumer(consumer), camera, partialTicks);
    }

    @Override
    public boolean shouldCull() {
        return shouldCull;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return renderType;
    }

    @Override
    public float getQuadSize(float pScaleFactor) {
        return super.getQuadSize(pScaleFactor);
    }

    @Override
    public float getU0() {
        return super.getU0();
    }

    @Override
    public float getU1() {
        return super.getU1();
    }

    @Override
    public float getV0() {
        return super.getV0();
    }

    @Override
    public float getV1() {
        return super.getV1();
    }

    public float getRoll() {
        return roll;
    }
    public float getORoll() {
        return oRoll;
    }

    public float getRed() {
        return rCol;
    }

    public float getGreen() {
        return gCol;
    }

    public float getBlue() {
        return bCol;
    }

    public float getAlpha() {
        return alpha;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getXOld() {
        return xo;
    }

    public double getYOld() {
        return yo;
    }

    public double getZOld() {
        return zo;
    }

    public double getXMotion() {
        return xd;
    }

    public double getYMotion() {
        return yd;
    }

    public double getZMotion() {
        return zd;
    }

    public Vec3 getParticleSpeed() {
        return new Vec3(getXMotion(), getYMotion(), getZMotion());
    }

    public int getLifetime() {
        return lifetime;
    }

    public int age() {
        return age;
    }
}