package com.sammy.ortus.handlers;

import com.sammy.ortus.capability.PlayerDataCapability;
import com.sammy.ortus.capability.WorldDataCapability;
import com.sammy.ortus.setup.worldevent.OrtusWorldEventRendererRegistry;
import com.sammy.ortus.setup.worldevent.OrtusWorldEventTypeRegistry;
import com.sammy.ortus.systems.worldevent.WorldEventInstance;
import com.sammy.ortus.systems.worldevent.WorldEventRenderer;
import com.sammy.ortus.systems.worldevent.WorldEventType;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

import java.util.Iterator;

public class WorldEventHandler {

    public static class ClientOnly {
        public static void renderWorldEvents(RenderLevelLastEvent event) {
            WorldDataCapability.getCapability(Minecraft.getInstance().level).ifPresent(capability -> {
                for (WorldEventInstance instance : capability.activeWorldEvents) {
                    WorldEventRenderer<WorldEventInstance> renderer = OrtusWorldEventRendererRegistry.RENDERERS.get(instance.type);
                    if (renderer != null) {
                        if (renderer.canRender(instance)) {
                            renderer.render(instance, event.getPoseStack(), RenderHandler.DELAYED_RENDER, event.getPartialTick());
                        }
                    }
                }
            });
        }
    }

    public static <T extends WorldEventInstance> T addWorldEvent(Level level, T instance) {
        WorldDataCapability.getCapability(level).ifPresent(capability -> {
            capability.inboundWorldEvents.add(instance);
            instance.start(level);
            instance.sync(level);
        });
        return instance;
    }

    public static void playerJoin(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player.level instanceof ServerLevel level) {
                PlayerDataCapability.getCapability(player).ifPresent(capability -> WorldDataCapability.getCapability(level).ifPresent(worldCapability -> {
                    if (player instanceof ServerPlayer serverPlayer) {
                        for (WorldEventInstance instance : worldCapability.activeWorldEvents) {
                            if (instance.isClientSynced()) {
                                WorldEventInstance.sync(instance, serverPlayer);
                            }
                        }
                    }
                }));
            }
        }
    }

    public static void worldTick(TickEvent.WorldTickEvent event) {
        if (event.phase.equals(TickEvent.Phase.END)) {
            if (!event.world.isClientSide) {
                tick(event.world);
            }
        }
    }

    public static void tick(Level level) {
        WorldDataCapability.getCapability(level).ifPresent(capability -> {
            capability.activeWorldEvents.addAll(capability.inboundWorldEvents);
            capability.inboundWorldEvents.clear();
            Iterator<WorldEventInstance> iterator = capability.activeWorldEvents.iterator();
            while (iterator.hasNext()) {
                WorldEventInstance instance = iterator.next();
                if (instance.discarded) {
                    iterator.remove();
                } else {
                    instance.tick(level);
                }
            }
        });
    }

    public static void serializeNBT(WorldDataCapability capability, CompoundTag tag) {
        tag.putInt("worldEventCount", capability.activeWorldEvents.size());
        for (int i = 0; i < capability.activeWorldEvents.size(); i++) {
            WorldEventInstance instance = capability.activeWorldEvents.get(i);
            CompoundTag instanceTag = new CompoundTag();
            instance.serializeNBT(instanceTag);
            tag.put("worldEvent_" + i, instanceTag);
        }
    }

    public static void deserializeNBT(WorldDataCapability capability, CompoundTag tag) {
        capability.activeWorldEvents.clear();
        int starfallCount = tag.getInt("worldEventCount");
        for (int i = 0; i < starfallCount; i++) {
            CompoundTag instanceTag = tag.getCompound("worldEvent_" + i);
            WorldEventType reader = OrtusWorldEventTypeRegistry.EVENT_TYPES.get(instanceTag.getString("type"));
            WorldEventInstance eventInstance = reader.createInstance(instanceTag);

            capability.activeWorldEvents.add(eventInstance);
        }
    }
}