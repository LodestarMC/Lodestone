package team.lodestar.lodestone.compability;

import net.minecraftforge.fml.ModList;

public class CuriosCompat {
    public static boolean LOADED;

    public static void init() {
        LOADED = ModList.get().isLoaded("curios");
    }
}