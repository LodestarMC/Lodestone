package team.lodestar.lodestone.systems.particle.screen;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import org.joml.Vector3d;
import team.lodestar.lodestone.handlers.screenparticle.ScreenParticleHandler;
import team.lodestar.lodestone.systems.particle.*;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
import team.lodestar.lodestone.systems.particle.render_types.LodestoneScreenParticleRenderType;
import team.lodestar.lodestone.systems.particle.screen.base.TextureSheetScreenParticle;
import team.lodestar.lodestone.systems.particle.world.options.WorldParticleOptions;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.*;
import java.util.function.Consumer;

public class LodestoneScreenParticle extends TextureSheetScreenParticle implements IParticle {

    private final LodestoneScreenParticleRenderType renderType;
    private final LodestoneCommonParticleData<LodestoneScreenParticle> data;

    public LodestoneScreenParticle(ClientLevel world, ScreenParticleOptions options, ParticleEngine.MutableSpriteSet spriteSet, double x, double y, double xMotion, double yMotion) {
        super(world, x, y);
        this.data = new LodestoneCommonParticleData<>(options, spriteSet);
        this.renderType = options.renderType;
        this.roll = options.spinData.spinOffset + options.spinData.startingValue;
        this.setLifetime(options.getLifetime());
        this.lifeDelay = options.getLifeDelay();
        this.gravity = options.getGravity();
        this.friction = options.getFriction();
        this.xMotion = xMotion;
        this.yMotion = yMotion;

        colorData.rgbToHsv(hsv1, hsv2);
        if (spriteSet != null) {
            switch (spritePicker) {
                case FIRST_INDEX, WITH_AGE -> pickSprite(0);
                case LAST_INDEX, WITH_AGE_INVERSE -> pickSprite(spriteSet.sprites.size() - 1);
                case RANDOM_SPRITE -> pickSprite(random.nextInt(spriteSet.sprites.size()));
            }
        }
        options.spawnActors.forEach(actor -> actor.accept(this));
        updateTraits();
    }

    @Override
    public float getQuadLength(float partialTicks) {
        return lengthData != null ? quadLength : super.getQuadLength(partialTicks);
    }

    public SimpleParticleOptions.ParticleSpritePicker getSpritePicker() {
        return spritePicker;
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
            }
            return;
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
    }

    @Override
    public void render(BufferBuilder bufferBuilder, @Nullable PoseStack poseStack) {
        if (lifeDelay > 0) {
            return;
        }
        if (tracksStack) {
            x = ScreenParticleHandler.currentItemX + stackTrackXOffset + xMoved;
            y = ScreenParticleHandler.currentItemY + stackTrackYOffset + yMoved;
        }
        renderActors.forEach(actor -> actor.accept(this));
        super.render(bufferBuilder, poseStack);
    }

    @Override
    public void tick() {
        if (lifeDelay > 0) {
            lifeDelay--;
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
    public LodestoneScreenParticleRenderType getRenderType() {
        return renderType;
    }

    public void setSpriteFromInverseAge(SpriteSet sprite) {
        if (!this.removed) {
            this.setSprite(sprite.get(lifetime - age, lifetime));
        }
    }

    public void setParticleSpeed(Vector3d speed) {
        setParticleSpeed(speed.x, speed.y);
    }
}