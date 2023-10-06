package team.lodestar.lodestone.handlers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import team.lodestar.lodestone.capability.LodestonePlayerDataCapability;
import team.lodestar.lodestone.capability.LodestoneWorldDataCapability;
import team.lodestar.lodestone.setup.worldevent.LodestoneWorldEventRendererRegistry;
import team.lodestar.lodestone.setup.worldevent.LodestoneWorldEventTypeRegistry;
import team.lodestar.lodestone.systems.worldevent.WorldEventInstance;
import team.lodestar.lodestone.systems.worldevent.WorldEventRenderer;
import team.lodestar.lodestone.systems.worldevent.WorldEventType;

import java.util.Iterator;

public class WorldEventHandler {

    public static class ClientOnly {
        public static void renderWorldEvents(PoseStack stack, float partialTicks) {
            LodestoneWorldDataCapability.getCapabilityOptional(Minecraft.getInstance().level).ifPresent(capability -> {
                for (WorldEventInstance instance : capability.activeWorldEvents) {
                    WorldEventRenderer<WorldEventInstance> renderer = LodestoneWorldEventRendererRegistry.RENDERERS.get(instance.type);
                    if (renderer != null) {
                        if (renderer.canRender(instance)) {
                            renderer.render(instance, stack, RenderHandler.DELAYED_RENDER, partialTicks);
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
        LodestoneWorldDataCapability.getCapabilityOptional(level).ifPresent(capability -> {
            capability.inboundWorldEvents.add(instance);
            if (shouldStart) {
                instance.start(level);
            }
            instance.sync(level);
        });
        return instance;
    }

    public static void playerJoin(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player.level() instanceof ServerLevel level) {
                LodestonePlayerDataCapability.getCapabilityOptional(player).ifPresent(capability -> LodestoneWorldDataCapability.getCapabilityOptional(level).ifPresent(worldCapability -> {
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

    public static void worldTick(TickEvent.LevelTickEvent event) {
        if (event.phase.equals(TickEvent.Phase.END)) {
            if (!event.level.isClientSide) {
                tick(event.level);
            }
        }
    }

    public static void tick(Level level) {
        LodestoneWorldDataCapability.getCapabilityOptional(level).ifPresent(c -> {
            c.activeWorldEvents.addAll(c.inboundWorldEvents);
            c.inboundWorldEvents.clear();
            Iterator<WorldEventInstance> iterator = c.activeWorldEvents.iterator();
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

    public static void serializeNBT(LodestoneWorldDataCapability capability, CompoundTag tag) {
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

    public static void deserializeNBT(LodestoneWorldDataCapability capability, CompoundTag tag) {
        capability.activeWorldEvents.clear();
        CompoundTag worldTag = tag.getCompound("worldEventData");
        int worldEventCount = worldTag.getInt("worldEventCount");
        for (int i = 0; i < worldEventCount; i++) {
            CompoundTag instanceTag = worldTag.getCompound("worldEvent_" + i);
            WorldEventType reader = LodestoneWorldEventTypeRegistry.EVENT_TYPES.get(instanceTag.getString("type"));
            WorldEventInstance eventInstance = reader.createInstance(instanceTag);
            capability.activeWorldEvents.add(eventInstance);
        }
    }
}