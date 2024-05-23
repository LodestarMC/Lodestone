package team.lodestar.lodestone.registry.common;

import me.pepperbell.simplenetworking.SimpleChannel;
import team.lodestar.lodestone.LodestoneLib;
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
public class LodestonePacketRegistry {
    public static final String PROTOCOL_VERSION = "1";
    //public static SimpleChannel LODESTONE_CHANNEL = NetworkRegistry.newSimpleChannel(lodestonePath("main"), () -> LodestonePacketRegistry.PROTOCOL_VERSION, LodestonePacketRegistry.PROTOCOL_VERSION::equals, LodestonePacketRegistry.PROTOCOL_VERSION::equals);
    public static final SimpleChannel LODESTONE_CHANNEL = new SimpleChannel(LodestoneLib.lodestonePath("main"));

    @SuppressWarnings("UnusedAssignment")
    @SubscribeEvent
    public static void registerPackets(FMLCommonSetupEvent event) {
        int index = 0;

        //TwoWay
        LODESTONE_CHANNEL.registerC2SPacket(SyncLodestonePlayerCapabilityPacket.class, index++);
        LODESTONE_CHANNEL.registerS2CPacket(SyncLodestonePlayerCapabilityPacket.class, index++);

        LODESTONE_CHANNEL.registerC2SPacket(SyncLodestoneEntityCapabilityPacket.class, index++);
        LODESTONE_CHANNEL.registerS2CPacket(SyncLodestoneEntityCapabilityPacket.class, index++);

        //S2C
        LODESTONE_CHANNEL.registerS2CPacket(ClearFireEffectInstancePacket.class, index++);
        LODESTONE_CHANNEL.registerS2CPacket(ScreenshakePacket.class, index++);
        LODESTONE_CHANNEL.registerS2CPacket(PositionedScreenshakePacket.class, index++);
        LODESTONE_CHANNEL.registerS2CPacket(SyncWorldEventPacket.class, index++);

        //C2S
        LODESTONE_CHANNEL.registerC2SPacket(RightClickEmptyPacket.class, index++);
        LODESTONE_CHANNEL.registerC2SPacket(UpdateLeftClickPacket.class, index++);
        LODESTONE_CHANNEL.registerC2SPacket(UpdateRightClickPacket.class, index++);
        LODESTONE_CHANNEL.registerS2CPacket(ResetRightClickDelayPacket.class, index++);
        LODESTONE_CHANNEL.registerS2CPacket(TotemOfUndyingEffectPacket.class, index++);
    }
}