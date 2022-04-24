package com.sammy.ortus.data;

import com.sammy.ortus.OrtusLib;
import com.sammy.ortus.helpers.DataHelper;
import net.minecraft.data.DataGenerator;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.sammy.ortus.setup.OrtusAttributes.ATTRIBUTES;

public class OrtusLang extends LanguageProvider {
    public OrtusLang(DataGenerator gen) {
        super(gen, OrtusLib.ORTUS, "en_us");
    }

    @Override
    protected void addTranslations() {
        Set<RegistryObject<Attribute>> attributes = new HashSet<>(ATTRIBUTES.getEntries());

        attributes.forEach(a -> {
            String name = DataHelper.toTitleCase(a.getId().getPath(), "_");
            add("attribute.name.ortus." + a.get().getRegistryName().getPath(), name);
        });
    }

    @Override
    public String getName() {
        return "Ortus Lang Entries";
    }
}