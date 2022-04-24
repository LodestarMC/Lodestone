package com.sammy.ortus.events;

import com.sammy.ortus.handlers.RenderHandler;
import com.sammy.ortus.handlers.ScreenParticleHandler;
import com.sammy.ortus.helpers.DataHelper;
import com.sammy.ortus.registry.OrtusParticles;
import com.sammy.ortus.registry.OrtusScreenParticles;
import com.sammy.ortus.registry.worldevent.OrtusWorldEventRenderers;
import com.sammy.ortus.systems.block.OrtusBlockProperties;
import com.sammy.ortus.systems.rendering.particle.screen.emitter.ItemParticleEmitter;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashSet;
import java.util.Set;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetupEvents {

    @SubscribeEvent
    public static void registerParticleFactory(ParticleFactoryRegisterEvent event) {
        OrtusParticles.registerParticleFactory(event);
        OrtusScreenParticles.registerParticleFactory(event);
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        RenderHandler.onClientSetup(event);
        ScreenParticleHandler.registerParticleEmitters(event);
        OrtusBlockProperties.setRenderLayers(event);
    }
}