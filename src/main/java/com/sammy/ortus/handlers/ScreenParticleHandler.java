package com.sammy.ortus.handlers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Matrix4f;
import com.sammy.ortus.compability.JeiCompat;
import com.sammy.ortus.helpers.DataHelper;
import com.sammy.ortus.systems.rendering.particle.screen.GenericScreenParticle;
import com.sammy.ortus.systems.rendering.particle.screen.ScreenParticleOptions;
import com.sammy.ortus.systems.rendering.particle.screen.ScreenParticleType;
import com.sammy.ortus.systems.rendering.particle.screen.base.ScreenParticle;
import com.sammy.ortus.systems.rendering.particle.screen.base.ScreenParticle.RenderOrder;
import com.sammy.ortus.systems.rendering.particle.screen.emitter.ItemParticleEmitter;
import com.sammy.ortus.systems.rendering.particle.screen.emitter.ParticleEmitter;
import mezz.jei.api.runtime.IRecipesGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.debug.GameModeSwitcherScreen;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.*;

import static com.sammy.ortus.config.ClientConfig.ENABLE_SCREEN_PARTICLES;
import static com.sammy.ortus.systems.rendering.particle.screen.base.ScreenParticle.RenderOrder.*;

/**
 * A handler for screen particles.
 * Particles are spawned during rendering once per tick.
 * We also track all present ItemStacks on the screen to allow our particles to more optimally follow a given ItemStacks position
 * Use {@link ScreenParticleHandler#addParticle(ScreenParticleOptions, double, double, double, double)} to create a screen particle, which will then be ticked.
 * A ScreenParticles {@link RenderOrder} decides when exactly it is rendered, different mixins will render different render orders.
 */
public class ScreenParticleHandler {

    public static Map<Pair<ParticleRenderType, RenderOrder>, ArrayList<ScreenParticle>> PARTICLES = new HashMap<>();
    public static ArrayList<StackTracker> RENDERED_STACKS = new ArrayList<>();
    public static Map<Item, ParticleEmitter> EMITTERS = new HashMap<>();
    public static final Tesselator TESSELATOR = new Tesselator();
    public static boolean canSpawnParticles;
    public static boolean renderingHotbar;

    public static void registerParticleEmitters(FMLClientSetupEvent event) {
        DataHelper.takeAll(new ArrayList<>(ForgeRegistries.ITEMS.getValues()), i -> i instanceof ItemParticleEmitter).forEach(i -> {
                    ItemParticleEmitter emitter = (ItemParticleEmitter) i;
                    ScreenParticleHandler.registerItemParticleEmitter(i, emitter::particleTick);
                }
        );
    }
    public static void clientTick(TickEvent.ClientTickEvent event) {
        if (!ENABLE_SCREEN_PARTICLES.getConfigValue()) {
            return;
        }
        PARTICLES.forEach((pair, particles) -> {
            Iterator<ScreenParticle> iterator = particles.iterator();
            while (iterator.hasNext()) {
                ScreenParticle particle = iterator.next();
                particle.tick();
                if (!particle.isAlive()) {
                    iterator.remove();
                }
            }
        });
        canSpawnParticles = true;
    }

    public static void renderItem(ItemStack stack) {
        if (!ENABLE_SCREEN_PARTICLES.getConfigValue()) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level != null && minecraft.player != null) {
            if (minecraft.isPaused()) {
                return;
            }
            if (!stack.isEmpty()) {
                ParticleEmitter emitter = ScreenParticleHandler.EMITTERS.get(stack.getItem());
                if (emitter != null) {
                    PoseStack posestack = RenderSystem.getModelViewStack();
                    RenderOrder renderOrder = AFTER_EVERYTHING;
                    Screen screen = minecraft.screen;
                    if (screen != null) {
                        if (!JeiCompat.LOADED || !JeiCompat.LoadedOnly.isRecipesUi(screen)) {
                            renderOrder = BEFORE_TOOLTIPS;
                        }
                        if (renderingHotbar) {
                            renderOrder = BEFORE_UI;
                        }
                    }
                    Matrix4f last = posestack.last().pose();
                    float x = last.m03;
                    float y = last.m13;
                    if (canSpawnParticles) {
                        emitter.tick(stack, x, y, renderOrder);
                    }
                    RENDERED_STACKS.add(new StackTracker(stack, renderOrder, x, y));
                }
            }
        }
    }

    public static void renderParticles(TickEvent.RenderTickEvent event) {
        if (event.phase.equals(TickEvent.Phase.END)) {
            Screen screen = Minecraft.getInstance().screen;
            if (JeiCompat.LOADED && JeiCompat.LoadedOnly.isRecipesUi(screen)) {
                renderParticles(AFTER_EVERYTHING);
            }
            if (screen == null || screen instanceof ChatScreen || screen instanceof GameModeSwitcherScreen) {
                renderParticles(AFTER_EVERYTHING, BEFORE_UI);
            }
            RENDERED_STACKS.clear();
            canSpawnParticles = false;
        }
    }

    public static void renderParticles(RenderOrder... renderOrders) {
        if (!ENABLE_SCREEN_PARTICLES.getConfigValue()) {
            return;
        }
        PARTICLES.forEach((pair, particles) -> {
            ParticleRenderType type = pair.getFirst();
            if (Arrays.stream(renderOrders).anyMatch(o -> o.equals(pair.getSecond()))) {
                type.begin(TESSELATOR.getBuilder(), Minecraft.getInstance().textureManager);
                for (ScreenParticle next : particles) {
                    if (next instanceof GenericScreenParticle genericScreenParticle) {
                        genericScreenParticle.trackStack();
                    }
                    next.render(TESSELATOR.getBuilder());
                }
                type.end(TESSELATOR);
            }
        });
    }

    @SuppressWarnings("ALL")
    public static <T extends ScreenParticleOptions> ScreenParticle addParticle(T options, double pX, double pY, double pXSpeed, double pYSpeed) {
        Minecraft minecraft = Minecraft.getInstance();
        ScreenParticleType<T> type = (ScreenParticleType<T>) options.type;
        ScreenParticleType.ParticleProvider<T> provider = type.provider;
        ScreenParticle particle = provider.createParticle(minecraft.level, options, pX, pY, pXSpeed, pYSpeed);
        ArrayList<ScreenParticle> list = PARTICLES.computeIfAbsent(Pair.of(particle.getRenderType(), particle.renderOrder), (a) -> new ArrayList<>());
        list.add(particle);
        return particle;
    }

    public static void wipeParticles(RenderOrder... renderOrders) {
        PARTICLES.forEach((pair, particles) -> {
            if (!particles.isEmpty()) {
                if (renderOrders.length == 0 || Arrays.stream(renderOrders).anyMatch(o -> o.equals(pair.getSecond()))) {
                    particles.clear();
                }
            }
        });
    }

    public static void registerItemParticleEmitter(Item item, ParticleEmitter.EmitterSupplier emitter) {
        EMITTERS.put(item, new ParticleEmitter(emitter));
    }

    public static void registerItemParticleEmitter(ParticleEmitter.EmitterSupplier emitter, Item... items) {
        for (Item item : items) {
            EMITTERS.put(item, new ParticleEmitter(emitter));
        }
    }

    public record StackTracker(ItemStack stack, RenderOrder order, float xOrigin, float yOrigin) {
    }
}