package team.lodestar.lodestone.systems.worldevent;

import net.minecraft.nbt.CompoundTag;

public class WorldEventType {

    public final String id;
    public final EventInstanceSupplier supplier;

    public WorldEventType(String id, EventInstanceSupplier supplier) {
        this.id = id;
        this.supplier = supplier;
    }

    public WorldEventInstance createInstance(CompoundTag tag) {
        return supplier.getInstance(tag);
    }

    public interface EventInstanceSupplier {
        WorldEventInstance getInstance(CompoundTag tag);
    }
}