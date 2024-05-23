package team.lodestar.lodestone.events;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.handlers.ThrowawayBlockDataHandler;
import team.lodestar.lodestone.handlers.screenparticle.ParticleEmitterHandler;
import team.lodestar.lodestone.registry.common.particle.*;

public class ClientSetupEvents {


    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {

    }
}