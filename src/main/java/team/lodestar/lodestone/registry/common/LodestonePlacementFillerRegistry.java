package team.lodestar.lodestone.registry.common;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import team.lodestar.lodestone.systems.worldgen.ChancePlacementFilter;
import team.lodestar.lodestone.systems.worldgen.DimensionPlacementFilter;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class LodestonePlacementFillerRegistry {

    public static PlacementModifierType<ChancePlacementFilter> CHANCE;
    public static PlacementModifierType<DimensionPlacementFilter> DIMENSION;

    @SubscribeEvent
    public static void registerTypes(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            CHANCE = register("lodestone:chance", ChancePlacementFilter.CODEC);
            DIMENSION = register("lodestone:dimension", DimensionPlacementFilter.CODEC);
        });
    }

    public static <P extends PlacementModifier> PlacementModifierType<P> register(String name, Codec<P> codec) {
        return Registry.register(BuiltInRegistries.PLACEMENT_MODIFIER_TYPE, name, () -> codec);
    }
}