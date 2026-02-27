package team.lodestar.lodestone.datagen.implementation;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.datagen.providers.tag.LodestoneItemTagsSystem;
import team.lodestar.lodestone.registry.common.tag.LodestoneItemTags;

import java.util.concurrent.CompletableFuture;

import static team.lodestar.lodestone.registry.common.tag.LodestoneItemTags.*;


@SuppressWarnings("NullableProblems")
public class LodestoneItemTagDatagen extends LodestoneItemTagsSystem {
    public LodestoneItemTagDatagen(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagLookup<Block>> blockProvider, ExistingFileHelper existingFileHelper) {
        super(output, provider, blockProvider, LodestoneLib.LODESTONE, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Lodestone Item Tags";
    }

    @SuppressWarnings({"unchecked"})
    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(LodestoneItemTags.ENCHANTMENT_HOLDER).add(Items.BOOK, Items.ENCHANTED_BOOK);

        tag(LodestoneItemTags.MELEE_ENCHANTABLE).addTags(ItemTags.SWORD_ENCHANTABLE, LodestoneItemTags.AXE_ENCHANTABLE, ItemTags.TRIDENT_ENCHANTABLE, ItemTags.MACE_ENCHANTABLE, LodestoneItemTags.KNIFE_ENCHANTABLE);
        tag(LodestoneItemTags.RANGED_ENCHANTABLE).addTags(ItemTags.CROSSBOW_ENCHANTABLE, ItemTags.BOW_ENCHANTABLE);
        tag(LodestoneItemTags.WEAPON_ENCHANTABLE).addTags(LodestoneItemTags.MELEE_ENCHANTABLE, LodestoneItemTags.RANGED_ENCHANTABLE);
        tag(LodestoneItemTags.SHIELD_ENCHANTABLE).add(Items.SHIELD);
        tag(LodestoneItemTags.AXE_ENCHANTABLE).addTags(ItemTags.AXES);
        tag(LodestoneItemTags.KNIFE_ENCHANTABLE).addTags(LodestoneItemTags.FD_KNIVES, LodestoneItemTags.C_KNIVES);

        tag(LodestoneItemTags.FD_KNIVES);
        tag(LodestoneItemTags.C_KNIVES);

        tag(LodestoneItemTags.NUGGETS_COPPER);
        tag(LodestoneItemTags.INGOTS_COPPER).add(Items.COPPER_INGOT);
        tag(LodestoneItemTags.NUGGETS_LEAD);
        tag(LodestoneItemTags.INGOTS_LEAD);
        tag(LodestoneItemTags.NUGGETS_SILVER);
        tag(LodestoneItemTags.INGOTS_SILVER);
        tag(LodestoneItemTags.NUGGETS_ALUMINUM);
        tag(LodestoneItemTags.INGOTS_ALUMINUM);
        tag(LodestoneItemTags.NUGGETS_NICKEL);
        tag(LodestoneItemTags.INGOTS_NICKEL);
        tag(LodestoneItemTags.NUGGETS_URANIUM);
        tag(LodestoneItemTags.INGOTS_URANIUM);
        tag(LodestoneItemTags.NUGGETS_OSMIUM);
        tag(LodestoneItemTags.INGOTS_OSMIUM);
        tag(LodestoneItemTags.NUGGETS_ZINC);
        tag(LodestoneItemTags.INGOTS_ZINC);
        tag(LodestoneItemTags.NUGGETS_TIN);
        tag(LodestoneItemTags.INGOTS_TIN);
        tag(LodestoneItemTags.NUGGETS_COBALT);
        tag(LodestoneItemTags.INGOTS_COBALT);
    }
}