package com.sammy.ortus.events;

import com.mojang.blaze3d.vertex.PoseStack;
import com.sammy.ortus.OrtusLibClient;
import com.sammy.ortus.capability.PlayerDataCapability;
import com.sammy.ortus.handlers.RenderHandler;
import com.sammy.ortus.handlers.ScreenParticleHandler;
import com.sammy.ortus.handlers.ScreenshakeHandler;
import com.sammy.ortus.handlers.WorldEventHandler;
import com.sammy.ortus.systems.rendering.SuperRenderTypeBuffer;
import com.sammy.ortus.util.AnimationTickHolder;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Option;
import net.minecraft.client.gui.screens.SimpleOptionsSubScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.sammy.ortus.OrtusLib.RANDOM;
import static com.sammy.ortus.setup.OrtusOptionRegistry.OPTIONS;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientRuntimeEvents {

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase.equals(TickEvent.Phase.END)) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.level != null) {
                if (minecraft.isPaused()) {
                    return;
                }
                Camera camera = minecraft.gameRenderer.getMainCamera();
                ScreenParticleHandler.clientTick(event);
                WorldEventHandler.tick(minecraft.level);
                ScreenshakeHandler.clientTick(camera, RANDOM);
                PlayerDataCapability.ClientOnly.clientTick(event);
                ScreenParticleHandler.clientTick(event);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void renderLast(RenderLevelLastEvent event) {
        PoseStack ps = event.getPoseStack();
        SuperRenderTypeBuffer buffer = SuperRenderTypeBuffer.getInstance();
        float partial = AnimationTickHolder.getPartialTicks();
        OrtusLibClient.GHOST_BLOCKS.renderAll(ps, buffer);
        OrtusLibClient.OUTLINER.renderOutlines(ps, buffer, partial);
        RenderHandler.renderLast(event);
        WorldEventHandler.ClientOnly.renderWorldEvents(event);
    }

    @SubscribeEvent
    public static void setupScreen(ScreenEvent.InitScreenEvent.Post event) {
        if (event.getScreen() instanceof SimpleOptionsSubScreen subScreen) {
            subScreen.list.addSmall(OPTIONS.stream().filter(e -> e.canAdd(event)).toArray(Option[]::new));
        }
    }

    @SubscribeEvent
    public static void renderTick(TickEvent.RenderTickEvent event) {
        ScreenParticleHandler.renderParticles(event);
    }
}