package team.lodestar.lodestone.systems.datagen.itemsmith;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.systems.datagen.providers.LodestoneBlockStateProvider;
import team.lodestar.lodestone.systems.datagen.providers.LodestoneItemModelProvider;
import team.lodestar.lodestone.systems.datagen.statesmith.AbstractBlockStateSmith;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ItemModelSmith extends AbstractItemModelSmith {

    public final ItemModelSupplier modelSupplier;

    public ItemModelSmith(ItemModelSupplier modelSupplier) {
        this.modelSupplier = modelSupplier;
    }

    @SafeVarargs
    public final void act(ItemModelSmithData data, Supplier<Item>... items) {
        for (Supplier<Item> item : items) {
            act(data, item);
        }
    }

    public void act(ItemModelSmithData data, Collection<Supplier<Item>> items) {
        items.forEach(r -> act(data, r));
    }

    private void act(ItemModelSmithData data, Supplier<Item> registryObject) {
        Item item = registryObject.get();
        modelSupplier.act(item, data.provider);
        data.consumer.accept(registryObject);
    }

    public interface ItemModelSupplier {
        void act(Item item, LodestoneItemModelProvider provider);
    }
}
