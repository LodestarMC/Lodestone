package team.lodestar.lodestone.common.compability;

import net.neoforged.fml.ModList;
import team.lodestar.lodestone.*;
import team.lodestar.lodestone.handlers.item.*;
import team.lodestar.lodestone.helpers.*;

public class CuriosCompat {
    public static boolean LOADED;

    public static void init() {
        LOADED = ModList.get().isLoaded("curios");
        if (LOADED) {
            LoadedOnly.init();
        }
    }

    public static class LoadedOnly {

        public static final ItemizedEventHandler.ItemizedEventResponderLookup CURIOS = new ItemizedEventHandler.ItemizedEventResponderLookup(LodestoneLib.lodestonePath("curios"), CurioHelper::getEquippedCurios);

        public static void init() {
            ItemizedEventHandler.registerLookup(CURIOS);
        }
    }
}