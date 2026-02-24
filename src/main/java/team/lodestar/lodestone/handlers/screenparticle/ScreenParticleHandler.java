package team.lodestar.lodestone.handlers.screenparticle;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.client.event.*;
import team.lodestar.lodestone.common.config.*;
import team.lodestar.lodestone.systems.particle.screen.*;

import java.util.*;

/**
 * A handler for screen particles.
 * Particles are spawned during rendering once per tick.
 * We also track all present ItemStacks on the screen to allow our particles to more optimally follow a given ItemStacks position
 * Use {@link ScreenParticleHolder#addParticle(ScreenParticleOptions, double, double, double, double)} to create a screen particle, which will then be ticked.
 */
@SuppressWarnings("unused")
public class ScreenParticleHandler {

    /**
     * Item Stack Bound Particles are rendered just after an item stack in the inventory. They are ticked the same as other particles.
     * We use a pair of a boolean and the ItemStack as a key. The boolean sorts item particles based on if the ItemStack in question is in the hotbar or not.
     */
    public static final Map<ScreenParticleItemStackKey, ScreenParticleHolder> ITEM_PARTICLE_HOLDERS = new HashMap<>();
    public static final Map<ScreenParticleItemStackVaultKey, ItemStack> ITEM_PARTICLE_VAULTS = new HashMap<>();
    public static final Collection<ScreenParticleItemStackVaultKey> ACTIVELY_ACCESSED_KEYS = new ArrayList<>();

    public static ScreenParticleHolder cachedLateItemParticles = null;
    public static int currentItemX, currentItemY;

    public static boolean canSpawnParticles;

    public static boolean renderingHotbar;

    public static void tickParticles() {
        if (!ClientConfig.ENABLE_SCREEN_PARTICLES.getConfigValue()) {
            return;
        }

        ITEM_PARTICLE_HOLDERS.values().forEach(ScreenParticleHolder::tick);
        ITEM_PARTICLE_HOLDERS.values().removeIf(ScreenParticleHolder::isEmpty);

        ITEM_PARTICLE_VAULTS.keySet().removeIf(k -> !ACTIVELY_ACCESSED_KEYS.contains(k));
        ACTIVELY_ACCESSED_KEYS.clear();
        canSpawnParticles = true;
    }

    public static void renderTick(RenderFrameEvent.Post event) {
        canSpawnParticles = false;
    }

    public static void renderItemStackEarly(PoseStack poseStack, ItemStack stack, int x, int y) {
        if (!ClientConfig.ENABLE_SCREEN_PARTICLES.getConfigValue()) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level != null && minecraft.player != null) {
            if (minecraft.isPaused()) {
                return;
            }
            if (!stack.isEmpty()) {
                var emitters = ItemScreenParticleEmitterHandler.EMITTERS.get(stack.getItem());
                if (emitters != null) {
                    var pose = poseStack.last().pose();
                    currentItemX = x + 8;
                    currentItemY = y + 8;
                    if (currentItemX == 8 && currentItemY == 8) {
                        int poseOffsetX = (int) pose.m30();
                        int poseOffsetY = (int) pose.m31();
                        currentItemX += poseOffsetX;
                        currentItemY += poseOffsetY;
                    }
                    for (ItemScreenParticleEmitterHandler.ItemScreenParticleEmitter emitter : emitters) {
                        spawnAndPullParticles(minecraft.level, emitter, stack, false).render(poseStack);
                        cachedLateItemParticles = spawnAndPullParticles(minecraft.level, emitter, stack, true);
                    }
                }
            }
        }
    }

    public static void renderLateParticles(PoseStack poseStack) {
        if (cachedLateItemParticles != null) {
            cachedLateItemParticles.render(poseStack);
            cachedLateItemParticles = null;
        }
    }

    public static ScreenParticleHolder spawnAndPullParticles(ClientLevel level, ItemScreenParticleEmitterHandler.ItemScreenParticleEmitter emitter, ItemStack stack, boolean isLate) {
        var vaultKey = new ScreenParticleItemStackVaultKey(renderingHotbar, isLate, currentItemX, currentItemY);
        var key = new ScreenParticleItemStackKey(renderingHotbar, isLate, stack);
        ScreenParticleHolder target = ITEM_PARTICLE_HOLDERS.computeIfAbsent(key, s -> new ScreenParticleHolder());
        pullFromParticleVault(vaultKey, stack, target, isLate);
        if (canSpawnParticles) {
            if (isLate) {
                emitter.spawnLateParticles(target, level, Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false), stack, currentItemX, currentItemY);
            } else {
                emitter.spawnEarlyParticles(target, level, Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false), stack, currentItemX, currentItemY);
            }
        }
        ACTIVELY_ACCESSED_KEYS.add(vaultKey);
        return target;
    }

    public static void pullFromParticleVault(ScreenParticleItemStackVaultKey cacheKey, ItemStack currentStack, ScreenParticleHolder target, boolean isRenderedAfterItem) {
        if (ITEM_PARTICLE_VAULTS.containsKey(cacheKey)) {
            var oldStack = ITEM_PARTICLE_VAULTS.get(cacheKey);
            if (oldStack != currentStack && oldStack.getItem().equals(currentStack.getItem())) {
                var oldKey = new ScreenParticleItemStackKey(renderingHotbar, isRenderedAfterItem, oldStack);
                var oldParticles = ITEM_PARTICLE_HOLDERS.get(oldKey);
                if (oldParticles != null) {
                    target.addFrom(oldParticles);
                }
                ITEM_PARTICLE_VAULTS.remove(cacheKey);
                ITEM_PARTICLE_HOLDERS.remove(oldKey);
            }
        }
        ITEM_PARTICLE_VAULTS.put(cacheKey, currentStack);
    }

    public static void clearParticles() {
        ITEM_PARTICLE_HOLDERS.values().forEach(ScreenParticleHolder::clear);
    }
}