package team.lodestar.lodestone.registry.common;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.decoration.PaintingVariant;
import team.lodestar.lodestone.LodestoneLib;


public class LodestonePaintingRegistry {
    public static final LazyRegistrar<PaintingVariant> PAINTING_MOTIVES = LazyRegistrar.create(BuiltInRegistries.PAINTING_VARIANT, LodestoneLib.LODESTONE);

    public static void register() {
        PAINTING_MOTIVES.register("lefunny", () -> new PaintingVariant(64, 64));
        PAINTING_MOTIVES.register("microfunny", () -> new PaintingVariant(16, 16));
    }
}