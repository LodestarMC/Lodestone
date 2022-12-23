package team.lodestar.lodestone.handlers;

import team.lodestar.lodestone.compability.JeiCompat;
import team.lodestar.lodestone.config.ClientConfig;
import team.lodestar.lodestone.helpers.DataHelper;
import team.lodestar.lodestone.systems.rendering.particle.screen.GenericScreenParticle;
import team.lodestar.lodestone.systems.rendering.particle.screen.ScreenParticleOptions;
import team.lodestar.lodestone.systems.rendering.particle.screen.ScreenParticleRenderType;
import team.lodestar.lodestone.systems.rendering.particle.screen.ScreenParticleType;
import team.lodestar.lodestone.systems.rendering.particle.screen.base.ScreenParticle;
import team.lodestar.lodestone.systems.rendering.particle.screen.emitter.ItemParticleEmitter;
import team.lodestar.lodestone.systems.rendering.particle.screen.emitter.ParticleEmitter;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Matrix4f;
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

import java.util.*;

/**
 * A handler for screen particles.
 * Particles are spawned during rendering once per tick.
 * We also track all present ItemStacks on the screen to allow our particles to more optimally follow a given ItemStacks position
 * Use {@link ScreenParticleHandler#addParticle(ScreenParticleOptions, double, double, double, double)} to create a screen particle, which will then be ticked.
 * A ScreenParticles {@link ScreenParticle.RenderOrder} decides when exactly it is rendered, different mixins will render different render orders.
 */
public class ScreenParticleHandler {

    public static final Map<Pair<ScreenParticleRenderType, ScreenParticle.RenderOrder>, ArrayList<ScreenParticle>> PARTICLES = new HashMap<>();
    public static final Map<Item, ParticleEmitter> EMITTERS = new HashMap<>();
    public static final ArrayList<StackTracker> RENDERED_STACKS = new ArrayList<>();

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

    public static void tickParticles() {
        if (!ClientConfig.ENABLE_SCREEN_PARTICLES.getConfigValue()) {
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

    public static void spawnItemParticles(ItemStack stack) {
        if (!ClientConfig.ENABLE_SCREEN_PARTICLES.getConfigValue()) {
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
                    ScreenParticle.RenderOrder renderOrder = ScreenParticle.RenderOrder.AFTER_EVERYTHING;
                    Screen screen = minecraft.screen;
                    if (screen != null) {
                        if (!JeiCompat.LOADED || !JeiCompat.LoadedOnly.isRecipesUi(screen)) {
                            renderOrder = ScreenParticle.RenderOrder.BEFORE_TOOLTIPS;
                        }
                        if (renderingHotbar) {
                            renderOrder = ScreenParticle.RenderOrder.BEFORE_UI;
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
                renderParticles(ScreenParticle.RenderOrder.AFTER_EVERYTHING);
            }
            if (screen == null || screen instanceof ChatScreen || screen instanceof GameModeSwitcherScreen) {
                renderParticles(ScreenParticle.RenderOrder.AFTER_EVERYTHING, ScreenParticle.RenderOrder.BEFORE_UI);
            }
            RENDERED_STACKS.clear();
            canSpawnParticles = false;
        }
    }

    public static void renderParticles(ScreenParticle.RenderOrder... renderOrders) {
        if (!ClientConfig.ENABLE_SCREEN_PARTICLES.getConfigValue()) {
            return;
        }

        PARTICLES.forEach((pair, particles) -> {
            ScreenParticleRenderType type = pair.getFirst();
            if (Arrays.stream(renderOrders).anyMatch(o -> o.equals(pair.getSecond()))) {
                type.begin(TESSELATOR.getBuilder(), Minecraft.getInstance().textureManager);
                for (ScreenParticle next : particles) {
                    next.render(TESSELATOR.getBuilder());
                }
                type.end(TESSELATOR);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static <T extends ScreenParticleOptions> ScreenParticle addParticle(T options, double pX, double pY, double pXSpeed, double pYSpeed) {
        Minecraft minecraft = Minecraft.getInstance();
        ScreenParticleType<T> type = (ScreenParticleType<T>) options.type;
        ScreenParticleType.ParticleProvider<T> provider = type.provider;
        ScreenParticle particle = provider.createParticle(minecraft.level, options, pX, pY, pXSpeed, pYSpeed);
        ArrayList<ScreenParticle> list = PARTICLES.computeIfAbsent(Pair.of(particle.getRenderType(), particle.getRenderOrder()), (a) -> new ArrayList<>());
        list.add(particle);
        return particle;
    }

    public static void wipeParticles(ScreenParticle.RenderOrder... renderOrders) {
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

    public record StackTracker(ItemStack stack, ScreenParticle.RenderOrder order, float xOrigin, float yOrigin) {
    }
}
