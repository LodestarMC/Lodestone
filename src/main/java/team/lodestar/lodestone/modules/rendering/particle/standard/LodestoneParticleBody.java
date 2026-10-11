package team.lodestar.lodestone.modules.rendering.particle.standard;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;
import net.minecraft.util.*;
import net.minecraft.world.phys.*;
import org.joml.*;
import team.lodestar.lodestone.modules.rendering.particle.standard.data.*;
import team.lodestar.lodestone.modules.rendering.particle.standard.data.color.*;
import team.lodestar.lodestone.modules.rendering.particle.standard.data.spin.*;
import team.lodestar.lodestone.modules.rendering.particle.standard.renderer.*;

import java.awt.*;
import java.lang.Math;
import java.util.*;

public class LodestoneParticleBody {

//    private static final AABB INITIAL_AABB = new AABB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);


    public enum Type {
        WORLD,
        SCREEN;
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
    protected final Vector3f oldTotalTravel = new Vector3f();

    protected final Vector3f totalTravel = new Vector3f();
    protected final Vector2f oldScale = new Vector2f();

    protected final Vector2f scale = new Vector2f();
//    protected AABB boundingBox;
    protected boolean real, removed;

    protected int delay, age, lifetime;

    protected float gravity, friction;
    protected float oldSpin, spin;

    protected boolean hasPhysics;

    protected boolean onGround;
    protected boolean stoppedByCollision;

    protected float[] hsv1 = new float[3], hsv2 = new float[3];

    protected float[] rgba = new float[4];
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

        this.delay = config.getDelay();
        this.lifetime = config.getLifetime();

        this.gravity = config.getGravity();
        this.friction = config.getFriction();

//        this.boundingBox = INITIAL_AABB;

        colorData.fillHSVArrays(hsv1, hsv2);

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
            if (delay > 0) {
                delay--;
                if (delay == 0) {
                    spawn();
                }
            }
            return;
        }
        age++;
        if (age >= lifetime) {
            remove();
            return;
        }

        if (scaleData.getProgress(age, lifetime) > 0.8f || alphaData.getProgress(age, lifetime) > 0.8f) {
            if (rgba[3] <= 0 || scale.x <= 0 || scale.y <= 0) {
                remove();
                return;
            }
        }

        tickActors.forEach(actor -> actor.act(this));

        updateValues();
//        updateTraits();
//        super.tick();
//        if (spriteSet != null) {
//            switch (spritePicker) {
//                case WITH_AGE -> setSpriteFromAge(spriteSet);
//                case WITH_AGE_INVERSE -> setSpriteFromInverseAge(spriteSet);
//            }
//        }
    }

    public void updateValues() {
        applyMotion();
        updateColor();
        updateScale();
        updateSpin();
    }

    public void applyMotion() {
        oldMotion.set(motion);
        oldPosition.set(position);
        oldTotalTravel.set(totalTravel);
        motion.y -= 0.04f * gravity;

        move(motion);
        motion.mul(friction);
        if (onGround) {
            motion.x *= 0.7f;
            motion.z *= 0.7f;
        }
    }

    public void move(Vector3f motion) {
        if (this.stoppedByCollision) {
            return;
        }
        float x = motion.x;
        float y = motion.y;
        float z = motion.z;

        float originalX = x;
        float originalY = y;
        float originalZ = z;

        var isWorld = type.equals(Type.WORLD);
        if (isWorld) {
            if (hasPhysics && motion.length() > 0 && motion.lengthSquared() < 10000) {
//                var collided = Entity.collideBoundingBox(null, new Vec3(motion), boundingBox, level, List.of());
//                x = (float) collided.x;
//                y = (float) collided.y;
//                z = (float) collided.z;
            }
        }

        if (x != 0.0 || y != 0.0 || z != 0.0) {
            position.add(x, y, z);
            totalTravel.add(x, y, z);
            if (isWorld) {
                //TODO: Update Bounding Box
            }
        }

        if (Math.abs(originalY) >= 9.999999747378752E-6 && Math.abs(y) < 9.999999747378752E-6) {
            this.stoppedByCollision = true;
        }

        this.onGround = originalY != y && originalY < 0.0;
        if (originalX != x) {
            motion.x = 0;
        }
        if (originalZ != z) {
            motion.z = 0;
        }
    }

    public void updateColor() {
        var raw = colorData.getProgress(age, lifetime);
        float delta = colorData.getColorCurve().ease(raw);
        float h = Mth.rotLerp(delta, 360f * hsv1[0], 360f * hsv2[0]) / 360f;
        float s = Mth.lerp(delta, hsv1[1], hsv2[1]);
        float v = Mth.lerp(delta, hsv1[2], hsv2[2]);
        int packed = Color.HSBtoRGB(h, s, v);
        float r = FastColor.ARGB32.red(packed) / 255.0f;
        float g = FastColor.ARGB32.green(packed) / 255.0f;
        float b = FastColor.ARGB32.blue(packed) / 255.0f;
        var a = Mth.clamp(alphaData.getValue(age, lifetime), 0, 1);
        rgba[0] = r;
        rgba[1] = g;
        rgba[2] = b;
        rgba[3] = a;
    }

    public void updateScale() {
        oldScale.set(scale);
        scale.x = scaleData.getValue(age, lifetime);
        scale.y = lengthData.getValue(age, lifetime);
    }

    public void updateSpin() {
        oldSpin = spin;
        spin += spinData.getValue(age, lifetime);
    }


//    protected void updateAABB(float width, float height) {
//        double x = (boundingBox.minX + boundingBox.maxX - (double) width) / 2.0;
//        double z = (boundingBox.minZ + boundingBox.maxZ - (double) width) / 2.0;
//        boundingBox = new AABB(x, boundingBox.minY, z, x + width, boundingBox.minY + height, z + width);
//    }
    public void render(Camera camera, VertexConsumer consumer, float partialTicks) {
        if (!real || removed) {
            return;
        }
        renderActors.forEach(actor -> actor.act(this));
        if (renderer != null) {
            renderer.render(this, consumer, camera, partialTicks);
        }
    }

    public void spawn() {
        spawnActors.forEach(actor -> actor.act(this));
        real = true;
        updateValues();
    }

    public void remove() {
        removeActors.forEach(actor -> actor.act(this));
        removed = true;
    }

    public Type getType() {
        return type;
    }

    public boolean isWorld() {
        return type.equals(Type.WORLD);
    }

    public Vector3f getInterpolatedPosition(Camera camera, float partialTicks) {
        var cameraPosition = camera.getPosition();
        return getInterpolatedPosition(partialTicks).sub((float) cameraPosition.x, (float) cameraPosition.y, (float) cameraPosition.z);
    }

    public Vector3f getInterpolatedPosition(float partialTicks) {
        return oldPosition.lerp(position, partialTicks);
    }

    public float getInterpolatedWidth(float partialTicks) {
        return Mth.lerp(partialTicks, oldScale.x, scale.x);
    }

    public float getInterpolatedHeight(float partialTicks) {
        return Mth.lerp(partialTicks, oldScale.y, scale.y);
    }

    public float getInterpolatedSpin(float partialTicks) {
        return Mth.lerp(partialTicks, oldSpin, spin);
    }
}
