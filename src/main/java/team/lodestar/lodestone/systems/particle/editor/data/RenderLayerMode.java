package team.lodestar.lodestone.systems.particle.editor.data;

import team.lodestar.lodestone.handlers.LodestoneRenderHandler;
import team.lodestar.lodestone.systems.rendering.buffer.LodestoneRenderLayer;

public enum RenderLayerMode {
    DEFERRED("deferred", "LodestoneRenderHandler.DEFERRED_RENDER", LodestoneRenderHandler.DEFERRED_RENDER),
    LATE_DEFERRED("late_deferred", "LodestoneRenderHandler.LATE_DEFERRED_RENDER", LodestoneRenderHandler.LATE_DEFERRED_RENDER);

    public final String label;
    public final String javaName;
    public final LodestoneRenderLayer layer;

    RenderLayerMode(String label, String javaName, LodestoneRenderLayer layer) {
        this.label = label;
        this.javaName = javaName;
        this.layer = layer;
    }
}
