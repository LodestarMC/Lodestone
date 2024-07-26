package team.lodestar.lodestone.systems.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.TagNetworkSerialization;

public class LodestoneClientNBTPayload extends LodestoneClientPayload {

    public CompoundTag data;

    public LodestoneClientNBTPayload(FriendlyByteBuf friendlyByteBuf) {
        super(friendlyByteBuf);
        friendlyByteBuf.writeNbt(data);
    }

    public LodestoneClientNBTPayload(CompoundTag tag) {
        this.data = tag;
    }


}
