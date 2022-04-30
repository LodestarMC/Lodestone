package com.sammy.ortus.setup;

import com.sammy.ortus.network.*;
import com.sammy.ortus.network.interaction.ResetRightClickDelayPacket;
import com.sammy.ortus.network.interaction.RightClickEmptyPacket;
import com.sammy.ortus.network.interaction.UpdateLeftClickPacket;
import com.sammy.ortus.network.interaction.UpdateRightClickPacket;
import com.sammy.ortus.network.screenshake.PositionedScreenshakePacket;
import com.sammy.ortus.network.screenshake.ScreenshakePacket;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import static com.sammy.ortus.OrtusLib.ORTUS;
import static com.sammy.ortus.OrtusLib.prefix;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = ORTUS, bus = Mod.EventBusSubscriber.Bus.MOD)
public class OrtusPacketRegistry {
    public static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(prefix("main"), () -> OrtusPacketRegistry.PROTOCOL_VERSION, OrtusPacketRegistry.PROTOCOL_VERSION::equals, OrtusPacketRegistry.PROTOCOL_VERSION::equals);

    @SuppressWarnings("UnusedAssignment")
    @SubscribeEvent
    public static void registerPackets(FMLCommonSetupEvent event) {
        int index = 0;
        ScreenshakePacket.register(INSTANCE, index++);
        PositionedScreenshakePacket.register(INSTANCE, index++);
        SyncWorldEventPacket.register(INSTANCE, index++);
        SyncPlayerCapabilityDataPacket.register(INSTANCE, index++);
        SyncPlayerCapabilityDataServerPacket.register(INSTANCE, index++);
        SyncEntityCapabilityDataPacket.register(INSTANCE, index++);
        RightClickEmptyPacket.register(INSTANCE, index++);
        UpdateLeftClickPacket.register(INSTANCE, index++);
        UpdateRightClickPacket.register(INSTANCE, index++);
        ResetRightClickDelayPacket.register(INSTANCE, index++);
        TotemOfUndyingEffectPacket.register(INSTANCE, index++);
    }
}