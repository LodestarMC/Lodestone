package com.sammy.ortus.setup;

import com.sammy.ortus.network.*;
import com.sammy.ortus.network.interaction.ResetRightClickDelayPacket;
import com.sammy.ortus.network.interaction.RightClickEmptyPacket;
import com.sammy.ortus.network.interaction.UpdateLeftClickPacket;
import com.sammy.ortus.network.interaction.UpdateRightClickPacket;
import com.sammy.ortus.network.packet.SyncOrtusEntityCapabilityPacket;
import com.sammy.ortus.network.packet.SyncOrtusPlayerCapabilityPacket;
import com.sammy.ortus.network.screenshake.PositionedScreenshakePacket;
import com.sammy.ortus.network.screenshake.ScreenshakePacket;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import static com.sammy.ortus.OrtusLib.ORTUS;
import static com.sammy.ortus.OrtusLib.ortusPrefix;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = ORTUS, bus = Mod.EventBusSubscriber.Bus.MOD)
public class OrtusPacketRegistry {
    public static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel ORTUS_CHANNEL = NetworkRegistry.newSimpleChannel(ortusPrefix("main"), () -> OrtusPacketRegistry.PROTOCOL_VERSION, OrtusPacketRegistry.PROTOCOL_VERSION::equals, OrtusPacketRegistry.PROTOCOL_VERSION::equals);

    @SuppressWarnings("UnusedAssignment")
    @SubscribeEvent
    public static void registerPackets(FMLCommonSetupEvent event) {
        int index = 0;
        SyncOrtusPlayerCapabilityPacket.register(ORTUS_CHANNEL, index++);
        SyncOrtusEntityCapabilityPacket.register(ORTUS_CHANNEL, index++);

        ScreenshakePacket.register(ORTUS_CHANNEL, index++);
        PositionedScreenshakePacket.register(ORTUS_CHANNEL, index++);
        SyncWorldEventPacket.register(ORTUS_CHANNEL, index++);
        RightClickEmptyPacket.register(ORTUS_CHANNEL, index++);
        UpdateLeftClickPacket.register(ORTUS_CHANNEL, index++);
        UpdateRightClickPacket.register(ORTUS_CHANNEL, index++);
        ResetRightClickDelayPacket.register(ORTUS_CHANNEL, index++);
        TotemOfUndyingEffectPacket.register(ORTUS_CHANNEL, index++);
    }
}