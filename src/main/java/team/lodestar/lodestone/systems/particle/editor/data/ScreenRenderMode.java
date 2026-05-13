package team.lodestar.lodestone.systems.particle.editor.data;

import team.lodestar.lodestone.systems.particle.render_types.LodestoneScreenParticleRenderType;

public enum ScreenRenderMode {
    ADDITIVE("additive", "LodestoneScreenParticleRenderType.ADDITIVE", LodestoneScreenParticleRenderType.ADDITIVE),
    LUMITRANSPARENT("transparent", "LodestoneScreenParticleRenderType.LUMITRANSPARENT", LodestoneScreenParticleRenderType.LUMITRANSPARENT),
    TRANSPARENT("alpha_transparent", "LodestoneScreenParticleRenderType.TRANSPARENT", LodestoneScreenParticleRenderType.TRANSPARENT);

    public final String label;
    public final String javaName;
    public final LodestoneScreenParticleRenderType renderType;

    ScreenRenderMode(String label, String javaName, LodestoneScreenParticleRenderType renderType) {
        this.label = label;
        this.javaName = javaName;
        this.renderType = renderType;
    }
}
