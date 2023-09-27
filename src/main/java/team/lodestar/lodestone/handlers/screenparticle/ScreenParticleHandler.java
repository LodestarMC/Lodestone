package team.lodestar.lodestone.handlers.screenparticle;

import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.debug.GameModeSwitcherScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import team.lodestar.lodestone.config.ClientConfig;
import team.lodestar.lodestone.systems.particle.options.ScreenParticleOptions;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleHolder;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleItemStackKey;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleItemStackRetrievalKey;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleType;
import team.lodestar.lodestone.systems.particle.screen.base.ScreenParticle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A handler for screen particles.
 * Particles are spawned during rendering once per tick.
 * We also track all present ItemStacks on the screen to allow our particles to more optimally follow a given ItemStacks position
 * Use {@link ScreenParticleHandler#addParticle(ScreenParticleHolder, ScreenParticleOptions, double, double, double, double)} to create a screen particle, which will then be ticked.
 */
public class ScreenParticleHandler {

    /**
     * Earliest Screen Particles are rendered before nearly every piece of user interface.
     */
    public static final ScreenParticleHolder EARLIEST_PARTICLES = new ScreenParticleHolder();

    /**
     * Early Screen Particles are rendered after other UI elements, but before things like tooltips or other overlays.
     */
    public static final ScreenParticleHolder EARLY_PARTICLES = new ScreenParticleHolder();

    /**
     * Late Screen Particles are rendered after everything else.
     */
    public static final ScreenParticleHolder LATE_PARTICLES = new ScreenParticleHolder();

    /**
     * Item Stack Bound Particles are rendered just after an item stack in the inventory. They are ticked the same as other particles.
     * We use a pair of a boolean and the ItemStack as a key. The boolean sorts item particles based on if the ItemStack in question is in the hotbar or not.
     */
    public static final Map<ScreenParticleItemStackKey, ScreenParticleHolder> ITEM_PARTICLES = new HashMap<>();
    public static final Map<ScreenParticleItemStackRetrievalKey, ItemStack> ITEM_STACK_CACHE = new HashMap<>();
    public static final Collection<ScreenParticleItemStackRetrievalKey> ACTIVELY_ACCESSED_KEYS = new ArrayList<>();

    public static ScreenParticleHolder cachedItemParticles = null;
    public static int currentItemX, currentItemY;

    public static final Tesselator TESSELATOR = new Tesselator();
    public static boolean canSpawnParticles;

    public static boolean renderingHotbar;

    public static void tickParticles() {
        if (!ClientConfig.ENABLE_SCREEN_PARTICLES.getConfigValue()) {
            return;
        }

        EARLIEST_PARTICLES.tick();
        EARLY_PARTICLES.tick();
        LATE_PARTICLES.tick();

        ITEM_PARTICLES.values().forEach(ScreenParticleHolder::tick);
        ITEM_PARTICLES.values().removeIf(ScreenParticleHolder::isEmpty);

        ITEM_STACK_CACHE.keySet().removeIf(k -> !ACTIVELY_ACCESSED_KEYS.contains(k));
        ACTIVELY_ACCESSED_KEYS.clear();
        canSpawnParticles = true;
    }

    public static void renderItemStackEarly(ItemStack stack, int x, int y, boolean b) {
        if (!ClientConfig.ENABLE_SCREEN_PARTICLES.getConfigValue()) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level != null && minecraft.player != null) {
            if (minecraft.isPaused()) {
                return;
            }
            if (!stack.isEmpty()) {

                currentItemX = x + 8;
                currentItemY = y + 8;

                if (b && minecraft.screen instanceof AbstractContainerScreen<?> containerScreen) {//TODO this whole thing sucks
                    int i = containerScreen.leftPos;
                    int j = containerScreen.topPos;

                    currentItemX += i;
                    currentItemY += j;
                } // TODO to here


                ParticleEmitterHandler.ItemParticleSupplier emitter = ParticleEmitterHandler.EMITTERS.get(stack.getItem());
                if (emitter != null) {
                    renderParticles(spawnAndPullParticles(minecraft.level, emitter, stack, false));
                    cachedItemParticles = spawnAndPullParticles(minecraft.level, emitter, stack, true);
                }
            }
        }
    }

    public static ScreenParticleHolder spawnAndPullParticles(ClientLevel level, ParticleEmitterHandler.ItemParticleSupplier emitter, ItemStack stack, boolean isRenderedAfterItem) {
        ScreenParticleItemStackRetrievalKey cacheKey = new ScreenParticleItemStackRetrievalKey(renderingHotbar, isRenderedAfterItem, currentItemX, currentItemY);
        ScreenParticleHolder target = ITEM_PARTICLES.computeIfAbsent(new ScreenParticleItemStackKey(renderingHotbar, isRenderedAfterItem, stack), s -> new ScreenParticleHolder());
        pullFromParticleVault(cacheKey, stack, target, isRenderedAfterItem);
        if (canSpawnParticles) {
            if (isRenderedAfterItem) {
                emitter.spawnLateParticles(target, level, Minecraft.getInstance().timer.partialTick, stack, currentItemX, currentItemY);
            } else {
                emitter.spawnEarlyParticles(target, level, Minecraft.getInstance().timer.partialTick, stack, currentItemX, currentItemY);
            }
        }
        ACTIVELY_ACCESSED_KEYS.add(cacheKey);
        return target;
    }

    public static void pullFromParticleVault(ScreenParticleItemStackRetrievalKey cacheKey, ItemStack currentStack, ScreenParticleHolder target, boolean isRenderedAfterItem) {
        if (ITEM_STACK_CACHE.containsKey(cacheKey)) {
            ItemStack oldStack = ITEM_STACK_CACHE.get(cacheKey);
            if (oldStack != currentStack && oldStack.getItem().equals(currentStack.getItem())) {
                ScreenParticleItemStackKey oldKey = new ScreenParticleItemStackKey(renderingHotbar, isRenderedAfterItem, oldStack);
                ScreenParticleHolder oldParticles = ITEM_PARTICLES.get(oldKey);
                if (oldParticles != null) {
                    target.addFrom(oldParticles);
                }
                ITEM_STACK_CACHE.remove(cacheKey);
                ITEM_PARTICLES.remove(oldKey);
            }
        }
        ITEM_STACK_CACHE.put(cacheKey, currentStack);
    }

    public static void renderItemStackLate() {
        if (cachedItemParticles != null) {
            renderParticles(cachedItemParticles);
            cachedItemParticles = null;
        }
    }

    public static void renderParticles(TickEvent.RenderTickEvent event) {
        if (event.phase.equals(TickEvent.Phase.END)) {
            if (false) {//TODO ClientConfig.ENABLE_SCREEN_PARTICLES.getConfigValue()
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

    private static void renderParticles(ScreenParticleHolder screenParticleTarget) {
        if (false) {//TODO ClientConfig.ENABLE_SCREEN_PARTICLES.getConfigValue()
            return;
        }
        screenParticleTarget.particles.forEach((renderType, particles) -> {
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

    public static void clearParticles(ScreenParticleHolder screenParticleTarget) {
        screenParticleTarget.particles.values().forEach(ArrayList::clear);
    }

    @SuppressWarnings("unchecked")
    public static <T extends ScreenParticleOptions> ScreenParticle addParticle(ScreenParticleHolder screenParticleTarget, T options, double x, double y, double xMotion, double yMotion) {
        Minecraft minecraft = Minecraft.getInstance();
        ScreenParticleType<T> type = (ScreenParticleType<T>) options.type;
        ScreenParticle particle = type.provider.createParticle(minecraft.level, options, x, y, xMotion, yMotion);
        ArrayList<ScreenParticle> list = screenParticleTarget.particles.computeIfAbsent(options.renderType, (a) -> new ArrayList<>());
        list.add(particle);
        return particle;
    }
}