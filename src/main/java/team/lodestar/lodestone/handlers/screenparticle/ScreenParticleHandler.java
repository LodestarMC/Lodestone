package team.lodestar.lodestone.handlers.screenparticle;

import team.lodestar.lodestone.config.ClientConfig;
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
     * Early Screen Particles are rendered before most other UI elements.
     * Post Ui Particles are rendered after other UI elements, but before things like tooltips or other overlays.
     * Late Screen Particles are rendered after everything else.
     */
    public static final HashMap<ScreenParticleRenderType, ArrayList<ScreenParticle>> EARLY_TARGET = new HashMap<>();
    public static final HashMap<ScreenParticleRenderType, ArrayList<ScreenParticle>> AFTER_UI_TARGET = new HashMap<>();
    public static final HashMap<ScreenParticleRenderType, ArrayList<ScreenParticle>> LATE_TARGET = new HashMap<>();

    private static boolean renderedEarlyParticles;
    private static boolean renderedParticles;
    private static boolean renderedLateParticles;

    public static final Tesselator TESSELATOR = new Tesselator();
    public static boolean canSpawnParticles;

    public static boolean renderingHotbar;

    public static void tickParticles() {
        if (!ClientConfig.ENABLE_SCREEN_PARTICLES.getConfigValue()) {
            return;
        }
        tickParticles(EARLY_TARGET);
        tickParticles(AFTER_UI_TARGET);
        tickParticles(LATE_TARGET);
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

    public static void renderParticles(TickEvent.RenderTickEvent event) {
        if (event.phase.equals(TickEvent.Phase.END)) {
            if (!ClientConfig.ENABLE_SCREEN_PARTICLES.getConfigValue()) {
                return;
            }
            if (!renderedEarlyParticles) {
                renderEarlyParticles();
            }
            if (!renderedParticles) {
                renderParticles();
            }
            if (!renderedLateParticles) {
                renderLateParticles();
            }

            ParticleEmitterHandler.RENDERED_STACKS.clear();

            renderedEarlyParticles = false;
            renderedParticles = false;
            renderedLateParticles = false;
            canSpawnParticles = false;
        }
    }

    public static void renderEarlyParticles() {
        renderParticles(EARLY_TARGET);
        renderedEarlyParticles = true;
    }

    public static void renderParticles() {
        renderParticles(AFTER_UI_TARGET);
        renderedParticles = true;
    }

    public static void renderLateParticles() {
        renderParticles(LATE_TARGET);
        renderedLateParticles = true;
    }

    public static void renderParticles(HashMap<ScreenParticleRenderType, ArrayList<ScreenParticle>> screenParticleTarget) {
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