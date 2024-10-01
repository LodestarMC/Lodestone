package team.lodestar.lodestone.registry.common;

import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.resources.*;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.network.TotemOfUndyingPayload;
import team.lodestar.lodestone.network.screenshake.PositionedScreenshakePayload;
import team.lodestar.lodestone.network.screenshake.ScreenshakePayload;
import team.lodestar.lodestone.network.worldevent.SyncWorldEventPayload;
import team.lodestar.lodestone.network.worldevent.UpdateWorldEventPayload;
import team.lodestar.lodestone.systems.network.*;

import java.util.HashMap;

public class LodestoneNetworkPayloads {

    public static final PayloadRegistryHelper LODESTONE_CHANNEL = new PayloadRegistryHelper(LodestoneLib.LODESTONE);

    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        LODESTONE_CHANNEL.playToClient(registrar, "totem_of_undying", TotemOfUndyingPayload.class, TotemOfUndyingPayload::new);
        LODESTONE_CHANNEL.playToClient(registrar, "sync_world_event", SyncWorldEventPayload.class, SyncWorldEventPayload::new);
        LODESTONE_CHANNEL.playToClient(registrar, "update_world_event", UpdateWorldEventPayload.class, UpdateWorldEventPayload::new);
        LODESTONE_CHANNEL.playToClient(registrar, "screenshake", ScreenshakePayload.class, ScreenshakePayload::new);
        LODESTONE_CHANNEL.playToClient(registrar, "positioned_screenshake", PositionedScreenshakePayload.class, PositionedScreenshakePayload::new);
    }

    /**
     * Network channels function as a database of payload types.
     * Payload Data that extends {@link LodestoneNetworkPayloadData} will use a resource location to first figure out which channel they belong to using the namespace, and the payload type using the path.
     * Lodestone payload data is designed to be extended, see {@link OneSidedPayloadData} and {@link TwoSidedPayloadData}.
     */
    public static class PayloadRegistryHelper {

        public static final HashMap<Class<? extends LodestoneNetworkPayloadData>, CustomPacketPayload.Type<? extends LodestoneNetworkPayloadData>> PAYLOAD_TO_TYPE = new HashMap<>();

        public final String namespace;

        public PayloadRegistryHelper(String namespace) {
            this.namespace = namespace;
        }

        public <T extends OneSidedPayloadData> void playToClient(PayloadRegistrar registrar, String name, Class<T> clazz, PayloadDataSupplier<T> decoder) {
            CustomPacketPayload.Type<T> type = createPayloadType(clazz, name);
            registrar.playToClient(type, createCodec(decoder), OneSidedPayloadData::handle);
        }

        public <T extends OneSidedPayloadData> void playToServer(PayloadRegistrar registrar, String name, Class<T> clazz, PayloadDataSupplier<T> decoder) {
            CustomPacketPayload.Type<T> type = createPayloadType(clazz, name);
            registrar.playToServer(type, createCodec(decoder), OneSidedPayloadData::handle);
        }

        public <T extends TwoSidedPayloadData> void playBidirectional(PayloadRegistrar registrar, String name, Class<T> clazz, PayloadDataSupplier<T> decoder) {
            CustomPacketPayload.Type<T> type = createPayloadType(clazz, name);
            registrar.playBidirectional(type, createCodec(decoder), new DirectionalPayloadHandler<>(
                    TwoSidedPayloadData::handleClient,
                    TwoSidedPayloadData::handleServer));
        }

        public <T extends LodestoneNetworkPayloadData> StreamCodec<FriendlyByteBuf, T> createCodec(PayloadDataSupplier<T> supplier) {
            return StreamCodec.ofMember(encodePacket(), decodePacket(supplier));
        }

        public final <B extends FriendlyByteBuf, T extends LodestoneNetworkPayloadData> StreamMemberEncoder<B, T> encodePacket() {
            return LodestoneNetworkPayloadData::serialize;
        }

        public final <B extends FriendlyByteBuf, T extends LodestoneNetworkPayloadData> StreamDecoder<B, T> decodePacket(PayloadDataSupplier<T> supplier) {
            return byteBuf -> {
                try {
                    return supplier.createPayload(byteBuf);
                } catch (Exception e) {
                    throw new RuntimeException("Couldn't decode payload type from channel " + namespace, e);
                }
            };
        }

        public <T extends LodestoneNetworkPayloadData> CustomPacketPayload.Type<T> createPayloadType(Class<T> clazz, String id) {
            CustomPacketPayload.Type<T> type = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(namespace, id));
            PAYLOAD_TO_TYPE.put(clazz, type);
            return type;
        }

    }

    public interface PayloadDataSupplier<T extends LodestoneNetworkPayloadData> {
        T createPayload(FriendlyByteBuf byteBuf);
    }
}