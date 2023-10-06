package team.lodestar.lodestone.registry.common;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import team.lodestar.lodestone.network.ClearFireEffectInstancePacket;
import team.lodestar.lodestone.network.SyncWorldEventPacket;
import team.lodestar.lodestone.network.TotemOfUndyingEffectPacket;
import team.lodestar.lodestone.network.capability.SyncLodestoneEntityCapabilityPacket;
import team.lodestar.lodestone.network.capability.SyncLodestonePlayerCapabilityPacket;
import team.lodestar.lodestone.network.interaction.ResetRightClickDelayPacket;
import team.lodestar.lodestone.network.interaction.RightClickEmptyPacket;
import team.lodestar.lodestone.network.interaction.UpdateLeftClickPacket;
import team.lodestar.lodestone.network.interaction.UpdateRightClickPacket;
import team.lodestar.lodestone.network.screenshake.PositionedScreenshakePacket;
import team.lodestar.lodestone.network.screenshake.ScreenshakePacket;

import static team.lodestar.lodestone.LodestoneLib.LODESTONE;
import static team.lodestar.lodestone.LodestoneLib.lodestonePath;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = LODESTONE, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LodestonePacketRegistry {
    public static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel LODESTONE_CHANNEL = NetworkRegistry.newSimpleChannel(lodestonePath("main"), () -> LodestonePacketRegistry.PROTOCOL_VERSION, LodestonePacketRegistry.PROTOCOL_VERSION::equals, LodestonePacketRegistry.PROTOCOL_VERSION::equals);

    @SuppressWarnings("UnusedAssignment")
    @SubscribeEvent
    public static void registerPackets(FMLCommonSetupEvent event) {
        int index = 0;
        SyncLodestonePlayerCapabilityPacket.register(LODESTONE_CHANNEL, index++);
        SyncLodestoneEntityCapabilityPacket.register(LODESTONE_CHANNEL, index++);
        ClearFireEffectInstancePacket.register(LODESTONE_CHANNEL, index++);
        ScreenshakePacket.register(LODESTONE_CHANNEL, index++);
        PositionedScreenshakePacket.register(LODESTONE_CHANNEL, index++);
        SyncWorldEventPacket.register(LODESTONE_CHANNEL, index++);
        RightClickEmptyPacket.register(LODESTONE_CHANNEL, index++);
        UpdateLeftClickPacket.register(LODESTONE_CHANNEL, index++);
        UpdateRightClickPacket.register(LODESTONE_CHANNEL, index++);
        ResetRightClickDelayPacket.register(LODESTONE_CHANNEL, index++);
        TotemOfUndyingEffectPacket.register(LODESTONE_CHANNEL, index++);
    }
}