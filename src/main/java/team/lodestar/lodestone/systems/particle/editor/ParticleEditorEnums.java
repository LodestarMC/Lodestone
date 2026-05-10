package team.lodestar.lodestone.systems.particle.editor;

import net.minecraft.client.particle.ParticleRenderType;
import team.lodestar.lodestone.handlers.LodestoneRenderHandler;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleTypes;
import team.lodestar.lodestone.registry.common.particle.LodestoneScreenParticleTypes;
import team.lodestar.lodestone.systems.particle.render_types.LodestoneScreenParticleRenderType;
import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleType;
import team.lodestar.lodestone.systems.particle.world.type.LodestoneWorldParticleType;
import team.lodestar.lodestone.systems.rendering.buffer.LodestoneRenderLayer;

import java.util.function.Supplier;

enum ParticleOutputMode {
    WORLD("world"),
    SCREEN("screen");

    final String label;

    ParticleOutputMode(String label) {
        this.label = label;
    }
}

enum SpawnDistributionMode {
    BUILDER_RANDOM("builder_random"),
    POINT("point"),
    SPHERE("sphere"),
    CIRCLE("circle"),
    CUBE("cube");

    final String label;

    SpawnDistributionMode(String label) {
        this.label = label;
    }
}

enum PreviewPlaybackMode {
    LOOPING("looping"),
    ONE_SHOT("one_shot");

    final String label;

    PreviewPlaybackMode(String label) {
        this.label = label;
    }
}

enum LivePreviewScope {
    ACTIVE("active"),
    VISIBLE_PROJECTS("visible_projects");

    final String label;

    LivePreviewScope(String label) {
        this.label = label;
    }
}

enum ParticlePreset {
    WISP("wisp", "LodestoneParticleTypes.WISP_PARTICLE", LodestoneParticleTypes.WISP_PARTICLE),
    SMOKE("smoke", "LodestoneParticleTypes.SMOKE_PARTICLE", LodestoneParticleTypes.SMOKE_PARTICLE),
    SPARKLE("sparkle", "LodestoneParticleTypes.SPARKLE_PARTICLE", LodestoneParticleTypes.SPARKLE_PARTICLE),
    TWINKLE("twinkle", "LodestoneParticleTypes.TWINKLE_PARTICLE", LodestoneParticleTypes.TWINKLE_PARTICLE),
    STAR("star", "LodestoneParticleTypes.STAR_PARTICLE", LodestoneParticleTypes.STAR_PARTICLE),
    SPARK("spark", "LodestoneParticleTypes.SPARK_PARTICLE", LodestoneParticleTypes.SPARK_PARTICLE),
    EXTRUDING_SPARK("extruding_spark", "LodestoneParticleTypes.EXTRUDING_SPARK_PARTICLE", LodestoneParticleTypes.EXTRUDING_SPARK_PARTICLE),
    THIN_EXTRUDING_SPARK("thin_extruding_spark", "LodestoneParticleTypes.THIN_EXTRUDING_SPARK_PARTICLE", LodestoneParticleTypes.THIN_EXTRUDING_SPARK_PARTICLE);

    final String label;
    final String javaName;
    final Supplier<? extends LodestoneWorldParticleType> supplier;

    ParticlePreset(String label, String javaName, Supplier<? extends LodestoneWorldParticleType> supplier) {
        this.label = label;
        this.javaName = javaName;
        this.supplier = supplier;
    }
}

enum ScreenParticlePreset {
    WISP("wisp", "LodestoneScreenParticleTypes.WISP", LodestoneScreenParticleTypes.WISP),
    SMOKE("smoke", "LodestoneScreenParticleTypes.SMOKE", LodestoneScreenParticleTypes.SMOKE),
    SPARKLE("sparkle", "LodestoneScreenParticleTypes.SPARKLE", LodestoneScreenParticleTypes.SPARKLE),
    TWINKLE("twinkle", "LodestoneScreenParticleTypes.TWINKLE", LodestoneScreenParticleTypes.TWINKLE),
    STAR("star", "LodestoneScreenParticleTypes.STAR", LodestoneScreenParticleTypes.STAR);

    final String label;
    final String javaName;
    final ScreenParticleType<?> type;

    ScreenParticlePreset(String label, String javaName, ScreenParticleType<?> type) {
        this.label = label;
        this.javaName = javaName;
        this.type = type;
    }
}

enum RenderMode {
    ADDITIVE("additive", "LodestoneWorldParticleRenderType.ADDITIVE", LodestoneWorldParticleRenderType.ADDITIVE),
    LUMITRANSPARENT("transparent", "LodestoneWorldParticleRenderType.LUMITRANSPARENT", LodestoneWorldParticleRenderType.LUMITRANSPARENT),
    TRANSPARENT("alpha_transparent", "LodestoneWorldParticleRenderType.TRANSPARENT", LodestoneWorldParticleRenderType.TRANSPARENT);

    final String label;
    final String javaName;
    final LodestoneWorldParticleRenderType renderType;

    RenderMode(String label, String javaName, LodestoneWorldParticleRenderType renderType) {
        this.label = label;
        this.javaName = javaName;
        this.renderType = renderType;
    }

    ParticleRenderType get(boolean depthFade) {
        return depthFade ? renderType.withDepthFade() : renderType;
    }

    String javaName(boolean depthFade) {
        return depthFade ? javaName + ".withDepthFade()" : javaName;
    }
}

enum ScreenRenderMode {
    ADDITIVE("additive", "LodestoneScreenParticleRenderType.ADDITIVE", LodestoneScreenParticleRenderType.ADDITIVE),
    LUMITRANSPARENT("transparent", "LodestoneScreenParticleRenderType.LUMITRANSPARENT", LodestoneScreenParticleRenderType.LUMITRANSPARENT),
    TRANSPARENT("alpha_transparent", "LodestoneScreenParticleRenderType.TRANSPARENT", LodestoneScreenParticleRenderType.TRANSPARENT);

    final String label;
    final String javaName;
    final LodestoneScreenParticleRenderType renderType;

    ScreenRenderMode(String label, String javaName, LodestoneScreenParticleRenderType renderType) {
        this.label = label;
        this.javaName = javaName;
        this.renderType = renderType;
    }
}

enum RenderLayerMode {
    DEFERRED("deferred", "LodestoneRenderHandler.DEFERRED_RENDER", LodestoneRenderHandler.DEFERRED_RENDER),
    LATE_DEFERRED("late_deferred", "LodestoneRenderHandler.LATE_DEFERRED_RENDER", LodestoneRenderHandler.LATE_DEFERRED_RENDER);

    final String label;
    final String javaName;
    final LodestoneRenderLayer layer;

    RenderLayerMode(String label, String javaName, LodestoneRenderLayer layer) {
        this.label = label;
        this.javaName = javaName;
        this.layer = layer;
    }
}

enum BehaviorMode {
    BILLBOARD("billboard"),
    DIRECTIONAL("directional"),
    POINTY_DIRECTIONAL("pointy_directional"),
    SPARK("spark");

    final String label;

    BehaviorMode(String label) {
        this.label = label;
    }
}

enum LightMode {
    FULL_BRIGHT("full_bright"),
    NATURAL("natural"),
    CUSTOM("custom");

    final String label;

    LightMode(String label) {
        this.label = label;
    }
}
