package team.lodestar.lodestone.handlers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import team.lodestar.lodestone.component.LodestoneComponents;
import team.lodestar.lodestone.component.LodestoneWorldComponent;
import team.lodestar.lodestone.events.types.worldevent.WorldEventEvents;
import team.lodestar.lodestone.network.worldevent.UpdateWorldEventPacket;
import team.lodestar.lodestone.registry.client.LodestoneWorldEventRendererRegistry;
import team.lodestar.lodestone.registry.common.LodestonePacketRegistry;
import team.lodestar.lodestone.registry.common.LodestoneWorldEventTypeRegistry;
import team.lodestar.lodestone.systems.worldevent.WorldEventInstance;
import team.lodestar.lodestone.systems.worldevent.WorldEventRenderer;
import team.lodestar.lodestone.systems.worldevent.WorldEventType;

import java.util.Iterator;

public class WorldEventHandler {

    public static boolean playerJoin(Entity entity, Level level, boolean b) {
        if (entity instanceof Player player) {
            if (level instanceof ServerLevel serverLevel) {
                LodestoneComponents.LODESTONE_PLAYER_COMPONENT.maybeGet(player).ifPresent(c -> {
                    LodestoneComponents.LODESTONE_WORLD_COMPONENT.maybeGet(serverLevel).ifPresent(wc -> {
                        if (player instanceof ServerPlayer serverPlayer) {
                            for (WorldEventInstance instance : wc.activeWorldEvents) {
                                if (instance.isClientSynced()) {
                                    WorldEventInstance.sync(instance, serverPlayer);
                                }
                            }
                        }
                    });
                });
            }
        }
        return true;
    }

    public static class ClientOnly {
        public static void renderWorldEvents(PoseStack stack, float partialTicks) {
            LodestoneComponents.LODESTONE_WORLD_COMPONENT.maybeGet(Minecraft.getInstance().level).ifPresent(c -> {
                for (WorldEventInstance instance : c.activeWorldEvents) {
                    WorldEventRenderer<WorldEventInstance> renderer = LodestoneWorldEventRendererRegistry.RENDERERS.get(instance.type);
                    if (renderer != null) {
                        if (renderer.canRender(instance)) {
                            WorldEventEvents.RENDER.invoker().onRender(instance, renderer, stack, RenderHandler.DELAYED_RENDER.getTarget(), partialTicks);
                            renderer.render(instance, stack, RenderHandler.DELAYED_RENDER.getTarget(), partialTicks);
                        }
                    }
                }
            });
        }
    }

    public static <T extends WorldEventInstance> T addWorldEvent(Level level, T instance) {
        return addWorldEvent(level, true, instance);
    }

    public static <T extends WorldEventInstance> T addWorldEvent(Level level, boolean shouldStart, T instance) {
        WorldEventEvents.CREATION.invoker().onCreation(instance, level);
        LodestoneComponents.LODESTONE_WORLD_COMPONENT.maybeGet(level).ifPresent(c -> {
            c.inboundWorldEvents.add(instance);
            if (shouldStart) {
                instance.start(level);
            }
            instance.sync(level);
        });
        return instance;
    }

    public static void worldTick(Level level) {
        if (!level.isClientSide) {
            tick(level);
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
        LodestoneComponents.LODESTONE_WORLD_COMPONENT.maybeGet(level).ifPresent(c -> {
            c.activeWorldEvents.addAll(c.inboundWorldEvents);
            c.inboundWorldEvents.clear();
            Iterator<WorldEventInstance> iterator = c.activeWorldEvents.iterator();
            while (iterator.hasNext()) {
                WorldEventInstance instance = iterator.next();
                if (instance.discarded) {
                    WorldEventEvents.DISCARD.invoker().onDiscard(instance, level);
                    iterator.remove();
                } else {
                    if (!instance.isFrozen()) {
                        WorldEventEvents.TICK.invoker().onTick(instance, level);
                        instance.tick(level);
                    }
                    if (instance.dirty) {
                        LodestonePacketRegistry.LODESTONE_CHANNEL.sendToClientsInCurrentServer(new UpdateWorldEventPacket(instance.uuid, instance.synchronizeNBT()));
                        instance.dirty = false;
                    }
                }
            }
        });
    }

    public static void serializeNBT(LodestoneWorldComponent capability, CompoundTag tag) {
        CompoundTag worldTag = new CompoundTag();
        worldTag.putInt("worldEventCount", capability.activeWorldEvents.size());
        for (int i = 0; i < capability.activeWorldEvents.size(); i++) {
            WorldEventInstance instance = capability.activeWorldEvents.get(i);
            CompoundTag instanceTag = new CompoundTag();
            instance.serializeNBT(instanceTag);
            worldTag.put("worldEvent_" + i, instanceTag);
        }
        tag.put("worldEventData", worldTag);
    }

    public static void deserializeNBT(LodestoneWorldComponent capability, CompoundTag tag) {
        capability.activeWorldEvents.clear();
        CompoundTag worldTag = tag.getCompound("worldEventData");
        int worldEventCount = worldTag.getInt("worldEventCount");
        for (int i = 0; i < worldEventCount; i++) {
            CompoundTag instanceTag = worldTag.getCompound("worldEvent_" + i);
            WorldEventType reader = LodestoneWorldEventTypeRegistry.EVENT_TYPES.get(new ResourceLocation(instanceTag.getString("type")));
            WorldEventInstance eventInstance = reader.createInstance(instanceTag);
            capability.activeWorldEvents.add(eventInstance);
        }
    }
}