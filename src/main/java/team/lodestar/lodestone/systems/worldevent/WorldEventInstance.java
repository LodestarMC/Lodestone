package team.lodestar.lodestone.systems.worldevent;

import team.lodestar.lodestone.registry.common.LodestonePacketRegistry;
import team.lodestar.lodestone.network.SyncWorldEventPacket;
import team.lodestar.lodestone.registry.common.LodestoneWorldEventTypeRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;

import java.util.UUID;

/**
 * World events are tickable instanced objects which are saved in a level capability, which means they are unique per dimension.
 * They can exist on the client and are ticked separately.
 */
public abstract class WorldEventInstance {
    public UUID uuid; //TODO: figure out why this is here.
    public WorldEventType type;
    public boolean discarded;

    public WorldEventInstance(WorldEventType type) {
        this.uuid = UUID.randomUUID();
        this.type = type;
    }

    /**
     * Syncs the world event to all players.
     */
    public void sync(Level level) {
        if (!level.isClientSide && isClientSynced()) {
            sync(this);
        }
    }

    /**
     * Should this event exist on the client? It will be automatically synced in {@link #sync(Level)}
     */
    public boolean isClientSynced() {
        return false;
    }

    public void start(Level level) {
    }

    public void tick(Level level) {

    }

    public void end(Level level) {
        discarded = true;
    }

    public CompoundTag serializeNBT(CompoundTag tag) {
        tag.putUUID("uuid", uuid);
        tag.putString("type", type.id);
        tag.putBoolean("discarded", discarded);
        return tag;
    }

    public WorldEventInstance deserializeNBT(CompoundTag tag) {
        uuid = tag.getUUID("uuid");
        type = LodestoneWorldEventTypeRegistry.EVENT_TYPES.get(tag.getString("type"));
        discarded = tag.getBoolean("discarded");
        return this;
    }

    public static <T extends WorldEventInstance> void sync(T instance) {
        LodestonePacketRegistry.LODESTONE_CHANNEL.send(PacketDistributor.ALL.noArg(), new SyncWorldEventPacket(instance.type.id, true, instance.serializeNBT(new CompoundTag())));
    }

    public static <T extends WorldEventInstance> void sync(T instance, ServerPlayer player) {
        LodestonePacketRegistry.LODESTONE_CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new SyncWorldEventPacket(instance.type.id, false, instance.serializeNBT(new CompoundTag())));
    }
}