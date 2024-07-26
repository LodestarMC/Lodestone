package team.lodestar.lodestone.systems.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class LodestoneServerNBTPayload extends LodestoneServerPayload {
    public CompoundTag data;

    public LodestoneServerNBTPayload(FriendlyByteBuf friendlyByteBuf) {
        super(friendlyByteBuf);
        friendlyByteBuf.writeNbt(data);
    }

    public LodestoneServerNBTPayload(CompoundTag tag) {
        this.data = tag;
    }
}
