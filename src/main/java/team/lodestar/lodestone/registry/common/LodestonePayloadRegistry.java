package team.lodestar.lodestone.registry.common;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import team.lodestar.lodestone.systems.network.LodestoneClientNBTPayload;
import team.lodestar.lodestone.systems.network.LodestoneClientPayload;
import team.lodestar.lodestone.systems.network.LodestoneTwoWayPayload;

public class LodestonePayloadRegistry {


    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        // Sets the current network version
        final PayloadRegistrar registrar = event.registrar("1");

        registrar.playToClient(
                LodestoneClientNBTPayload.TYPE,
                LodestoneClientNBTPayload.STREAM_CODEC,
                LodestoneClientNBTPayload::handle
        );

        registrar.playBidirectional(
                LodestoneTwoWayPayload.TYPE,
                LodestoneTwoWayPayload.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        LodestoneTwoWayPayload.Client::handleClient,
                        LodestoneTwoWayPayload.Server::handleServer
                )
        );

    }
}
