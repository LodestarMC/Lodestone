package team.lodestar.lodestone.compability;

import net.neoforged.fml.ModList;
import team.lodestar.lodestone.*;
import team.lodestar.lodestone.handlers.item_events.EventResponderSource;
import team.lodestar.lodestone.handlers.item_events.ItemEventHandler;
import team.lodestar.lodestone.handlers.item_events.ItemEventResponderSource;
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

        public static final EventResponderSource CURIOS = new ItemEventResponderSource(LodestoneLib.lodestonePath("curios"), CurioHelper::getEquippedCurios);

        public static void init() {
            ItemEventHandler.registerSource(CURIOS);
        }
    }
}