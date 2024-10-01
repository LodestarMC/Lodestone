package team.lodestar.lodestone.handlers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import team.lodestar.lodestone.events.types.worldevent.*;
import team.lodestar.lodestone.network.worldevent.UpdateWorldEventPayload;
import team.lodestar.lodestone.registry.client.LodestoneWorldEventRenderers;
import team.lodestar.lodestone.systems.worldevent.WorldEventInstance;
import team.lodestar.lodestone.systems.worldevent.WorldEventRenderer;

import java.util.Iterator;

public class WorldEventHandler {

    public static class ClientOnly {
        public static void renderWorldEvents(PoseStack stack, float partialTicks) {

            if (Minecraft.getInstance().level != null) {
                var worldData = Minecraft.getInstance().level.getData(LodestoneAttachmentTypes.WORLD_EVENT_DATA);

                for (WorldEventInstance instance : worldData.activeWorldEvents) {
                    WorldEventRenderer<WorldEventInstance> renderer = LodestoneWorldEventRenderers.RENDERERS.get(instance.type);
                    if (renderer != null) {
                        if (renderer.canRender(instance)) {
                            NeoForge.EVENT_BUS.post(new WorldEventRenderEvent(instance, renderer, stack, RenderHandler.DELAYED_RENDER.getTarget(), partialTicks));
                            renderer.render(instance, stack, RenderHandler.DELAYED_RENDER.getTarget(), partialTicks);
                        }
                    }
                }

            }
        }
    }

    public static <T extends WorldEventInstance> T addWorldEvent(Level level, T instance) {
        return addWorldEvent(level, true, instance);
    }

    public static <T extends WorldEventInstance> T addWorldEvent(Level level, boolean shouldStart, T instance) {
        NeoForge.EVENT_BUS.post(new WorldEventCreationEvent(instance, level));

        var worldData = level.getData(LodestoneAttachmentTypes.WORLD_EVENT_DATA);

        worldData.inboundWorldEvents.add(instance);
        if (shouldStart) {
            instance.start(level);
        }
        instance.sync(level);

        return instance;
    }

    public static void playerJoin(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player.level() instanceof ServerLevel level) {

                var worldData = level.getData(LodestoneAttachmentTypes.WORLD_EVENT_DATA);

                if (player instanceof ServerPlayer serverPlayer) {
                    for (WorldEventInstance instance : worldData.activeWorldEvents) {
                        if (instance.type.isClientSynced()) {
                            WorldEventInstance.sync(instance, serverPlayer);
                        }
                    }
                }
            }
        }
    }

    public static void worldTick(LevelTickEvent.Post event) {
        if (!event.getLevel().isClientSide) {
            tick(event.getLevel());
        }
    }

    /**
     * Ticks all active world events in the given level.
     * <p>
     * Will tick on both client and server side.
     * <p>
     * See {@link WorldEventInstance#tick(Level)}
     */
    public static void tick(Level level) {
        var c = level.getData(LodestoneAttachmentTypes.WORLD_EVENT_DATA);
        c.activeWorldEvents.addAll(c.inboundWorldEvents);
        c.inboundWorldEvents.clear();

        Iterator<WorldEventInstance> iterator = c.activeWorldEvents.iterator();
        while (iterator.hasNext()) {
            WorldEventInstance instance = iterator.next();
            if (instance.discarded) {
                NeoForge.EVENT_BUS.post(new WorldEventDiscardEvent(instance, level));
                iterator.remove();
            } else {
                if (!instance.isFrozen()) {
                    NeoForge.EVENT_BUS.post(new WorldEventTickEvent(instance, level));
                    instance.tick(level);
                }
                if (instance.dirty) {
                    PacketDistributor.sendToServer(new UpdateWorldEventPayload(instance.uuid, instance.synchronizeNBT()));
                    instance.dirty = false;
                }
            }
        }
    }
}