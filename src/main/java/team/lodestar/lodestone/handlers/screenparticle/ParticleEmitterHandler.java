package team.lodestar.lodestone.handlers.screenparticle;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import team.lodestar.lodestone.helpers.DataHelper;
import team.lodestar.lodestone.setup.LodestoneScreenParticleRegistry;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.rendering.particle.ParticleBuilders;
import team.lodestar.lodestone.systems.rendering.particle.screen.ScreenParticleRenderType;
import team.lodestar.lodestone.systems.rendering.particle.screen.base.ScreenParticle;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ParticleEmitterHandler {
    public static final Map<Item, ItemParticleSupplier> EMITTERS = new HashMap<>();

    public static void registerParticleEmitters(FMLClientSetupEvent event) {
        registerItemParticleEmitter(Items.DIAMOND, (target, level, partialTicks, stack, x, y) -> {
            float gameTime = level.getGameTime() + partialTicks;
            Color firstColor = Color.RED;
            Color secondColor = Color.YELLOW;
            ParticleBuilders.create(LodestoneScreenParticleRegistry.STAR, target)
                    .setAlpha(0.11f, 0f)
                    .setLifetime(7)
                    .setScale((float) (5.75f + Math.sin(gameTime * 0.05f) * 0.125f), 0)
                    .setColor(firstColor, secondColor)
                    .setColorCoefficient(1.25f)
                    .randomOffset(0.05f)
                    .setSpinOffset(0.025f * gameTime % 6.28f)
                    .setSpin(0, 1)
                    .setSpinEasing(Easing.EXPO_IN_OUT)
                    .setAlphaEasing(Easing.QUINTIC_IN)
                    .repeat(x, y, 1)
                    .setScale((float) (0.75f - Math.sin(gameTime * 0.075f) * 0.125f), 0)
                    .setColor(secondColor, firstColor)
                    .setSpinOffset(0.785f - 0.01f * gameTime % 6.28f)
                    .repeat(x, y, 1);

        });



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
        void spawnParticles(HashMap<ScreenParticleRenderType, ArrayList<ScreenParticle>> target, Level level, float partialTick, ItemStack stack, float x, float y);
    }
}