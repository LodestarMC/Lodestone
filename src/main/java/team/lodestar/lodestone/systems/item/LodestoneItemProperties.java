package team.lodestar.lodestone.systems.item;

import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.*;
import net.minecraft.world.item.*;

import java.util.*;

public class LodestoneItemProperties extends Item.Properties {
    public static final Map<ResourceKey<CreativeModeTab>, List<ResourceLocation>> TAB_SORTING = new HashMap<>();

    public final ResourceKey<CreativeModeTab> tab;

    public LodestoneItemProperties(RegistryObject<CreativeModeTab> registryObject) {
        this(registryObject.getKey());
    }

    public LodestoneItemProperties(ResourceKey<CreativeModeTab> tab) {
        this.tab = tab;
    }

    public static void populateItemGroups(CreativeModeTab tab, FabricItemGroupEntries entry) {
        /*TODO fabric is wayy off
        for (CreativeModeTab all : CreativeModeTabs.allTabs()) {
            if (TAB_SORTING.containsKey(all.)) {
                TAB_SORTING.get(tabKey).stream().map(BuiltInRegistries.ITEM::get).forEach(entry::accept);
            }
        }

         */
    }
}
