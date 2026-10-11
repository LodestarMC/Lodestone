package team.lodestar.lodestone.modules.rendering.particle.standard;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;
import net.minecraft.util.*;
import org.joml.*;
import team.lodestar.lodestone.modules.rendering.particle.standard.data.*;
import team.lodestar.lodestone.modules.rendering.particle.standard.data.color.*;
import team.lodestar.lodestone.modules.rendering.particle.standard.data.spin.*;
import team.lodestar.lodestone.modules.rendering.particle.standard.render_types.*;
import team.lodestar.lodestone.modules.rendering.particle.standard.renderer.*;

import java.util.*;

public class LodestoneParticleBody {

    public enum Type {
        WORLD,
        SCREEN
    }

    protected final RandomSource random = RandomSource.create();

    protected final ClientLevel level;
    protected final Type type;
    protected final LodestoneParticleRenderer renderer;
    protected final ParticleEngine.MutableSpriteSet spriteSet;

    protected final ColorParticleData colorData;
    protected final GenericParticleData alphaData;
    protected final GenericParticleData scaleData;
    protected final GenericParticleData lengthData;
    protected final SpinParticleData spinData;

    protected final Vector3f oldPosition = new Vector3f();
    protected final Vector3f position = new Vector3f();
    protected final Vector3f oldMotion = new Vector3f();
    protected final Vector3f motion = new Vector3f();
    protected final Vector2f scale = new Vector2f();

    protected boolean real, removed;

    protected int delay, age, lifetime;
    protected float gravity, friction;

    public final Collection<LodestoneParticleBehavior> spawnActors = new ArrayList<>();
    public final Collection<LodestoneParticleBehavior> removeActors = new ArrayList<>();

    public final Collection<LodestoneParticleBehavior> tickActors = new ArrayList<>();
    public final Collection<LodestoneParticleBehavior> renderActors = new ArrayList<>();

    public LodestoneParticleBody(ClientLevel level, LodestoneParticleConfig config, ParticleEngine.MutableSpriteSet spriteSet) {
        this.level = level;
        this.type = config.type;
        this.renderer = config.renderer;
        this.spriteSet = spriteSet;

        this.colorData = config.getColorData();
        this.alphaData = config.getAlphaData();
        this.scaleData = config.getScaleData();
        this.lengthData = config.getLengthData();
        this.spinData = config.getSpinData();

        this.gravity = config.getGravity();
        this.friction = config.getFriction();

        if (delay == 0) {
            spawn();
        }
    }

    public void place(Vector3f position, Vector3f motion) {
        this.oldPosition.set(position);
        this.position.set(position);
        this.oldMotion.set(motion);
        this.motion.set(motion);
    }

    public void tick() {
        if (!real) {
            if (delay == 0) {
                spawn();
            }
            if (delay > 0) {
                delay--;
                return;
            }
        }
        age++;
        if (age >= lifetime) {
            remove();
            return;
        }

        tickActors.forEach(actor -> actor.act(this));


//        updateTraits();
//        super.tick();
//        if (spriteSet != null) {
//            switch (spritePicker) {
//                case WITH_AGE -> setSpriteFromAge(spriteSet);
//                case WITH_AGE_INVERSE -> setSpriteFromInverseAge(spriteSet);
//            }
//        }
    }

    public void render(Camera camera, float partialTicks) {
        if (!real || removed) {
            return;
        }
        renderActors.forEach(actor -> actor.act(this));
        if (renderer != null) {
            renderer.render(this, getVertexConsumer(), camera, partialTicks);
        }
    }

    public VertexConsumer getVertexConsumer() {
        VertexConsumer consumerToUse = original;
        if (renderType instanceof LodestoneWorldParticleRenderType lodestone) {
            consumerToUse = renderLayer.getParticleTarget().getBuffer(lodestone.renderType);
        }
        return consumerToUse;
    }

    public void spawn() {
        spawnActors.forEach(actor -> actor.act(this));
        real = true;
    }

    public void remove() {
        removeActors.forEach(actor -> actor.act(this));
        removed = true;
    }

    public Vector3f getInterpolatedPosition(Camera camera, float partialTicks) {
        var cameraPosition = camera.getPosition();
        return getInterpolatedPosition(partialTicks).sub((float) cameraPosition.x, (float) cameraPosition.y, (float) cameraPosition.z);
    }

    public Vector3f getInterpolatedPosition(float partialTicks) {
        return oldPosition.lerp(position, partialTicks);
    }

}
