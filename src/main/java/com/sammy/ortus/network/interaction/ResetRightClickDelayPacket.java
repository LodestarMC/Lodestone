package com.sammy.ortus.network.interaction;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class ResetRightClickDelayPacket {

    public ResetRightClickDelayPacket() {
    }

    public static ResetRightClickDelayPacket decode(FriendlyByteBuf buf) {
        return new ResetRightClickDelayPacket();
    }

    public void encode(FriendlyByteBuf buf) {
    }

    public void execute(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                ClientOnly.resetDelay();
            }
        });
        context.get().setPacketHandled(true);
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, ResetRightClickDelayPacket.class, ResetRightClickDelayPacket::encode, ResetRightClickDelayPacket::decode, ResetRightClickDelayPacket::execute);
    }

    public static class ClientOnly {
        public static void resetDelay() {
            Minecraft.getInstance().rightClickDelay = 0;
        }
    }
}