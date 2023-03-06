package team.lodestar.lodestone.handlers.screenparticle;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.debug.GameModeSwitcherScreen;
import net.minecraft.world.item.ItemStack;
import team.lodestar.lodestone.config.ClientConfig;
import team.lodestar.lodestone.systems.particle.screen.*;
import team.lodestar.lodestone.systems.particle.screen.base.ScreenParticle;
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
    public static final HashMap<LodestoneScreenParticleRenderType, ArrayList<ScreenParticle>> EARLIEST_PARTICLES = new HashMap<>();

    /**
     * Early Screen Particles are rendered after other UI elements, but before things like tooltips or other overlays.
     */
    public static final HashMap<LodestoneScreenParticleRenderType, ArrayList<ScreenParticle>> EARLY_PARTICLES = new HashMap<>();

    /**
     * Late Screen Particles are rendered after everything else.
     */
    public static final HashMap<LodestoneScreenParticleRenderType, ArrayList<ScreenParticle>> LATE_PARTICLES = new HashMap<>();

    /**
     * Item Stack Bound Particles are rendered just after an item stack in the inventory. They are ticked the same as other particles.
     * We use a pair of a boolean and the ItemStack as a key. The boolean sorts item particles based on if the ItemStack in question is in the hotbar or not.
     */
    public static final HashMap<Pair<Boolean, ItemStack>, HashMap<LodestoneScreenParticleRenderType, ArrayList<ScreenParticle>>> ITEM_PARTICLES = new HashMap<>();
    public static final HashMap<Pair<Boolean, Pair<Integer, Integer>>, ItemStack> ITEM_STACK_CACHE = new HashMap<>();

    public static HashMap<LodestoneScreenParticleRenderType, ArrayList<ScreenParticle>> cachedItemTarget = null;
    public static int currentItemX, currentItemY;

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

        ITEM_PARTICLES.values().forEach(ScreenParticleHandler::tickParticles);
        ITEM_PARTICLES.values().removeIf(map -> map.values().stream().allMatch(ArrayList::isEmpty));
        canSpawnParticles = true;
    }

    public static void tickParticles(HashMap<LodestoneScreenParticleRenderType, ArrayList<ScreenParticle>> screenParticleTarget) {
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

    public static void renderItemStackEarly(ItemStack stack, int x, int y) {
        if (!ClientConfig.ENABLE_SCREEN_PARTICLES.getConfigValue()) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level != null && minecraft.player != null) {
            if (minecraft.isPaused()) {
                return;
            }
            if (!stack.isEmpty()) {
                currentItemX = x+8;
                currentItemY = y+8;
                ParticleEmitterHandler.ItemParticleSupplier emitter = ParticleEmitterHandler.EMITTERS.get(stack.getItem());
                if (emitter != null) {
                    HashMap<LodestoneScreenParticleRenderType, ArrayList<ScreenParticle>> target = ITEM_PARTICLES.computeIfAbsent(Pair.of(renderingHotbar, stack), s -> new HashMap<>());

                    pullFromParticleVault(stack, target);
                    if (canSpawnParticles) {
                        emitter.spawnParticles(target, minecraft.level, Minecraft.getInstance().timer.partialTick, stack, currentItemX, currentItemY);
                    }
                    cachedItemTarget = target;
                }
            }
        }
    }

    public static void pullFromParticleVault(ItemStack currentStack, HashMap<LodestoneScreenParticleRenderType, ArrayList<ScreenParticle>> target) {
        Pair<Boolean, Pair<Integer, Integer>> cacheKey = Pair.of(renderingHotbar, Pair.of(currentItemX, currentItemY));
        if (ITEM_STACK_CACHE.containsKey(cacheKey)) {
            ItemStack oldStack = ITEM_STACK_CACHE.get(cacheKey);
            if (oldStack != currentStack) {
                HashMap<LodestoneScreenParticleRenderType, ArrayList<ScreenParticle>> oldParticles = ITEM_PARTICLES.get(Pair.of(renderingHotbar, oldStack));
                oldParticles.forEach((key, value) -> {
                    List<ScreenParticle> particles = target.get(key);
                    if (particles != null) {
                        particles.addAll(value);
                    }
                });
                ITEM_STACK_CACHE.remove(cacheKey);
            }
        }
        ITEM_STACK_CACHE.put(cacheKey, currentStack);
    }

    public static void renderItemStackLate() {
        if (cachedItemTarget != null) {
            renderParticles(cachedItemTarget);
            cachedItemTarget = null;
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

    private static void renderParticles(HashMap<LodestoneScreenParticleRenderType, ArrayList<ScreenParticle>> screenParticleTarget) {
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
        ITEM_PARTICLES.values().forEach(ScreenParticleHandler::clearParticles);
    }

    public static void clearParticles(HashMap<LodestoneScreenParticleRenderType, ArrayList<ScreenParticle>> screenParticleTarget) {
        screenParticleTarget.values().forEach(ArrayList::clear);
    }

    @SuppressWarnings("unchecked")
    public static <T extends ScreenParticleOptions> ScreenParticle addParticle(HashMap<LodestoneScreenParticleRenderType, ArrayList<ScreenParticle>> screenParticleTarget, T options, double x, double y, double xMotion, double yMotion) {
        Minecraft minecraft = Minecraft.getInstance();
        ScreenParticleType<T> type = (ScreenParticleType<T>) options.type;
        ScreenParticle particle = type.provider.createParticle(minecraft.level, options, x, y, xMotion, yMotion);
        ArrayList<ScreenParticle> list = screenParticleTarget.computeIfAbsent(options.renderType, (a) -> new ArrayList<>());
        list.add(particle);
        return particle;
    }

    public static class ItemStackParticleData {

    }
}