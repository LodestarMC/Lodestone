package team.lodestar.lodestone.setup;

import team.lodestar.lodestone.network.ClearFireEffectInstancePacket;
import team.lodestar.lodestone.network.SyncWorldEventPacket;
import team.lodestar.lodestone.network.TotemOfUndyingEffectPacket;
import team.lodestar.lodestone.network.interaction.ResetRightClickDelayPacket;
import team.lodestar.lodestone.network.interaction.RightClickEmptyPacket;
import team.lodestar.lodestone.network.interaction.UpdateLeftClickPacket;
import team.lodestar.lodestone.network.interaction.UpdateRightClickPacket;
import team.lodestar.lodestone.network.capability.SyncLodestoneEntityCapabilityPacket;
import team.lodestar.lodestone.network.capability.SyncLodestonePlayerCapabilityPacket;
import team.lodestar.lodestone.network.screenshake.PositionedScreenshakePacket;
import team.lodestar.lodestone.network.screenshake.ScreenshakePacket;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import static team.lodestar.lodestone.LodestoneLib.LODESTONE;
import static team.lodestar.lodestone.LodestoneLib.lodestonePath;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = LODESTONE, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LodestonePacketRegistry {
    public static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel ORTUS_CHANNEL = NetworkRegistry.newSimpleChannel(lodestonePath("main"), () -> LodestonePacketRegistry.PROTOCOL_VERSION, LodestonePacketRegistry.PROTOCOL_VERSION::equals, LodestonePacketRegistry.PROTOCOL_VERSION::equals);

    @SuppressWarnings("UnusedAssignment")
    @SubscribeEvent
    public static void registerPackets(FMLCommonSetupEvent event) {
        int index = 0;
        SyncLodestonePlayerCapabilityPacket.register(ORTUS_CHANNEL, index++);
        SyncLodestoneEntityCapabilityPacket.register(ORTUS_CHANNEL, index++);
        ClearFireEffectInstancePacket.register(ORTUS_CHANNEL, index++);
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