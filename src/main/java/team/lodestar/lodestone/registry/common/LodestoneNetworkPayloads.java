package team.lodestar.lodestone.registry.common;

import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.network.protocol.common.custom.*;
import net.minecraft.resources.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.*;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.systems.network.*;
import team.lodestar.lodestone.common.network.ScreenshakePayload;
import team.lodestar.lodestone.common.network.worldevent.SyncWorldEventPayload;
import team.lodestar.lodestone.common.network.worldevent.UpdateWorldEventPayload;
import team.lodestar.lodestone.systems.network.particle.NetworkedParticleEffectPayload;

import java.util.HashMap;

@EventBusSubscriber()
public class LodestoneNetworkPayloads {

    public static final LodestonePayloadRegistryHelper LODESTONE_CHANNEL = new LodestonePayloadRegistryHelper(LodestoneLib.LODESTONE);

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        var registrar = event.registrar("1");

        LODESTONE_CHANNEL.playToClient(registrar, "sync_world_event", SyncWorldEventPayload.class, SyncWorldEventPayload::new);
        LODESTONE_CHANNEL.playToClient(registrar, "update_world_event", UpdateWorldEventPayload.class, UpdateWorldEventPayload::new);
        LODESTONE_CHANNEL.playToClient(registrar, "screenshake", ScreenshakePayload.class, ScreenshakePayload::new);
        LODESTONE_CHANNEL.playToClient(registrar, "particle_effect", NetworkedParticleEffectPayload.class, NetworkedParticleEffectPayload::new);

    }

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

    }

    public interface PayloadDataSupplier<T extends LodestoneNetworkPayloadData> {
        T deserializePayload(RegistryFriendlyByteBuf byteBuf);
    }
}