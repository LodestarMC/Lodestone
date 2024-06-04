package team.lodestar.lodestone.systems.particle;

import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import team.lodestar.lodestone.systems.particle.builder.*;
import team.lodestar.lodestone.systems.particle.world.options.*;

import javax.annotation.*;
import java.util.function.*;

public class ParticleEffectSpawner<T extends WorldParticleOptions> {

    private final WorldParticleBuilder<T> builder;
    private final Consumer<WorldParticleBuilder<T>> particleSpawner;

    private final WorldParticleBuilder<T> bloomBuilder;
    private final Consumer<WorldParticleBuilder<T>> bloomSpawner;

    public ParticleEffectSpawner(WorldParticleBuilder<T> builder, Consumer<WorldParticleBuilder<T>> particleSpawner, @Nullable WorldParticleBuilder<T> bloomBuilder, @Nullable Consumer<WorldParticleBuilder<T>> bloomSpawner) {
        this.builder = builder;
        this.particleSpawner = particleSpawner;
        this.bloomBuilder = bloomBuilder;
        this.bloomSpawner = bloomSpawner;
    }

    public ParticleEffectSpawner(Level level, Vec3 pos, WorldParticleBuilder<T> builder, WorldParticleBuilder<T> bloomBuilder) {
        this(builder, b -> b.spawn(level, pos.x, pos.y, pos.z), bloomBuilder, b -> b.spawn(level, pos.x, pos.y, pos.z));
    }

    public ParticleEffectSpawner(Level level, Vec3 pos, WorldParticleBuilder<T> builder) {
        this(builder, b -> b.spawn(level, pos.x, pos.y, pos.z), null, null);
    }

    public ParticleEffectSpawner(WorldParticleBuilder<T> builder, Consumer<WorldParticleBuilder<T>> particleSpawner) {
        this(builder, particleSpawner, null, null);
    }

    public WorldParticleBuilder<T> getBuilder() {
        return builder;
    }

    public WorldParticleBuilder<T> getBloomBuilder() {
        return bloomBuilder;
    }

    public void spawnParticles() {
        particleSpawner.accept(builder);
        if (bloomSpawner != null) {
            bloomSpawner.accept(bloomBuilder);
        }
    }

    public void spawnParticlesRaw() {
        particleSpawner.accept(builder);
    }
}