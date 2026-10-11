package team.lodestar.lodestone.handlers.screenparticle;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.RenderFrameEvent;
import team.lodestar.lodestone.modules.rendering.particle.standard.screen.ScreenParticleConfig;
import team.lodestar.lodestone.modules.rendering.particle.standard.screen.ScreenParticleHolder;
import team.lodestar.lodestone.modules.rendering.particle.standard.screen.ItemStackScreenParticleKey;
import team.lodestar.lodestone.modules.rendering.particle.standard.screen.ItemStackScreenParticleCacheKey;
import team.lodestar.lodestone.modules.rendering.particle.standard.screen.ScreenParticleType;
import team.lodestar.lodestone.modules.rendering.particle.standard.screen.base.ScreenParticle;

import java.util.*;

/**
 * A handler for screen particles.
 * Particles are spawned during rendering once per tick.
 * We also track all present ItemStacks on the screen to allow our particles to more optimally follow a given ItemStacks position
 * Use {@link ItemScreenParticleHandler#addParticle(ScreenParticleHolder, ScreenParticleConfig, double, double, double, double)} to create a screen particle, which will then be ticked.
 */
public class ItemScreenParticleHandler {

    /**
     * Item Stack Bound Particles are rendered just after an item stack in the inventory. They are ticked the same as other particles.
     * We use a pair of a boolean and the ItemStack as a key. The boolean sorts item particles based on if the ItemStack in question is in the hotbar or not.
     */
    public static final Map<ItemStackScreenParticleKey, ScreenParticleHolder> ITEM_PARTICLES = new HashMap<>();
    public static final Map<ItemStackScreenParticleCacheKey, ItemStack> ITEM_STACK_CACHE = new HashMap<>();
    public static final Collection<ItemStackScreenParticleCacheKey> ACTIVELY_ACCESSED_KEYS = new ArrayList<>();

    public static ScreenParticleHolder cachedItemParticles = null;
    public static int currentItemX, currentItemY;

    protected static boolean canSpawnParticles;
    protected static boolean isRenderingHotbar;

    public static void setCanSpawnParticles(boolean canSpawnParticles) {
        ItemScreenParticleHandler.canSpawnParticles = canSpawnParticles;
    }

    public static void setRenderingHotbar(boolean isRenderingHotbar) {
        ItemScreenParticleHandler.isRenderingHotbar = isRenderingHotbar;
    }

    public static void tickItemParticles() {
        var toRemove = new ArrayList<ItemStackScreenParticleKey>();
        for (ItemStackScreenParticleKey key : ITEM_PARTICLES.keySet()) {
            var holder = ITEM_PARTICLES.get(key);
            holder.tick();
            if (holder.isEmpty()) {
                toRemove.add(key);
            }
        }
        for (ItemStackScreenParticleKey key : toRemove) {
            ITEM_PARTICLES.remove(key);
        }

        ITEM_STACK_CACHE.keySet().removeIf(k -> !ACTIVELY_ACCESSED_KEYS.contains(k));
        ACTIVELY_ACCESSED_KEYS.clear();


        canSpawnParticles = true;
    }

    public static void renderTick(RenderFrameEvent.Post event) {
        canSpawnParticles = false;
    }

    public static void renderItemStackEarly(PoseStack poseStack, ItemStack stack, int x, int y) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level != null && minecraft.player != null) {
            if (minecraft.isPaused()) {
                return;
            }
            if (stack.isEmpty()) {
                return;
            }
            var emitters = ItemParticleEmitters.EMITTERS.get(stack.getItem());
            if (emitters == null) {
                return;
            }
            var pose = poseStack.last().pose();
            currentItemX = x + 8;
            currentItemY = y + 8;
            if (currentItemX == 8 && currentItemY == 8) {
                int poseOffsetX = (int) pose.m30();
                int poseOffsetY = (int) pose.m31();
                currentItemX += poseOffsetX;
                currentItemY += poseOffsetY;
            }
            for (ItemParticleEmitters.ItemParticleEmitter emitter : emitters) {
                spawnAndPullParticles(minecraft.level, emitter, stack, false).render(poseStack);
                cachedItemParticles = spawnAndPullParticles(minecraft.level, emitter, stack, true);
            }
        }
    }

    public static void renderItemStackLate(PoseStack poseStack) {
        if (cachedItemParticles != null) {
            cachedItemParticles.render(poseStack);
            cachedItemParticles = null;
        }
    }

    public static ScreenParticleHolder spawnAndPullParticles(ClientLevel level, ItemParticleEmitters.ItemParticleEmitter emitter, ItemStack stack, boolean isRenderedAfterItem) {
        var liveKey = new ItemStackScreenParticleKey(isRenderingHotbar, isRenderedAfterItem, stack);
        var cacheKey = new ItemStackScreenParticleCacheKey(isRenderingHotbar, isRenderedAfterItem, currentItemX, currentItemY);
        var target = ITEM_PARTICLES.computeIfAbsent(liveKey, s -> new ScreenParticleHolder());
        pullFromParticleCache(cacheKey, stack, target, isRenderedAfterItem);
        if (canSpawnParticles) {
            var minecraft = Minecraft.getInstance();
            var partialTicks = minecraft.getTimer().getGameTimeDeltaPartialTick(false);
            if (isRenderedAfterItem) {
                emitter.spawnLateParticles(target, level, partialTicks, stack, currentItemX, currentItemY);
            } else {
                emitter.spawnEarlyParticles(target, level, partialTicks, stack, currentItemX, currentItemY);
            }
        }
        ACTIVELY_ACCESSED_KEYS.add(cacheKey);
        return target;
    }

    public static void pullFromParticleCache(ItemStackScreenParticleCacheKey cacheKey, ItemStack currentStack, ScreenParticleHolder target, boolean isLateRender) {
        if (ITEM_STACK_CACHE.containsKey(cacheKey)) {
            var oldStack = ITEM_STACK_CACHE.get(cacheKey);
            if (ItemStack.isSameItem(oldStack, currentStack)) {
                var oldKey = new ItemStackScreenParticleKey(isRenderingHotbar, isLateRender, oldStack);
                var oldParticles = ITEM_PARTICLES.get(oldKey);
                if (oldParticles != null) {
                    target.addFrom(oldParticles);
                }
                ITEM_STACK_CACHE.remove(cacheKey);
                ITEM_PARTICLES.remove(oldKey);
            }
        }
        ITEM_STACK_CACHE.put(cacheKey, currentStack);
    }

    @SuppressWarnings("unchecked")
    public static <T extends ScreenParticleConfig> ScreenParticle addParticle(ScreenParticleHolder target, T options, double x, double y, double xMotion, double yMotion) {
        var minecraft = Minecraft.getInstance();
        ScreenParticleType<T> type = (ScreenParticleType<T>) options.type;
        ScreenParticle particle = type.provider.createParticle(minecraft.level, options, x, y, xMotion, yMotion);
        ArrayList<ScreenParticle> list = target.particles.computeIfAbsent(options.renderType, (a) -> new ArrayList<>());
        list.add(particle);
        return particle;
    }
}