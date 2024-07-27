package team.lodestar.lodestone.registry.common;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.network.ClearFireEffectInstancePayload;
import team.lodestar.lodestone.network.TotemOfUndyingPayload;
import team.lodestar.lodestone.network.capability.SyncCapabilityPayload;
import team.lodestar.lodestone.network.screenshake.PositionedScreenshakePayload;
import team.lodestar.lodestone.network.screenshake.ScreenshakePayload;
import team.lodestar.lodestone.network.worldevent.SyncWorldEventPayload;
import team.lodestar.lodestone.network.worldevent.UpdateWorldEventPayload;
import team.lodestar.lodestone.systems.network.*;

import java.util.HashMap;

public class LodestonePayloadRegistry {

    public static final HashMap<String, PayloadNetworkChannel> CHANNEL_MAP = new HashMap<>();

    public static final PayloadNetworkChannel LODESTONE_CHANNEL = new PayloadNetworkChannel(LodestoneLib.LODESTONE);

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");

        LODESTONE_CHANNEL.playToClient(registrar, "totem_of_undying", TotemOfUndyingPayload::new);
        LODESTONE_CHANNEL.playToClient(registrar, "clear_fire_effect", ClearFireEffectInstancePayload::new);
        LODESTONE_CHANNEL.playToClient(registrar, "sync_world_event", SyncWorldEventPayload::new);
        LODESTONE_CHANNEL.playToClient(registrar, "update_world_event", UpdateWorldEventPayload::new);
        LODESTONE_CHANNEL.playToClient(registrar, "screenshake", ScreenshakePayload::new);
        LODESTONE_CHANNEL.playToClient(registrar, "positioned_screenshake", PositionedScreenshakePayload::new);


        LODESTONE_CHANNEL.playBidirectional(registrar, "sync_capability", SyncCapabilityPayload::new);
    }

    /**
     * Network channels function as a database of payload types.
     * Payload Data that extends {@link LodestoneNetworkPayloadData} will use a resource location to first figure out which channel they belong to using the namespace, and the payload type using the path.
     * Lodestone payload data is designed to be extended, see {@link OneSidedPayloadData} and {@link TwoSidedPayloadData}.
     */
    public static class PayloadNetworkChannel {

        private final HashMap<String, CustomPacketPayload.Type<? extends LodestoneNetworkPayloadData>> payloadTypeMap = new HashMap<>();

        public final String namespace;

        public PayloadNetworkChannel(String namespace) {
            this.namespace = namespace;
            CHANNEL_MAP.put(namespace, this);
        }

        public <T extends OneSidedPayloadData> void playToClient(PayloadRegistrar registrar, String name, PayloadDataSupplier<T> payloadSupplier) {
            CustomPacketPayload.Type<T> type = getPayloadType(name);
            registrar.playToClient(type, createCodec(payloadSupplier), OneSidedPayloadData::handle);
        }

        public <T extends OneSidedPayloadData> void playToServer(PayloadRegistrar registrar, String name, PayloadDataSupplier<T> payloadSupplier) {
            CustomPacketPayload.Type<T> type = getPayloadType(name);
            registrar.playToServer(type, createCodec(payloadSupplier), OneSidedPayloadData::handle);
        }

        public <T extends TwoSidedPayloadData> void playBidirectional(PayloadRegistrar registrar, String name, PayloadDataSupplier<T> payloadSupplier) {
            CustomPacketPayload.Type<T> type = getPayloadType(name);
            registrar.playBidirectional(type, createCodec(payloadSupplier), new DirectionalPayloadHandler<>(
                            TwoSidedPayloadData::handleClient,
                            TwoSidedPayloadData::handleServer));
        }

        public <T extends LodestoneNetworkPayloadData> StreamCodec<ByteBuf, T> createCodec(PayloadDataSupplier<T> supplier) {

//            static <B extends ByteBuf, T extends Packet<?>> StreamCodec<B, T> codec(StreamMemberEncoder<B, T> pEncoder, StreamDecoder<B, T> pDecoder) {
//                return StreamCodec.ofMember(pEncoder, pDecoder);
//            }

            return StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8, LodestoneNetworkPayloadData::name,
                    ByteBufCodecs.COMPOUND_TAG, LodestoneNetworkPayloadData::serialize,
                    (id, tag) -> createPayload(supplier, id, tag));
        }

        public final <T extends LodestoneNetworkPayloadData> T createPayload(PayloadDataSupplier<T> supplier, String path, CompoundTag tag) {
            return createPayload(supplier, ResourceLocation.parse(path), tag);
        }

        public <T extends LodestoneNetworkPayloadData> T createPayload(PayloadDataSupplier<T> supplier, ResourceLocation resourceLocation, CompoundTag tag) {
            T payload = supplier.createPayload(resourceLocation);
            payload.deserialize(tag);
            return payload;
        }

        public <T extends LodestoneNetworkPayloadData> CustomPacketPayload.Type<T> getPayloadType(String path) {
            if (payloadTypeMap.containsKey(path)) {
                return (CustomPacketPayload.Type<T>) payloadTypeMap.get(path);
            }
            CustomPacketPayload.Type<T> type = CustomPacketPayload.createType(namespace + "/" + path);
            payloadTypeMap.put(path, type);
            return type;
        }
    }

    public interface PayloadDataSupplier<T extends LodestoneNetworkPayloadData> {
        T createPayload(ResourceLocation resourceLocation);
    }
}
