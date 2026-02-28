package team.lodestar.lodestone.core.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.StreamDecoder;
import net.minecraft.network.codec.StreamMemberEncoder;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.HashMap;


//TODO: This was all written as a way to ignore the Codec-ification of packets.
// By now, I think Codec based packets are pretty cool. Having some support for that would be nice.

/**
 * Network channels function as a database of payload types.
 * Payload Data that extends {@link LodestoneNetworkPayloadData} will use a resource location to first figure out which channel they belong to using the namespace, and the payload type using the path.
 * Lodestone payload data is designed to be extended, see {@link OneSidedPayloadData} and {@link TwoSidedPayloadData}.
 */
public record LodestonePayloadRegistryHelper(String namespace) {

    public static final HashMap<Class<? extends LodestoneNetworkPayloadData>, CustomPacketPayload.Type<? extends LodestoneNetworkPayloadData>> PAYLOAD_TO_TYPE = new HashMap<>();

    public <T extends OneSidedPayloadData> void playToClient(PayloadRegistrar registrar, String name, Class<T> clazz, PayloadDataSupplier<T> decoder) {
        var type = createPayloadType(clazz, name);
        var codec = createStreamCodec(decoder);
        registrar.playToClient(type, codec, OneSidedPayloadData::handle);
    }

    public <T extends OneSidedPayloadData> void playToServer(PayloadRegistrar registrar, String name, Class<T> clazz, PayloadDataSupplier<T> decoder) {
        var type = createPayloadType(clazz, name);
        var codec = createStreamCodec(decoder);
        registrar.playToServer(type, codec, OneSidedPayloadData::handle);
    }

    public <T extends TwoSidedPayloadData> void playBidirectional(PayloadRegistrar registrar, String name, Class<T> clazz, PayloadDataSupplier<T> decoder) {
        var type = createPayloadType(clazz, name);

        var codec = createStreamCodec(decoder);
        registrar.playBidirectional(type, codec, new DirectionalPayloadHandler<>(
                TwoSidedPayloadData::handleClient,
                TwoSidedPayloadData::handleServer));
    }

    public <T extends LodestoneNetworkPayloadData> StreamCodec<RegistryFriendlyByteBuf, T> createStreamCodec(PayloadDataSupplier<T> supplier) {
        return StreamCodec.ofMember(serializePayload(), deserializePayload(supplier));
    }

    public <B extends RegistryFriendlyByteBuf, T extends LodestoneNetworkPayloadData> StreamMemberEncoder<B, T> serializePayload() {
        return LodestoneNetworkPayloadData::serialize;
    }

    public <B extends RegistryFriendlyByteBuf, T extends LodestoneNetworkPayloadData> StreamDecoder<B, T> deserializePayload(PayloadDataSupplier<T> supplier) {
        return byteBuf -> {
            try {
                return supplier.deserializePayload(byteBuf);
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
    
    public interface PayloadDataSupplier<T extends LodestoneNetworkPayloadData> {
        T deserializePayload(RegistryFriendlyByteBuf byteBuf);
    }

}
