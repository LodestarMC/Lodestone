package team.lodestar.lodestone.systems.worldevent;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class WorldEventType {

    public final ResourceLocation id;
    public final EventInstanceSupplier supplier;

    public WorldEventType(ResourceLocation id, EventInstanceSupplier supplier) {
        this.id = id;
        this.supplier = supplier;
    }

    public WorldEventInstance createInstance(CompoundTag tag) {
        return supplier.getInstance().deserializeNBT(tag);
    }

    public interface EventInstanceSupplier {
        WorldEventInstance getInstance();
    }
}