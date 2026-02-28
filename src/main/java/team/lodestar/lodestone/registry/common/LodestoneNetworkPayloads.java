package team.lodestar.lodestone.registry.common;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.*;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.systems.network.*;
import team.lodestar.lodestone.common.network.ScreenshakePayload;
import team.lodestar.lodestone.common.network.worldevent.SyncWorldEventPayload;
import team.lodestar.lodestone.common.network.worldevent.UpdateWorldEventPayload;
import team.lodestar.lodestone.systems.network.particle.NetworkedParticleEffectPayload;

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
}