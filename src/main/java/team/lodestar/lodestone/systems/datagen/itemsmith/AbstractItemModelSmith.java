package team.lodestar.lodestone.systems.datagen.itemsmith;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.registries.RegistryObject;
import team.lodestar.lodestone.systems.datagen.providers.LodestoneItemModelProvider;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class AbstractItemModelSmith {

    public static class ItemModelSmithData {
        public final LodestoneItemModelProvider provider;
        public final Consumer<Supplier<Item>> consumer;

        public ItemModelSmithData(LodestoneItemModelProvider provider, Consumer<Supplier<Item>> consumer) {
            this.provider = provider;
            this.consumer = consumer;
        }
    }
}
