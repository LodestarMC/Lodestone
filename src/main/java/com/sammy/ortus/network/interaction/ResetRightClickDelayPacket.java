package com.sammy.ortus.network.interaction;

import com.sammy.ortus.systems.network.OrtusClientPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class ResetRightClickDelayPacket extends OrtusClientPacket {

    @OnlyIn(value = Dist.CLIENT)
    @Override
    public void execute(Supplier<NetworkEvent.Context> context) {
        Minecraft.getInstance().rightClickDelay = 0;
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, ResetRightClickDelayPacket.class, ResetRightClickDelayPacket::encode, ResetRightClickDelayPacket::decode, ResetRightClickDelayPacket::handle);
    }

    public static ResetRightClickDelayPacket decode(FriendlyByteBuf buf) {
        return new ResetRightClickDelayPacket();
    }
}