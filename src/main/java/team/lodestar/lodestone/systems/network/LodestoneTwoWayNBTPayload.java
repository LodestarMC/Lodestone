package team.lodestar.lodestone.systems.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class LodestoneTwoWayNBTPayload extends LodestoneTwoWayPayload{

    public CompoundTag data;

    public LodestoneTwoWayNBTPayload(FriendlyByteBuf friendlyByteBuf) {
        super(friendlyByteBuf);
        friendlyByteBuf.writeNbt(data);
    }

    public LodestoneTwoWayNBTPayload(CompoundTag tag) {
        this.data = tag;
    }
}
