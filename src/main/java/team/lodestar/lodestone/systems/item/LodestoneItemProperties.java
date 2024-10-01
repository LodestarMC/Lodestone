package team.lodestar.lodestone.systems.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;

import java.util.*;

public class LodestoneItemProperties extends Item.Properties {
    public static final Map<ResourceKey<CreativeModeTab>, List<ResourceLocation>> TAB_SORTING = new HashMap<>();

    public final ResourceKey<CreativeModeTab> tab;

    public LodestoneItemProperties(ResourceKey<CreativeModeTab> tab) {
        this.tab = tab;
    }


    public static void populateItemGroups(CreativeModeTab tab, FabricItemGroupEntries entry) {
        Optional<ResourceKey<CreativeModeTab>> opt = BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(tab);
        if (opt.isPresent()) {
            if (TAB_SORTING.containsKey(opt.get())) {
                TAB_SORTING.get(opt.get()).stream().map(BuiltInRegistries.ITEM::get).forEach(entry::accept);
            }
        }
    }
}
