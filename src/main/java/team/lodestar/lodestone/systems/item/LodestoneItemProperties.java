package team.lodestar.lodestone.systems.item;

import net.minecraft.resources.*;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

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

    public static void populateItemGroups(BuildCreativeModeTabContentsEvent event) {
        final ResourceKey<CreativeModeTab> tabKey = event.getTabKey();
        if (TAB_SORTING.containsKey(tabKey)) {
            TAB_SORTING.get(tabKey).stream().map(ForgeRegistries.ITEMS::getValue)
                    .filter(Objects::nonNull)
                    .forEach(event::accept);
        }
    }
}
