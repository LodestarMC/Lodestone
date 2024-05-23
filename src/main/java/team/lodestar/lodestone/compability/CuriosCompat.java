package team.lodestar.lodestone.compability;


import net.fabricmc.loader.api.FabricLoader;

public class CuriosCompat {
    public static boolean LOADED;

    public static void init() {
        LOADED = FabricLoader.getInstance().isModLoaded("trinkets");
    }
}