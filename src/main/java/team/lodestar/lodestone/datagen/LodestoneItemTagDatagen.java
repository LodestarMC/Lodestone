package team.lodestar.lodestone.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.systems.datagen.providers.*;

import java.util.concurrent.CompletableFuture;

import static team.lodestar.lodestone.registry.common.tag.LodestoneItemTags.*;


public class LodestoneItemTagDatagen extends LodestoneItemTagsProvider {
    public LodestoneItemTagDatagen(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, CompletableFuture<TagLookup<Block>> blockProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, blockProvider, LodestoneLib.LODESTONE, existingFileHelper);
    }

    @Override
    public String getName() {
        return "Lodestone Item Tags";
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        tag(ENCHANTMENT_HOLDER).add(Items.BOOK, Items.ENCHANTED_BOOK);

        tag(MELEE_ENCHANTABLE).addTags(ItemTags.SWORD_ENCHANTABLE, AXE_ENCHANTABLE, ItemTags.TRIDENT_ENCHANTABLE, ItemTags.MACE_ENCHANTABLE, KNIFE_ENCHANTABLE);
        tag(RANGED_ENCHANTABLE).addTags(ItemTags.CROSSBOW_ENCHANTABLE, ItemTags.BOW_ENCHANTABLE);
        tag(WEAPON_ENCHANTABLE).addTags(MELEE_ENCHANTABLE, RANGED_ENCHANTABLE);
        tag(SHIELD_ENCHANTABLE).add(Items.SHIELD);
        tag(AXE_ENCHANTABLE).addTags(ItemTags.AXES);
        tag(KNIFE_ENCHANTABLE).addTags(FD_KNIVES, C_KNIVES);

        tag(FD_KNIVES);
        tag(C_KNIVES);

        tag(NUGGETS_COPPER);
        tag(INGOTS_COPPER).add(Items.COPPER_INGOT);
        tag(NUGGETS_LEAD);
        tag(INGOTS_LEAD);
        tag(NUGGETS_SILVER);
        tag(INGOTS_SILVER);
        tag(NUGGETS_ALUMINUM);
        tag(INGOTS_ALUMINUM);
        tag(NUGGETS_NICKEL);
        tag(INGOTS_NICKEL);
        tag(NUGGETS_URANIUM);
        tag(INGOTS_URANIUM);
        tag(NUGGETS_OSMIUM);
        tag(INGOTS_OSMIUM);
        tag(NUGGETS_ZINC);
        tag(INGOTS_ZINC);
        tag(NUGGETS_TIN);
        tag(INGOTS_TIN);
        tag(NUGGETS_COBALT);
        tag(INGOTS_COBALT);
    }
}