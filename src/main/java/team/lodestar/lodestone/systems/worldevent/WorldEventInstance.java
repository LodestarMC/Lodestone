package team.lodestar.lodestone.systems.worldevent;

import io.netty.buffer.Unpooled;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.ApiStatus;
import team.lodestar.lodestone.network.worldevent.SyncWorldEventPayload;
import team.lodestar.lodestone.registry.common.LodestoneWorldEventTypes;

import java.util.UUID;

/**
 * World events are tickable instanced objects which are saved in a level capability, which means they are unique per dimension.
 * They can exist on the client and are ticked separately.
 */
public abstract class WorldEventInstance {
    public UUID uuid;
    public WorldEventType type;
    public Level level;
    public boolean discarded;
    public boolean dirty;
    public boolean frozen = false;

    public WorldEventInstance(WorldEventType type) {
        if (type == null) throw new IllegalArgumentException("World event type cannot be null");
        this.uuid = UUID.randomUUID();
        this.type = type;
    }

    public abstract void tick(Level level);

    /**
     * Adds additional data during serialization.
     * This method should be overridden to save custom fields.
     *
     * @param tag the CompoundTag to add the additional data to
     */
    protected abstract void addAdditionalSaveData(CompoundTag tag);

    /**
     * Reads additional data during deserialization.
     * This method should be overridden to load custom fields.
     *
     * @param tag the CompoundTag to read the additional data from
     */
    protected abstract void readAdditionalSaveData(CompoundTag tag);

    public void end(Level level) {
        discarded = true;
    }

    /**
     * If the event is dirty, it will be synchronized to the client then set to not dirty.
     */
    public void setDirty() {
        dirty = true;
    }

    /**
     * If the event is frozen, it will not be ticked in {@link #tick(Level)}
     */
    public boolean isFrozen() {
        return frozen;
    }

    public Level getLevel() {
        return level;
    }

    @Deprecated(since = "1.7.0")
    @ApiStatus.Internal
    public CompoundTag serializeNBT(CompoundTag tag) {
        tag.putUUID("uuid", uuid);
        tag.putString("type", type.id.toString());
        tag.putBoolean("discarded", discarded);
        tag.putBoolean("frozen", frozen);
        this.addAdditionalSaveData(tag);
        return tag;
    }

    @Deprecated(since = "1.7.0")
    @ApiStatus.Internal
    public WorldEventInstance deserializeNBT(CompoundTag tag) {
        uuid = tag.getUUID("uuid");
        type = LodestoneWorldEventTypes.WORLD_EVENT_TYPE_REGISTRY.get(ResourceLocation.parse(tag.getString("type")));
        discarded = tag.getBoolean("discarded");
        frozen = tag.getBoolean("frozen");
        this.readAdditionalSaveData(tag);
        return this;
    }

    @ApiStatus.Internal
    public void sync(Level level) {
        if (!level.isClientSide && this.type.isClientSynced()) {
            sync(this);
        }
    }

    @ApiStatus.Internal
    public void start(Level level) {
        this.level = level;
    }

    // Update World Event Data Server -> Client
    @ApiStatus.Internal
    public CompoundTag synchronizeNBT() {
        return serializeNBT(new CompoundTag());
    }

    //Duplicate World Event to Client, Only Call once per world event instance
    @ApiStatus.Internal
    public static <T extends WorldEventInstance> void sync(T instance) {
        var buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeResourceLocation(instance.type.id);
        buf.writeBoolean(true);
        buf.writeNbt(instance.serializeNBT(new CompoundTag()));
        PacketDistributor.sendToServer(new SyncWorldEventPayload(buf));
    }

    //Duplicate World Event to Client, Only Call once per world event instance
    @ApiStatus.Internal
    public static <T extends WorldEventInstance> void sync(T instance, ServerPlayer player) {
        var buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeResourceLocation(instance.type.id);
        buf.writeBoolean(false);
        buf.writeNbt(instance.serializeNBT(new CompoundTag()));
        PacketDistributor.sendToPlayer(player, new SyncWorldEventPayload(buf));
    }
}