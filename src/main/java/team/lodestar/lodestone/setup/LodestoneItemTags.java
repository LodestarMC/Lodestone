package team.lodestar.lodestone.setup;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class LodestoneItemTags {

    public static final TagKey<Item> NUGGETS_COPPER = forgeTag("nuggets/copper");
    public static final TagKey<Item> INGOTS_COPPER = forgeTag("ingots/copper");
    public static final TagKey<Item> NUGGETS_LEAD = forgeTag("nuggets/lead");
    public static final TagKey<Item> INGOTS_LEAD = forgeTag("ingots/lead");
    public static final TagKey<Item> NUGGETS_SILVER = forgeTag("nuggets/silver");
    public static final TagKey<Item> INGOTS_SILVER = forgeTag("ingots/silver");
    public static final TagKey<Item> NUGGETS_ALUMINUM = forgeTag("nuggets/aluminum");
    public static final TagKey<Item> INGOTS_ALUMINUM = forgeTag("ingots/aluminum");
    public static final TagKey<Item> NUGGETS_NICKEL = forgeTag("nuggets/nickel");
    public static final TagKey<Item> INGOTS_NICKEL = forgeTag("ingots/nickel");
    public static final TagKey<Item> NUGGETS_URANIUM = forgeTag("nuggets/uranium");
    public static final TagKey<Item> INGOTS_URANIUM = forgeTag("ingots/uranium");
    public static final TagKey<Item> NUGGETS_OSMIUM = forgeTag("nuggets/osmium");
    public static final TagKey<Item> INGOTS_OSMIUM = forgeTag("ingots/osmium");
    public static final TagKey<Item> NUGGETS_ZINC = forgeTag("nuggets/zinc");
    public static final TagKey<Item> INGOTS_ZINC = forgeTag("ingots/zinc");
    public static final TagKey<Item> NUGGETS_TIN = forgeTag("nuggets/tin");
    public static final TagKey<Item> INGOTS_TIN = forgeTag("ingots/tin");

    public static TagKey<Item> modTag(String path) {
        return TagKey.create(Registries.ITEM, new ResourceLocation(path));
    }

    public static TagKey<Item> forgeTag(String name) {
        return ItemTags.create(new ResourceLocation("forge", name));
    }
}