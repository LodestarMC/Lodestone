package team.lodestar.lodestone.handlers.screenparticle;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import team.lodestar.lodestone.config.ClientConfig;
import team.lodestar.lodestone.helpers.DataHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ParticleEmitterHandler {
    public static final Map<Item, ItemParticleSupplier> EMITTERS = new HashMap<>();

    public static final ArrayList<StackTracker> RENDERED_STACKS = new ArrayList<>();

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
                ItemParticleSupplier emitter = EMITTERS.get(stack.getItem());
                if (emitter != null) {
                    PoseStack posestack = RenderSystem.getModelViewStack();
                    Matrix4f last = posestack.last().pose();
                    float x = last.m03;
                    float y = last.m13;
                    if (ScreenParticleHandler.canSpawnParticles) {
                        float gameTime = minecraft.level.getGameTime() + Minecraft.getInstance().timer.partialTick;
                        emitter.spawnParticles(minecraft.level, gameTime, stack, x, y);
                    }
                    RENDERED_STACKS.add(new StackTracker(stack, x, y));
                }
            }
        }
    }

    public static void registerParticleEmitters(FMLClientSetupEvent event) {
        DataHelper.takeAll(new ArrayList<>(ForgeRegistries.ITEMS.getValues()), i -> i instanceof ItemParticleSupplier).forEach(i -> {
                    ItemParticleSupplier emitter = (ItemParticleSupplier) i;
                    registerItemParticleEmitter(i, emitter);
                }
        );
    }

    public static void registerItemParticleEmitter(Item item, ItemParticleSupplier emitter) {
        EMITTERS.put(item, emitter);
    }

    public static void registerItemParticleEmitter(ItemParticleSupplier emitter, Item... items) {
        for (Item item : items) {
            EMITTERS.put(item, emitter);
        }
    }

    public record StackTracker(ItemStack stack, float xOrigin, float yOrigin) {

    }
    public interface ItemParticleSupplier {
        void spawnParticles(Level level, float gameTime, ItemStack stack, float x, float y);
    }
}