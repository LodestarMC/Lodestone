package team.lodestar.lodestone.datagen.smith.itemmodel;

import net.minecraft.world.item.Item;
import team.lodestar.lodestone.datagen.providers.item.LodestoneItemModelSystem;

import java.util.function.Consumer;
import java.util.function.Supplier;

public record ItemModelSmithProcessor(LodestoneItemModelSystem provider, Consumer<Supplier<? extends Item>> consumer) {
}
