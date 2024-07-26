package team.lodestar.lodestone.systems.network;
/*
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public abstract class LodestoneTwoWayNBTPacket extends LodestoneTwoWayPacket {
    protected CompoundTag data;

    public LodestoneTwoWayNBTPacket(CompoundTag data) {
        this.data = data;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeNbt(data);
    }

    @Override
    public final void serverExecute(Supplier<NetworkEvent.Context> context) {
        serverExecute(context, data);
    }

    @Override
    public final void clientExecute(Supplier<NetworkEvent.Context> context) {
        clientExecute(context, data);
    }

    public void serverExecute(Supplier<NetworkEvent.Context> context, CompoundTag data) {
    }

    @OnlyIn(Dist.CLIENT)
    public void clientExecute(Supplier<NetworkEvent.Context> context, CompoundTag data) {
    }
}

 */