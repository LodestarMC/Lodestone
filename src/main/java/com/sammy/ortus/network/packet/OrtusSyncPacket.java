package com.sammy.ortus.network.packet;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public abstract class OrtusSyncPacket extends OrtusPacket{
    public OrtusSyncPacket(CompoundTag tag) {
        super(tag);
    }

    @Override
    public void clientExecute(Supplier<NetworkEvent.Context> context) {
        modifyClient(context,modifyTag(getServerTag(context)));
    }

    @Override
    public void serverExecute(Supplier<NetworkEvent.Context> context) {
        modifyServer(context,modifyTag(getClientTag(context)));
    }

    //Sync methods
    public CompoundTag modifyTag(CompoundTag inTag){
        for(String key: data.getAllKeys()){
            if(inTag.contains(key)){
                inTag.put(key, Objects.requireNonNull(data.get(key)));
            }
        }
        return inTag;
    }
    public abstract CompoundTag getClientTag(Supplier<NetworkEvent.Context> context);
    public abstract CompoundTag getServerTag(Supplier<NetworkEvent.Context> context);

    public abstract void modifyClient(Supplier<NetworkEvent.Context> context, CompoundTag tag);
    public abstract void modifyServer(Supplier<NetworkEvent.Context> context, CompoundTag tag);
}
