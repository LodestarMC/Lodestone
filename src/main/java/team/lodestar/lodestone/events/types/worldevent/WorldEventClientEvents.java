package team.lodestar.lodestone.events.types.worldevent;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.renderer.MultiBufferSource;
import team.lodestar.lodestone.systems.worldevent.WorldEventInstance;
import team.lodestar.lodestone.systems.worldevent.WorldEventRenderer;

@Environment(EnvType.CLIENT)
public class WorldEventClientEvents {
    public static final Event<Render> RENDER = EventFactory.createArrayBacked(Render.class, callbacks -> (worldEvent, renderer, poseStack, multiBufferSource, partialTicks) -> {
        for (Render e : callbacks)
            e.onRender(worldEvent, renderer, poseStack, multiBufferSource, partialTicks);
    });

    @FunctionalInterface
    public interface Render {
        void onRender(WorldEventInstance worldEvent, WorldEventRenderer<WorldEventInstance> renderer, PoseStack poseStack, MultiBufferSource multiBufferSource, float partialTicks);
    }
}
