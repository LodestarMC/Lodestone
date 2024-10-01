package team.lodestar.lodestone.registry.common.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class LodestoneItemTags {

    public static final TagKey<Item> NUGGETS_COPPER = cTag("nuggets/copper");
    public static final TagKey<Item> INGOTS_COPPER = cTag("ingots/copper");
    public static final TagKey<Item> NUGGETS_LEAD = cTag("nuggets/lead");
    public static final TagKey<Item> INGOTS_LEAD = cTag("ingots/lead");
    public static final TagKey<Item> NUGGETS_SILVER = cTag("nuggets/silver");
    public static final TagKey<Item> INGOTS_SILVER = cTag("ingots/silver");
    public static final TagKey<Item> NUGGETS_ALUMINUM = cTag("nuggets/aluminum");
    public static final TagKey<Item> INGOTS_ALUMINUM = cTag("ingots/aluminum");
    public static final TagKey<Item> NUGGETS_NICKEL = cTag("nuggets/nickel");
    public static final TagKey<Item> INGOTS_NICKEL = cTag("ingots/nickel");
    public static final TagKey<Item> NUGGETS_URANIUM = cTag("nuggets/uranium");
    public static final TagKey<Item> INGOTS_URANIUM = cTag("ingots/uranium");
    public static final TagKey<Item> NUGGETS_OSMIUM = cTag("nuggets/osmium");
    public static final TagKey<Item> INGOTS_OSMIUM = cTag("ingots/osmium");
    public static final TagKey<Item> NUGGETS_ZINC = cTag("nuggets/zinc");
    public static final TagKey<Item> INGOTS_ZINC = cTag("ingots/zinc");
    public static final TagKey<Item> NUGGETS_TIN = cTag("nuggets/tin");
    public static final TagKey<Item> INGOTS_TIN = cTag("ingots/tin");

    public static TagKey<Item> modTag(String path) {
        return TagKey.create(Registries.ITEM, ResourceLocation.parse(path));
    }

    public static TagKey<Item> cTag(String name) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", name));
    }
}