package com.sammy.ortus.network.packet;

import com.sammy.ortus.capability.EntityDataCapability;
import com.sammy.ortus.systems.network.OrtusSyncPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@SuppressWarnings("ConstantConditions")
public class SyncOrtusEntityCapabilityPacket extends OrtusSyncPacket {
    public static final String ENTITY_ID = "entity_id";

    private final int entityID;

    public SyncOrtusEntityCapabilityPacket(CompoundTag tag) {
        super(tag);
        this.entityID = tag.getInt(ENTITY_ID);
    }

    public SyncOrtusEntityCapabilityPacket(int id, CompoundTag tag) {
        super(handleTag(id, tag));
        this.entityID = id;
    }

    public static CompoundTag handleTag(int id, CompoundTag tag) {
        tag.putInt(ENTITY_ID, id);
        return tag;
    }

    @Override
    public void modifyClient(Supplier<NetworkEvent.Context> context, CompoundTag tag) {
        Entity entity = Minecraft.getInstance().level.getEntity(entityID);
        EntityDataCapability.getCapability(entity).ifPresent(c -> c.deserializeNBT(tag));
    }

    @Override
    public void modifyServer(Supplier<NetworkEvent.Context> context, CompoundTag tag) {
        Entity entity = context.get().getSender().level.getEntity(entityID);
        EntityDataCapability.getCapability(entity).ifPresent(c -> c.deserializeNBT(tag));
    }
}