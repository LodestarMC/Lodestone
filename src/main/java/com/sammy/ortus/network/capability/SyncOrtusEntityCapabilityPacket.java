package com.sammy.ortus.network.capability;

import com.sammy.ortus.capability.OrtusEntityDataCapability;
import com.sammy.ortus.systems.network.OrtusTwoWayNBTPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

@SuppressWarnings("ConstantConditions")
public class SyncOrtusEntityCapabilityPacket extends OrtusTwoWayNBTPacket {

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
    public void clientExecute(Supplier<NetworkEvent.Context> context, CompoundTag data) {
        Entity entity = Minecraft.getInstance().level.getEntity(entityID);
        OrtusEntityDataCapability.getCapabilityOptional(entity).ifPresent(c -> c.deserializeNBT(data));
    }

    @Override
    public void serverExecute(Supplier<NetworkEvent.Context> context, CompoundTag data) {
        Entity entity = context.get().getSender().level.getEntity(entityID);
        OrtusEntityDataCapability.getCapabilityOptional(entity).ifPresent(c -> c.deserializeNBT(data));
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, SyncOrtusEntityCapabilityPacket.class, SyncOrtusEntityCapabilityPacket::encode, SyncOrtusEntityCapabilityPacket::decode, SyncOrtusEntityCapabilityPacket::handle);
    }

    public static SyncOrtusEntityCapabilityPacket decode(FriendlyByteBuf buf) {
        return new SyncOrtusEntityCapabilityPacket(buf.readNbt());
    }
}