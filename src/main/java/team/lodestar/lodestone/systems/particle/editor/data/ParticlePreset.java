package team.lodestar.lodestone.systems.particle.editor.data;

import team.lodestar.lodestone.registry.common.particle.LodestoneParticleTypes;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType;

import java.util.function.Supplier;

public enum ParticlePreset {
    WISP("wisp", "LodestoneParticleTypes.WISP_PARTICLE", LodestoneParticleTypes.WISP_PARTICLE),
    SMOKE("smoke", "LodestoneParticleTypes.SMOKE_PARTICLE", LodestoneParticleTypes.SMOKE_PARTICLE),
    SPARKLE("sparkle", "LodestoneParticleTypes.SPARKLE_PARTICLE", LodestoneParticleTypes.SPARKLE_PARTICLE),
    TWINKLE("twinkle", "LodestoneParticleTypes.TWINKLE_PARTICLE", LodestoneParticleTypes.TWINKLE_PARTICLE),
    STAR("star", "LodestoneParticleTypes.STAR_PARTICLE", LodestoneParticleTypes.STAR_PARTICLE),
    SPARK("spark", "LodestoneParticleTypes.SPARK_PARTICLE", LodestoneParticleTypes.SPARK_PARTICLE),
    EXTRUDING_SPARK("extruding_spark", "LodestoneParticleTypes.EXTRUDING_SPARK_PARTICLE", LodestoneParticleTypes.EXTRUDING_SPARK_PARTICLE),
    THIN_EXTRUDING_SPARK("thin_extruding_spark", "LodestoneParticleTypes.THIN_EXTRUDING_SPARK_PARTICLE", LodestoneParticleTypes.THIN_EXTRUDING_SPARK_PARTICLE);

    public final String label;
    public final String javaName;
    public final Supplier<? extends LodestoneWorldParticleType> supplier;

    ParticlePreset(String label, String javaName, Supplier<? extends LodestoneWorldParticleType> supplier) {
        this.label = label;
        this.javaName = javaName;
        this.supplier = supplier;
    }
}
