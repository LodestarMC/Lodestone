package team.lodestar.lodestone.setup;

import com.mojang.serialization.Codec;
import team.lodestar.lodestone.systems.worldgen.ChancePlacementFilter;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import team.lodestar.lodestone.systems.worldgen.DimensionPlacementFilter;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
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
        return Registry.register(Registry.PLACEMENT_MODIFIERS, name, () -> codec);
    }
}