package team.lodestar.lodestone.handlers.screenparticle;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import team.lodestar.lodestone.helpers.DataHelper;
import team.lodestar.lodestone.systems.particle.screen.*;
import team.lodestar.lodestone.systems.particle.screen.base.ScreenParticle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ParticleEmitterHandler {
    public static final Map<Item, ItemParticleSupplier> EMITTERS = new HashMap<>();

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

    public interface ItemParticleSupplier {
        void spawnEarlyParticles(ScreenParticleHolder target, Level level, float partialTick, ItemStack stack, float x, float y);
        void spawnLateParticles(ScreenParticleHolder target, Level level, float partialTick, ItemStack stack, float x, float y);
    }
}