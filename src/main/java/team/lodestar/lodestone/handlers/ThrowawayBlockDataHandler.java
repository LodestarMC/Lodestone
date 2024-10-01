package team.lodestar.lodestone.handlers;

import team.lodestar.lodestone.systems.block.LodestoneBlockProperties;
import team.lodestar.lodestone.systems.block.LodestoneThrowawayBlockData;
import team.lodestar.lodestone.systems.datagen.LodestoneDatagenBlockData;

import java.util.HashMap;

public class ThrowawayBlockDataHandler {

    public static HashMap<LodestoneBlockProperties, LodestoneThrowawayBlockData> THROWAWAY_DATA_CACHE = new HashMap<>();
    public static HashMap<LodestoneBlockProperties, LodestoneDatagenBlockData> DATAGEN_DATA_CACHE = new HashMap<>();

    public static void wipeCache() {
        THROWAWAY_DATA_CACHE = new HashMap<>();
        DATAGEN_DATA_CACHE = new HashMap<>();
    }
}
