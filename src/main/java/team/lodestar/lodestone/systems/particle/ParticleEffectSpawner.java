package team.lodestar.lodestone.systems.particle;

import net.minecraft.world.level.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.Nullable;
import team.lodestar.lodestone.systems.particle.builder.*;
import team.lodestar.lodestone.systems.particle.world.options.*;


import java.util.function.*;

public class ParticleEffectSpawner {

    private final WorldParticleBuilder builder;
    private final Consumer<WorldParticleBuilder> particleSpawner;

    private final WorldParticleBuilder bloomBuilder;
    private final Consumer<WorldParticleBuilder> bloomSpawner;

    public ParticleEffectSpawner(WorldParticleBuilder builder, Consumer<WorldParticleBuilder> particleSpawner, @Nullable WorldParticleBuilder bloomBuilder, @Nullable Consumer<WorldParticleBuilder> bloomSpawner) {
        this.builder = builder;
        this.particleSpawner = particleSpawner;
        this.bloomBuilder = bloomBuilder;
        this.bloomSpawner = bloomSpawner;
    }

    public ParticleEffectSpawner(Level level, Vec3 pos, WorldParticleBuilder builder, WorldParticleBuilder bloomBuilder) {
        this(builder, b -> b.spawn(level, pos.x, pos.y, pos.z), bloomBuilder, b -> b.spawn(level, pos.x, pos.y, pos.z));
    }

    public ParticleEffectSpawner(Level level, Vec3 pos, WorldParticleBuilder builder) {
        this(builder, b -> b.spawn(level, pos.x, pos.y, pos.z), null, null);
    }

    public ParticleEffectSpawner(WorldParticleBuilder builder, Consumer<WorldParticleBuilder> particleSpawner) {
        this(builder, particleSpawner, null, null);
    }

    public WorldParticleBuilder getBuilder() {
        return builder;
    }

    public ParticleEffectSpawner act(Consumer<WorldParticleBuilder> builderConsumer) {
        builderConsumer.accept(bloomBuilder);
        builderConsumer.accept(builder);
        return this;
    }

    public ParticleEffectSpawner actRaw(Consumer<WorldParticleBuilder> builderConsumer) {
        builderConsumer.accept(builder);
        return this;
    }

    public WorldParticleBuilder getBloomBuilder() {
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