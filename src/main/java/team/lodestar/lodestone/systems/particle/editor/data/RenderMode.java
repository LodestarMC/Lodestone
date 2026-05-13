package team.lodestar.lodestone.systems.particle.editor.data;

import net.minecraft.client.particle.ParticleRenderType;
import team.lodestar.lodestone.systems.particle.render_types.LodestoneWorldParticleRenderType;

public enum RenderMode {
    ADDITIVE("additive", "LodestoneWorldParticleRenderType.ADDITIVE", LodestoneWorldParticleRenderType.ADDITIVE),
    LUMITRANSPARENT("transparent", "LodestoneWorldParticleRenderType.LUMITRANSPARENT", LodestoneWorldParticleRenderType.LUMITRANSPARENT),
    TRANSPARENT("alpha_transparent", "LodestoneWorldParticleRenderType.TRANSPARENT", LodestoneWorldParticleRenderType.TRANSPARENT);

    public final String label;
    public final String javaName;
    public final LodestoneWorldParticleRenderType renderType;

    RenderMode(String label, String javaName, LodestoneWorldParticleRenderType renderType) {
        this.label = label;
        this.javaName = javaName;
        this.renderType = renderType;
    }

    public ParticleRenderType get(boolean depthFade) {
        return depthFade ? renderType.withDepthFade() : renderType;
    }

    public String javaName(boolean depthFade) {
        return depthFade ? javaName + ".withDepthFade()" : javaName;
    }
}
