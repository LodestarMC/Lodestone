package team.lodestar.lodestone.events.types.worldevent;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.Level;
import team.lodestar.lodestone.systems.worldevent.WorldEventInstance;
import team.lodestar.lodestone.systems.worldevent.WorldEventRenderer;

public class WorldEventRenderEvent extends WorldEventInstanceEvent {
    private final WorldEventRenderer<WorldEventInstance> renderer;
    private final PoseStack poseStack;
    private final MultiBufferSource multiBufferSource;
    private final float partialTicks;
    public WorldEventRenderEvent(WorldEventInstance worldEvent, WorldEventRenderer<WorldEventInstance> renderer, PoseStack poseStack, MultiBufferSource multiBufferSource, float partialTicks) {
        super(worldEvent, null);
        this.renderer = renderer;
        this.poseStack = poseStack;
        this.multiBufferSource = multiBufferSource;
        this.partialTicks = partialTicks;
    }

    public WorldEventRenderer<WorldEventInstance> getRenderer() {
        return renderer;
    }

    public PoseStack getPoseStack() {
        return poseStack;
    }

    public MultiBufferSource getMultiBufferSource() {
        return multiBufferSource;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    @Override
    public boolean isCancelable() {
        return false;
    }
}
