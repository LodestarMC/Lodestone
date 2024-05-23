package team.lodestar.lodestone.systems.particle.world;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.fabric.impl.client.particle.FabricSpriteProviderImpl;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import team.lodestar.lodestone.config.ClientConfig;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.helpers.RenderHelper;
import team.lodestar.lodestone.systems.particle.LodestoneWorldParticleActor;
import team.lodestar.lodestone.systems.particle.SimpleParticleOptions;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
import team.lodestar.lodestone.systems.particle.options.AbstractWorldParticleOptions;
import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType;

import java.awt.*;
import java.util.Collection;
import java.util.function.Consumer;

import static team.lodestar.lodestone.systems.particle.SimpleParticleOptions.ParticleDiscardFunctionType.ENDING_CURVE_INVISIBLE;
import static team.lodestar.lodestone.systems.particle.SimpleParticleOptions.ParticleDiscardFunctionType.INVISIBLE;
import static team.lodestar.lodestone.systems.particle.SimpleParticleOptions.ParticleSpritePicker.*;

public class GenericParticle<T extends AbstractWorldParticleOptions> extends TextureSheetParticle implements LodestoneWorldParticleActor {
    protected final ParticleRenderType renderType;
    protected final boolean shouldCull;
    protected final FabricSpriteProviderImpl spriteSet;
    protected final SimpleParticleOptions.ParticleSpritePicker spritePicker;
    protected final SimpleParticleOptions.ParticleDiscardFunctionType discardFunctionType;
    protected final ColorParticleData colorData;
    protected final GenericParticleData transparencyData;
    protected final GenericParticleData scaleData;
    protected final SpinParticleData spinData;
    protected final Collection<Consumer<LodestoneWorldParticleActor>> tickActors;
    protected final Collection<Consumer<LodestoneWorldParticleActor>> renderActors;

    private boolean reachedPositiveAlpha;
    private boolean reachedPositiveScale;

    protected int lifeDelay;

    float[] hsv1 = new float[3], hsv2 = new float[3];

    public GenericParticle(ClientLevel world, T options, FabricSpriteProviderImpl spriteSet, double x, double y, double z, double xd, double yd, double zd) {
        super(world, x, y, z);
        this.renderType = options.renderType == null ? LodestoneWorldParticleRenderType.ADDITIVE : options.renderType;
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
            consumerToUse = RenderHandler.DELAYED_PARTICLE_RENDER.getBuffer(lodestoneRenderType.renderType);
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
    protected int getLightColor(float pPartialTick) {
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
    public Vec3 getParticlePosition() {
        return new Vec3(x, y, z);
    }

    @Override
    public LodestoneWorldParticleActor setParticlePosition(double x, double y, double z) {
        setPos(x, y, z);
        return this;
    }

    @Override
    public Vec3 getParticleSpeed() {
        return new Vec3(xd, yd, zd);
    }

    @Override
    public LodestoneWorldParticleActor setParticleMotion(double x, double y, double z) {
        setParticleSpeed(x, y, z);
        return this;
    }

    @Override
    public int getParticleAge() {
        return age;
    }

    @Override
    public LodestoneWorldParticleActor setParticleAge(int age) {
        this.age = age;
        return this;
    }

    @Override
    public int getParticleLifespan() {
        return lifetime;
    }

    @Override
    public LodestoneWorldParticleActor tickParticle(int times) {
        for (int i = 0; i < times; i++) {
            tick();
        }
        return this;
    }
}