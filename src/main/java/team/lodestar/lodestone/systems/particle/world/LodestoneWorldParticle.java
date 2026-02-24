package team.lodestar.lodestone.systems.particle.world;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.*;
import net.minecraft.world.phys.*;
import org.joml.Vector3f;
import team.lodestar.lodestone.systems.particle.*;
import team.lodestar.lodestone.systems.particle.world.behaviors.*;
import team.lodestar.lodestone.common.config.ClientConfig;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
import team.lodestar.lodestone.systems.particle.world.options.WorldParticleOptions;
import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType;
import team.lodestar.lodestone.systems.rendering.buffer.LodestoneRenderLayer;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Collection;
import java.util.function.Consumer;

public class LodestoneWorldParticle extends TextureSheetParticle implements IParticle {

    public final LodestoneParticleBehavior behavior;
    public final ParticleEngine.MutableSpriteSet spriteSet;
    public final SimpleParticleOptions.ParticleSpritePicker spritePicker;

    public final ParticleRenderType renderType;
    public final LodestoneRenderLayer renderLayer;

    public final ColorParticleData colorData;
    public final GenericParticleData transparencyData;
    public final GenericParticleData scaleData;
    @Nullable
    public final GenericParticleData lengthData;
    public final SpinParticleData spinData;

    public final Collection<Consumer<LodestoneWorldParticle>> spawnActors;
    public final Collection<Consumer<LodestoneWorldParticle>> tickActors;
    public final Collection<Consumer<LodestoneWorldParticle>> renderActors;

    public final int particleLight;

    public Vector3f oldTravelledDistance = new Vector3f();
    public Vector3f travelledDistance = new Vector3f();

    public float partialTicksCache;

    public int lifeDelay;
    private float quadLength;

    float[] hsv1 = new float[3], hsv2 = new float[3];

    public LodestoneWorldParticle(ClientLevel world, WorldParticleOptions options, ParticleEngine.MutableSpriteSet spriteSet, double x, double y, double z, double xd, double yd, double zd) {
        super(world, x, y, z);
        this.behavior = options.behavior;
        this.spriteSet = spriteSet;
        this.spritePicker = options.spritePicker;
        this.renderType = options.renderType;
        this.renderLayer = options.renderLayer;
        this.colorData = options.colorData;
        this.transparencyData = options.transparencyData;
        this.scaleData = options.scaleData;
        this.lengthData = options.lengthData != WorldParticleOptions.DEFAULT_GENERIC ? options.lengthData : null;
        this.spinData = options.spinData;
        this.spawnActors = options.spawnActors;
        this.tickActors = options.tickActors;
        this.renderActors = options.renderActors;
        this.particleLight = options.particleLight;
        this.roll = options.spinData.spinOffset + options.spinData.startingValue;
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.setLifetime(options.getLifetime());
        this.lifeDelay = options.getLifeDelay();
        this.gravity = options.getGravity();
        this.friction = options.getFriction();
        this.hasPhysics = !options.noClip;

        colorData.rgbToHsv(hsv1, hsv2);
        if (spriteSet != null) {
            switch (spritePicker) {
                case FIRST_INDEX, WITH_AGE -> pickSprite(0);
                case LAST_INDEX, WITH_AGE_INVERSE -> pickSprite(spriteSet.sprites.size() - 1);
                case RANDOM_SPRITE -> pickSprite(random.nextInt(spriteSet.sprites.size()));
            }
        }
        if (lifeDelay == 0) {
            options.spawnActors.forEach(actor -> actor.accept(this));
        }
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
        setSprite(spriteSet.sprites.get(Mth.clamp(spriteIndex, 0, spriteSet.sprites.size() - 1)));
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
        if (scaleData.getProgress(age, lifetime) > 0.8f || transparencyData.getProgress(age, lifetime) > 0.8f) {
            if (alpha <= 0 || getQuadSize(0) <= 0 || getQuadLength(0) <= 0) {
                remove();
                return;
            }
        }
        pickColor(colorData.getColorCurve().clamped(colorData.getProgress(age, lifetime), 0, 1));

        quadSize = scaleData.getValue(age, lifetime);
        quadLength = lengthData != null ? lengthData.getValue(age, lifetime) : quadSize;

        alpha = Mth.clamp(transparencyData.getValue(age, lifetime), 0, 1);
        oRoll = roll;
        roll += spinData.getValue(age, lifetime);

        if (!tickActors.isEmpty()) {
            tickActors.forEach(a -> a.accept(this));
        }
        if (behavior != null) {
            behavior.tick(this);
        }
    }

    @Override
    public int getLightColor(float pPartialTick) {
        if (particleLight == -1) {
            return super.getLightColor(pPartialTick);
        }
        return particleLight;
    }

    @Override
    public void tick() {
        if (lifeDelay > 0) {
            lifeDelay--;
            spawnActors.forEach(actor -> actor.accept(this));
            xo = x;
            yo = y;
            zo = z;
            return;
        }
        updateTraits();
        super.tick();
        if (spriteSet != null) {
            switch (spritePicker) {
                case WITH_AGE -> setSpriteFromAge(spriteSet);
                case WITH_AGE_INVERSE -> setSpriteFromInverseAge(spriteSet);
            }
        }
    }

    @Override
    public void move(double x, double y, double z) {
        oldTravelledDistance = new Vector3f(travelledDistance);
        travelledDistance.x += (float) x;
        travelledDistance.y += (float) y;
        travelledDistance.z += (float) z;
        super.move(x, y, z);
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        partialTicksCache = partialTicks;
        if (lifeDelay > 0) {
            return;
        }
        renderActors.forEach(actor -> actor.accept(this));
        if (behavior != null) {
            behavior.render(this, getVertexConsumer(consumer), camera, partialTicks);
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return renderType;
    }

    public void setSpriteFromInverseAge(SpriteSet sprite) {
        if (!this.removed) {
            this.setSprite(sprite.get(lifetime - age, lifetime));
        }
    }

    public float getQuadLength(float partialTicks) {
        return quadLength;
    }

    public Vec3 getTravelledDistance() {
        return new Vec3(travelledDistance);
    }

    public Vec3 getInterpolatedTravelledDistance() {
        return new Vec3(oldTravelledDistance.lerp(travelledDistance, partialTicksCache));
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

    public Vec3 getParticlePosition() {
        return new Vec3(getX(), getY(), getZ());
    }

    public void setParticlePosition(Vec3 pos) {
        setPos(pos.x, pos.y, pos.z);
    }

    public Vec3 getOldParticlePosition() {
        return new Vec3(xo, yo, zo);
    }

    public void setOldParticlePosition(Vec3 pos) {
        xo = pos.x;
        yo = pos.y;
        zo = pos.z;
    }

    public Vec3 getParticleSpeed() {
        return new Vec3(getXMotion(), getYMotion(), getZMotion());
    }

    public void setParticleSpeed(Vec3 speed) {
        setParticleSpeed(speed.x, speed.y, speed.z);
    }

    public int getAge() {
        return age;
    }

    public RandomSource getRandom() {
        return random;
    }

    public void tick(int times) {
        for (int i = 0; i < times; i++) {
            tick();
        }
    }
}