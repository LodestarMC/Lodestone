package team.lodestar.lodestone.modules.rendering.particle.standard.screen;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import org.joml.Vector3d;
import team.lodestar.lodestone.handlers.screenparticle.ItemScreenParticleHandler;
import team.lodestar.lodestone.modules.rendering.particle.standard.LodestoneParticleConfig;
import team.lodestar.lodestone.modules.rendering.particle.standard.data.GenericParticleData;
import team.lodestar.lodestone.modules.rendering.particle.standard.data.color.ColorParticleData;
import team.lodestar.lodestone.modules.rendering.particle.standard.data.spin.SpinParticleData;
import team.lodestar.lodestone.modules.rendering.particle.standard.render_types.LodestoneScreenParticleRenderType;
import team.lodestar.lodestone.modules.rendering.particle.standard.screen.base.TextureSheetScreenParticle;
import team.lodestar.lodestone.modules.rendering.particle.standard.world.options.WorldParticleConfig;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.*;
import java.util.function.Consumer;

public class LodestoneScreenParticle extends TextureSheetScreenParticle {

    protected final ParticleEngine.MutableSpriteSet spriteSet;
    protected final LodestoneParticleConfig.Animator spritePicker;

    private final LodestoneScreenParticleRenderType renderType;

    protected final ColorParticleData colorData;
    protected final GenericParticleData transparencyData;
    protected final GenericParticleData scaleData;
    @Nullable
    protected final GenericParticleData lengthData;
    protected final SpinParticleData spinData;

    public final Collection<Consumer<LodestoneScreenParticle>> tickActors;
    public final Collection<Consumer<LodestoneScreenParticle>> renderActors;

    private final boolean tracksStack;
    private final double stackTrackXOffset;
    private final double stackTrackYOffset;

    private int lifeDelay;

    private float quadLength;

    float[] hsv1 = new float[3], hsv2 = new float[3];

    public LodestoneScreenParticle(ClientLevel world, ScreenParticleConfig options, ParticleEngine.MutableSpriteSet spriteSet, double x, double y, double xMotion, double yMotion) {
        super(world, x, y);
        this.spriteSet = spriteSet;
        this.spritePicker = options.animator;
        this.renderType = options.renderType;
        this.colorData = options.colorData;
        this.transparencyData = options.alphaData;
        this.scaleData = options.scaleData;
        this.lengthData = options.lengthData != WorldParticleConfig.DEFAULT_GENERIC ? options.lengthData : null;
        this.spinData = options.spinData;
        this.tickActors = options.tickActors;
        this.renderActors = options.renderActors;
        this.tracksStack = options.tracksStack;
        this.stackTrackXOffset = options.stackTrackXOffset;
        this.stackTrackYOffset = options.stackTrackYOffset;
        this.roll = options.spinData.spinOffset + options.spinData.startingValue;
        this.setLifetime(options.getLifetime());
        this.lifeDelay = options.getLifeDelay();
        this.gravity = options.getGravity();
        this.friction = options.getFriction();
        this.xMotion = xMotion;
        this.yMotion = yMotion;

        colorData.fillHSVArrays(hsv1, hsv2);
        if (spriteSet != null) {
            switch (spritePicker) {
                case FIRST_INDEX, WITH_AGE -> pickSprite(0);
                case LAST_INDEX, WITH_AGE_INVERSE -> pickSprite(spriteSet.sprites.size() - 1);
                case RANDOM_SPRITE -> pickSprite(RANDOM.nextInt(spriteSet.sprites.size()));
            }
        }
        options.spawnActors.forEach(actor -> actor.accept(this));
        updateTraits();
    }

    @Override
    public float getQuadLength(float partialTicks) {
        return lengthData != null ? quadLength : super.getQuadLength(partialTicks);
    }

    public LodestoneParticleConfig.Animator getSpritePicker() {
        return spritePicker;
    }

    public void pickSprite(int spriteIndex) {
        setSprite(spriteSet.sprites.get(Mth.clamp(spriteIndex, 0, spriteSet.sprites.size() - 1)));
    }

    public void pickColor(float delta) {
        float h = Mth.rotLerp(delta, 360f * hsv1[0], 360f * hsv2[0]) / 360f;
        float s = Mth.lerp(delta, hsv1[1], hsv2[1]);
        float v = Mth.lerp(delta, hsv1[2], hsv2[2]);
        int packed = Color.HSBtoRGB(h, s, v);
        float r = FastColor.ARGB32.red(packed) / 255.0f;
        float g = FastColor.ARGB32.green(packed) / 255.0f;
        float b = FastColor.ARGB32.blue(packed) / 255.0f;
        setColor(r, g, b);
    }

    protected void updateTraits() {
        if (scaleData.getProgress(age, lifetime) > 0.8f || transparencyData.getProgress(age, lifetime) > 0.8f) {
            if (a <= 0 || getQuadSize(0) <= 0 || getQuadLength(0) <= 0) {
                remove();
            }
            return;
        }

        float eased = colorData.getColorCurve().ease(colorData.getProgress(age, lifetime));
        pickColor(eased);

        quadSize = scaleData.getValue(age, lifetime);
        quadLength = lengthData != null ? lengthData.getValue(age, lifetime) : quadSize;

        a = Mth.clamp(transparencyData.getValue(age, lifetime), 0, 1);
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
            x = ItemScreenParticleHandler.currentItemX + stackTrackXOffset + xMoved;
            y = ItemScreenParticleHandler.currentItemY + stackTrackYOffset + yMoved;
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