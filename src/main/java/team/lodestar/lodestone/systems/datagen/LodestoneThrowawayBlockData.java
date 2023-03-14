package team.lodestar.lodestone.systems.datagen;

import net.minecraft.client.renderer.RenderType;
import team.lodestar.lodestone.handlers.ThrowawayBlockDataHandler;

import java.util.function.Supplier;

/**
 * Various throwaway data stored in {@link ThrowawayBlockDataHandler#THROWAWAY_DATA_CACHE}, which is cleared after the game is finished loading.
 */
public class LodestoneThrowawayBlockData {

    public static final LodestoneThrowawayBlockData EMPTY = new LodestoneThrowawayBlockData();
    private Supplier<Supplier<RenderType>> renderType;
    private boolean hasCustomRenderType;

    public LodestoneThrowawayBlockData() {
    }

    public LodestoneThrowawayBlockData setRenderType(Supplier<Supplier<RenderType>> renderType) {
        this.renderType = renderType;
        this.hasCustomRenderType = true;
        return this;
    }

    public Supplier<Supplier<RenderType>> getRenderType() {
        return renderType;
    }

    public boolean hasCustomRenderType() {
        return hasCustomRenderType;
    }
}