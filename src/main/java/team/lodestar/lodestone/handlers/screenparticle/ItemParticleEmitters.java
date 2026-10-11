package team.lodestar.lodestone.handlers.screenparticle;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import team.lodestar.lodestone.helpers.DataHelper;
import team.lodestar.lodestone.modules.rendering.particle.standard.screen.ScreenParticleHolder;

import java.util.*;

public class ItemParticleEmitters {

    public static final Map<Item, List<ItemParticleEmitter>> EMITTERS = new HashMap<>();

    public static void registerParticleEmitters(FMLClientSetupEvent event) {
        for (Item item : BuiltInRegistries.ITEM) {
            if (item instanceof ItemParticleEmitter supplier) {
                registerItemParticleEmitter(item, supplier);
            }
        }
    }

    public static void registerItemParticleEmitter(Item item, ItemParticleEmitter emitter) {
        if (EMITTERS.containsKey(item)) {
            EMITTERS.get(item).add(emitter);
        } else {
            EMITTERS.put(item, new ArrayList<>(List.of(emitter)));
        }
    }

    public static void registerItemParticleEmitter(ItemParticleEmitter emitter, Item... items) {
        for (Item item : items) {
            registerItemParticleEmitter(item, emitter);
        }
    }

    public interface ItemParticleEmitter {
        default void spawnEarlyParticles(ScreenParticleHolder target, Level level, float partialTicks, ItemStack stack, float x, float y) {
        }

        default void spawnLateParticles(ScreenParticleHolder target, Level level, float partialTicks, ItemStack stack, float x, float y) {
        }
    }
}