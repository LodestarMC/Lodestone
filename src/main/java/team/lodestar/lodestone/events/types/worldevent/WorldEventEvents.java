package team.lodestar.lodestone.events.types.worldevent;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.Level;
import team.lodestar.lodestone.systems.worldevent.WorldEventInstance;
import team.lodestar.lodestone.systems.worldevent.WorldEventRenderer;

public class WorldEventEvents {

    public static final Event<Creation> CREATION = EventFactory.createArrayBacked(Creation.class, callbacks -> (worldEvent, level) -> {
        for (Creation e : callbacks)
            e.onCreation(worldEvent, level);
    });

    public static final Event<Discard> DISCARD = EventFactory.createArrayBacked(Discard.class, callbacks -> (worldEvent, level) -> {
        for (Discard e : callbacks)
            e.onDiscard(worldEvent, level);
    });

    public static final Event<Tick> TICK = EventFactory.createArrayBacked(Tick.class, callbacks -> (worldEvent, level) -> {
        for (Tick e : callbacks)
            e.onTick(worldEvent, level);
    });

    public static final Event<Render> RENDER = EventFactory.createArrayBacked(Render.class, callbacks -> (worldEvent, renderer, poseStack, multiBufferSource, partialTicks) -> {
        for (Render e : callbacks)
            e.onRender(worldEvent, renderer, poseStack, multiBufferSource, partialTicks);
    });

    @FunctionalInterface
    public interface Creation {
        void onCreation(WorldEventInstance worldEvent, Level level);
    }

    @FunctionalInterface
    public interface Discard {
        void onDiscard(WorldEventInstance worldEvent, Level level);
    }

    @FunctionalInterface
    public interface Tick {
        void onTick(WorldEventInstance worldEvent, Level level);
    }

    @FunctionalInterface
    public interface Render {
        void onRender(WorldEventInstance worldEvent, WorldEventRenderer<WorldEventInstance> renderer, PoseStack poseStack, MultiBufferSource multiBufferSource, float partialTicks);
    }

}
