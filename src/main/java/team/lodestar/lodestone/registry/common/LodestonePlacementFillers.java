package team.lodestar.lodestone.registry.common;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import team.lodestar.lodestone.systems.worldgen.ChancePlacementFilter;
import team.lodestar.lodestone.systems.worldgen.DimensionPlacementFilter;

import static team.lodestar.lodestone.LodestoneLib.LODESTONE;

public class LodestonePlacementFillers {

    public static final DeferredRegister<PlacementModifierType<?>> MODIFIERS =
            DeferredRegister.create(BuiltInRegistries.PLACEMENT_MODIFIER_TYPE, LODESTONE);


    public static final DeferredHolder<PlacementModifierType<?>, PlacementModifierType<ChancePlacementFilter>> CHANCE =
            MODIFIERS.register("chance", () -> () ->(ChancePlacementFilter.CODEC));


    public static final DeferredHolder<PlacementModifierType<?>, PlacementModifierType<DimensionPlacementFilter>> DIMENSION =
            MODIFIERS.register("dimension", () -> () -> (DimensionPlacementFilter.CODEC));

}