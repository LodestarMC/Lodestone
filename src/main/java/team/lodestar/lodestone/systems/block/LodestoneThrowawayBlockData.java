package team.lodestar.lodestone.systems.block;

import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;

import java.util.HashMap;

/**
 * Various throwaway data stored in {@link #DATA_CACHE}, which is cleared after the game is finished loading.
 * All needsX parameters are intended to be used for datagen only.
 */
public class LodestoneThrowawayBlockData {

    public static HashMap<LodestoneBlockProperties, LodestoneThrowawayBlockData> DATA_CACHE = new HashMap<>();

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

    public LodestoneThrowawayBlockData() {

    }

    public LodestoneThrowawayBlockData needsPickaxe() {
        needsPickaxe = true;
        return this;
    }

    public LodestoneThrowawayBlockData needsAxe() {
        needsAxe = true;
        return this;
    }

    public LodestoneThrowawayBlockData needsShovel() {
        needsShovel = true;
        return this;
    }

    public LodestoneThrowawayBlockData needsHoe() {
        needsHoe = true;
        return this;
    }

    public LodestoneThrowawayBlockData needsStone() {
        needsStone = true;
        return this;
    }

    public LodestoneThrowawayBlockData needsIron() {
        needsIron = true;
        return this;
    }

    public LodestoneThrowawayBlockData needsDiamond() {
        needsDiamond = true;
        return this;
    }

    public LodestoneThrowawayBlockData isCutoutLayer() {
        isCutoutLayer = true;
        return this;
    }

    public LodestoneThrowawayBlockData hasCustomLoot() {
        hasCustomLoot = true;
        return this;
    }
}