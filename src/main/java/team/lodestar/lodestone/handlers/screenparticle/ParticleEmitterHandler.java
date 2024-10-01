package team.lodestar.lodestone.handlers.screenparticle;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import team.lodestar.lodestone.helpers.DataHelper;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleHolder;

import java.util.*;

public class ParticleEmitterHandler {
    public static final Map<Item, List<ItemParticleSupplier>> EMITTERS = new HashMap<>();

    public static void registerParticleEmitters() {
        DataHelper.getAll(BuiltInRegistries.ITEM.stream().toList(), i -> i instanceof ItemParticleSupplier).forEach(i -> {
                    ItemParticleSupplier emitter = (ItemParticleSupplier) i;
                    registerItemParticleEmitter(i, emitter);
                }
        );
    }

    public static void registerItemParticleEmitter(Item item, ItemParticleSupplier emitter) {
        if (EMITTERS.containsKey(item)) {
            EMITTERS.get(item).add(emitter);
        }
        else {
            EMITTERS.put(item, new ArrayList<>(List.of(emitter)));
        }
    }

    public static void registerItemParticleEmitter(ItemParticleSupplier emitter, Item... items) {
        for (Item item : items) {
            registerItemParticleEmitter(item, emitter);
        }
    }

    public interface ItemParticleSupplier {
        default void spawnEarlyParticles(ScreenParticleHolder target, Level level, float partialTick, ItemStack stack, float x, float y) {
        }

        default void spawnLateParticles(ScreenParticleHolder target, Level level, float partialTick, ItemStack stack, float x, float y) {
        }
    }
}