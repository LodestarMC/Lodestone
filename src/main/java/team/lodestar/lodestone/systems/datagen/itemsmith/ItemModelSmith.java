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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
        List.of(items).forEach(data.consumer);
    }

    public void act(ItemModelSmithData data, Collection<Supplier<Item>> items) {
        items.forEach(r -> act(data, r));
        new ArrayList<>(items).forEach(data.consumer);
    }

    private void act(ItemModelSmithData data, Supplier<Item> registryObject) {
        Item item = registryObject.get();
        modelSupplier.act(item, data.provider);
    }

    public void act(Supplier<Item> registryObject, LodestoneItemModelProvider provider) {
        Item item = registryObject.get();
        modelSupplier.act(item, provider);
    }

    public interface ItemModelSupplier {
        void act(Item item, LodestoneItemModelProvider provider);
    }
}