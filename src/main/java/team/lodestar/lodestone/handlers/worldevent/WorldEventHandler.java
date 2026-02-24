package team.lodestar.lodestone.handlers.worldevent;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import team.lodestar.lodestone.handlers.events.types.worldevent.*;
import team.lodestar.lodestone.common.network.worldevent.UpdateWorldEventPayload;
import team.lodestar.lodestone.registry.common.LodestoneAttachmentTypes;
import team.lodestar.lodestone.systems.worldevent.WorldEventInstance;

import java.util.Iterator;

public class WorldEventHandler {

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
                    PacketDistributor.sendToAllPlayers(new UpdateWorldEventPayload(instance));
                    instance.dirty = false;
                }
            }
        }
    }
}