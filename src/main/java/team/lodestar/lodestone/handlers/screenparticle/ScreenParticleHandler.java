package team.lodestar.lodestone.handlers.screenparticle;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.debug.GameModeSwitcherScreen;
import net.minecraft.world.item.ItemStack;
import team.lodestar.lodestone.compability.JeiCompat;
import team.lodestar.lodestone.config.ClientConfig;
import team.lodestar.lodestone.mixin.ItemStackMixin;
import team.lodestar.lodestone.systems.rendering.particle.screen.*;
import team.lodestar.lodestone.systems.rendering.particle.screen.base.ScreenParticle;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.TickEvent;

import java.util.*;

/**
 * A handler for screen particles.
 * Particles are spawned during rendering once per tick.
 * We also track all present ItemStacks on the screen to allow our particles to more optimally follow a given ItemStacks position
 * Use {@link ScreenParticleHandler#addParticle(HashMap, ScreenParticleOptions, double, double, double, double)} to create a screen particle, which will then be ticked.
 */
public class ScreenParticleHandler {

    /**
     * Earliest Screen Particles are rendered before nearly every piece of user interface.
     */
    public static final HashMap<ScreenParticleRenderType, ArrayList<ScreenParticle>> EARLIEST_PARTICLES = new HashMap<>();

    /**
     * Early Screen Particles are rendered after other UI elements, but before things like tooltips or other overlays.
     */
    public static final HashMap<ScreenParticleRenderType, ArrayList<ScreenParticle>> EARLY_PARTICLES = new HashMap<>();

    /**
     * Late Screen Particles are rendered after everything else.
     */
    public static final HashMap<ScreenParticleRenderType, ArrayList<ScreenParticle>> LATE_PARTICLES = new HashMap<>();

   // public static final HashMap<ItemStack, HashMap<ScreenParticleRenderType, ArrayList<ScreenParticle>>> ITEM_STACK_BOUND_PARTICLES = new HashMap<>();

    public static final Tesselator TESSELATOR = new Tesselator();
    public static boolean canSpawnParticles;

    public static boolean renderingHotbar;

    public static void tickParticles() {
        if (!ClientConfig.ENABLE_SCREEN_PARTICLES.getConfigValue()) {
            return;
        }
        tickParticles(EARLIEST_PARTICLES);
        tickParticles(EARLY_PARTICLES);
        tickParticles(LATE_PARTICLES);
       // ITEM_STACK_BOUND_PARTICLES.values().forEach(ScreenParticleHandler::tickParticles);

        canSpawnParticles = true;
    }

    public static void tickParticles(HashMap<ScreenParticleRenderType, ArrayList<ScreenParticle>> screenParticleTarget) {
        screenParticleTarget.forEach((pair, particles) -> {
            Iterator<ScreenParticle> iterator = particles.iterator();
            while (iterator.hasNext()) {
                ScreenParticle particle = iterator.next();
                particle.tick();
                if (!particle.isAlive()) {
                    iterator.remove();
                }
            }
        });
    }

    public static void renderItemStack(ItemStack stack) {
        if (!ClientConfig.ENABLE_SCREEN_PARTICLES.getConfigValue()) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level != null && minecraft.player != null) {
            if (minecraft.isPaused()) {
                return;
            }
            if (!stack.isEmpty()) {
                ParticleEmitterHandler.ItemParticleSupplier emitter = ParticleEmitterHandler.EMITTERS.get(stack.getItem());
                if (emitter != null) {
                    PoseStack posestack = RenderSystem.getModelViewStack();
                    Matrix4f last = posestack.last().pose();
                    float x = last.m03;
                    float y = last.m13;
                    float z = last.m23;
                    HashMap<ScreenParticleRenderType, ArrayList<ScreenParticle>> target = EARLY_PARTICLES;
                    Screen screen = minecraft.screen;
                    if (renderingHotbar) {
                        target = EARLIEST_PARTICLES;
                    }
                    else if (screen != null) {
                        if (JeiCompat.LOADED || JeiCompat.LoadedOnly.isRecipesUi(screen)) {
                            target = LATE_PARTICLES;
                        }
                    }
                    if (canSpawnParticles) {
                        emitter.spawnParticles(target, minecraft.level, Minecraft.getInstance().timer.partialTick, stack, x, y);
                    }
                }
            }
        }
    }

    public static void renderParticles(TickEvent.RenderTickEvent event) {
        if (event.phase.equals(TickEvent.Phase.END)) {
            if (!ClientConfig.ENABLE_SCREEN_PARTICLES.getConfigValue()) {
                return;
            }
            Screen screen = Minecraft.getInstance().screen;

            if (screen == null || screen instanceof ChatScreen || screen instanceof GameModeSwitcherScreen) {
                renderEarliestParticles();
            }
            renderLateParticles();
            canSpawnParticles = false;
        }
    }

    public static void renderEarliestParticles() {
        renderParticles(EARLIEST_PARTICLES);
    }

    public static void renderEarlyParticles() {
        renderParticles(EARLY_PARTICLES);
    }

    public static void renderLateParticles() {
        renderParticles(LATE_PARTICLES);
    }

    private static void renderParticles(HashMap<ScreenParticleRenderType, ArrayList<ScreenParticle>> screenParticleTarget) {
        if (!ClientConfig.ENABLE_SCREEN_PARTICLES.getConfigValue()) {
            return;
        }
        screenParticleTarget.forEach((renderType, particles) -> {
            renderType.begin(TESSELATOR.getBuilder(), Minecraft.getInstance().textureManager);
            for (ScreenParticle next : particles) {
                next.render(TESSELATOR.getBuilder());
            }
            renderType.end(TESSELATOR);
        });
    }

    public static void clearParticles() {
        clearParticles(EARLIEST_PARTICLES);
        clearParticles(EARLY_PARTICLES);
        clearParticles(LATE_PARTICLES);
    }

    public static void clearParticles(HashMap<ScreenParticleRenderType, ArrayList<ScreenParticle>> screenParticleTarget) {
        screenParticleTarget.values().forEach(ArrayList::clear);
    }

    @SuppressWarnings("unchecked")
    public static <T extends ScreenParticleOptions> ScreenParticle addParticle(HashMap<ScreenParticleRenderType, ArrayList<ScreenParticle>> screenParticleTarget, T options, double x, double y, double xMotion, double yMotion) {
        Minecraft minecraft = Minecraft.getInstance();
        ScreenParticleType<T> type = (ScreenParticleType<T>) options.type;
        ScreenParticle particle = type.provider.createParticle(minecraft.level, options, x, y, xMotion, yMotion);
        ArrayList<ScreenParticle> list = screenParticleTarget.computeIfAbsent(options.renderType, (a) -> new ArrayList<>());
        list.add(particle);
        return particle;
    }
}