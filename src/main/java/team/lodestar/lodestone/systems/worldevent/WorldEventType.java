package team.lodestar.lodestone.systems.worldevent;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class WorldEventType {

    public final ResourceLocation id;
    public final EventInstanceSupplier supplier;
    public final boolean clientSynced;


    /**
     * @param id           The id of the event type
     * @param supplier     The supplier for the event instance
     * @param clientSynced Should this event exist on the client? It will be automatically synced in {@link WorldEventInstance#sync(net.minecraft.world.level.Level)}
     */
    public WorldEventType(ResourceLocation id, EventInstanceSupplier supplier, boolean clientSynced) {
        this.id = id;
        this.supplier = supplier;
        this.clientSynced = clientSynced;
    }

    public WorldEventType(ResourceLocation id, EventInstanceSupplier supplier) {
        this(id, supplier, false);
    }

    public boolean isClientSynced() {
        return clientSynced;
    }

    public WorldEventInstance createInstance(CompoundTag tag) {
        return supplier.getInstance().deserializeNBT(tag);
    }

    public interface EventInstanceSupplier {
        WorldEventInstance getInstance();
    }
}