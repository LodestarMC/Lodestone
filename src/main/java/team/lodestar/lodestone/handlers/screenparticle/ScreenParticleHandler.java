package team.lodestar.lodestone.handlers.screenparticle;

import team.lodestar.lodestone.config.ClientConfig;
import team.lodestar.lodestone.systems.rendering.particle.screen.*;
import team.lodestar.lodestone.systems.rendering.particle.screen.base.ScreenParticle;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
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
     * Regular Screen Particles are rendered after other UI elements, but before things like tooltips or other overlays.
     * Late Screen Particles are rendered after everything else.
     */
    public static final HashMap<ScreenParticleRenderType, ArrayList<ScreenParticle>> EARLY_SCREEN_PARTICLES = new HashMap<>();
    public static final HashMap<ScreenParticleRenderType, ArrayList<ScreenParticle>> SCREEN_PARTICLES = new HashMap<>();
    public static final HashMap<ScreenParticleRenderType, ArrayList<ScreenParticle>> LATE_SCREEN_PARTICLES = new HashMap<>();

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
        tickParticles(EARLY_SCREEN_PARTICLES);
        tickParticles(SCREEN_PARTICLES);
        tickParticles(LATE_SCREEN_PARTICLES);
        canSpawnParticles = true;
    }

    public static void tickParticles(HashMap<ScreenParticleRenderType, ArrayList<ScreenParticle>> particleMap) {
        particleMap.forEach((pair, particles) -> {
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
        renderParticles(EARLY_SCREEN_PARTICLES);
        renderedEarlyParticles = true;
    }

    public static void renderParticles() {
        renderParticles(SCREEN_PARTICLES);
        renderedParticles = true;
    }

    public static void renderLateParticles() {
        renderParticles(LATE_SCREEN_PARTICLES);
        renderedLateParticles = true;
    }

    public static void renderParticles(HashMap<ScreenParticleRenderType, ArrayList<ScreenParticle>> particleMap) {
        if (!ClientConfig.ENABLE_SCREEN_PARTICLES.getConfigValue()) {
            return;
        }
        particleMap.forEach((renderType, particles) -> {
            renderType.begin(TESSELATOR.getBuilder(), Minecraft.getInstance().textureManager);
            for (ScreenParticle next : particles) {
                next.render(TESSELATOR.getBuilder());
            }
            renderType.end(TESSELATOR);
        });
    }

    @SuppressWarnings("unchecked")
    public static <T extends ScreenParticleOptions> ScreenParticle addParticle(HashMap<ScreenParticleRenderType, ArrayList<ScreenParticle>> particleMap, T options, double x, double y, double xMotion, double yMotion) {
        Minecraft minecraft = Minecraft.getInstance();
        ScreenParticleType<T> type = (ScreenParticleType<T>) options.type;
        ScreenParticle particle = type.provider.createParticle(minecraft.level, options, x, y, xMotion, yMotion);
        ArrayList<ScreenParticle> list = particleMap.computeIfAbsent(options.renderType, (a) -> new ArrayList<>());
        list.add(particle);
        return particle;
    }

}