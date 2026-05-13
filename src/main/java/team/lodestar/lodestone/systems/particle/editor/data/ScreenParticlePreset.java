package team.lodestar.lodestone.systems.particle.editor.data;

import team.lodestar.lodestone.registry.common.particle.LodestoneScreenParticleTypes;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleType;

public enum ScreenParticlePreset {
    WISP("wisp", "LodestoneScreenParticleTypes.WISP", LodestoneScreenParticleTypes.WISP),
    SMOKE("smoke", "LodestoneScreenParticleTypes.SMOKE", LodestoneScreenParticleTypes.SMOKE),
    SPARKLE("sparkle", "LodestoneScreenParticleTypes.SPARKLE", LodestoneScreenParticleTypes.SPARKLE),
    TWINKLE("twinkle", "LodestoneScreenParticleTypes.TWINKLE", LodestoneScreenParticleTypes.TWINKLE),
    STAR("star", "LodestoneScreenParticleTypes.STAR", LodestoneScreenParticleTypes.STAR);

    public final String label;
    public final String javaName;
    public final ScreenParticleType<?> type;

    ScreenParticlePreset(String label, String javaName, ScreenParticleType<?> type) {
        this.label = label;
        this.javaName = javaName;
        this.type = type;
    }
}
