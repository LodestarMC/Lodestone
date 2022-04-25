package com.sammy.ortus.systems.block;

import com.sammy.ortus.helpers.DataHelper;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;

/**
 * Various throwaway data stored in {@link #DATA_CACHE}, which is cleared after the game is finished loading.
 * All needsX parameters are intended to be used for datagen only.
 */
public class OrtusThrowawayBlockData {

    public static HashMap<OrtusBlockProperties, OrtusThrowawayBlockData> DATA_CACHE = new HashMap<>();

    public static void wipeCache(InterModEnqueueEvent event) {
        DATA_CACHE.clear();
    }
    public boolean needsPickaxe;
    public boolean needsAxe;
    public boolean needsShovel;
    public boolean needsHoe;

    public boolean needsStone;
    public boolean needsIron;
    public boolean needsDiamond;

    public boolean isCutoutLayer;
    public boolean hasCustomLoot;

    public OrtusThrowawayBlockData() {

    }

    public OrtusThrowawayBlockData needsPickaxe() {
        needsPickaxe = true;
        return this;
    }

    public OrtusThrowawayBlockData needsAxe() {
        needsAxe = true;
        return this;
    }

    public OrtusThrowawayBlockData needsShovel() {
        needsShovel = true;
        return this;
    }

    public OrtusThrowawayBlockData needsHoe() {
        needsHoe = true;
        return this;
    }

    public OrtusThrowawayBlockData needsStone() {
        needsStone = true;
        return this;
    }

    public OrtusThrowawayBlockData needsIron() {
        needsIron = true;
        return this;
    }

    public OrtusThrowawayBlockData needsDiamond() {
        needsDiamond = true;
        return this;
    }

    public OrtusThrowawayBlockData isCutoutLayer() {
        isCutoutLayer = true;
        return this;
    }

    public OrtusThrowawayBlockData hasCustomLoot() {
        hasCustomLoot = true;
        return this;
    }
}