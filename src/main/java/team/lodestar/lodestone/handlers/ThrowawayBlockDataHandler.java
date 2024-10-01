package team.lodestar.lodestone.handlers;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import team.lodestar.lodestone.helpers.DataHelper;
import team.lodestar.lodestone.systems.block.LodestoneBlockProperties;
import team.lodestar.lodestone.systems.block.LodestoneThrowawayBlockData;
import team.lodestar.lodestone.systems.datagen.LodestoneDatagenBlockData;

import java.util.HashMap;

public class ThrowawayBlockDataHandler {

    public static HashMap<LodestoneBlockProperties, LodestoneThrowawayBlockData> THROWAWAY_DATA_CACHE = new HashMap<>();
    public static HashMap<LodestoneBlockProperties, LodestoneDatagenBlockData> DATAGEN_DATA_CACHE = new HashMap<>();

    public static void wipeCache(InterModEnqueueEvent event) {
        THROWAWAY_DATA_CACHE = null;
        DATAGEN_DATA_CACHE = null;
    }

    public static void setRenderLayers(FMLClientSetupEvent event) {
        DataHelper.getAll(BuiltInRegistries.BLOCK.stream().toList(),
                        b -> b.properties() instanceof LodestoneBlockProperties blockProperties && blockProperties.getThrowawayData().hasCustomRenderType())
                .forEach(b -> ItemBlockRenderTypes.setRenderLayer(b, ((LodestoneBlockProperties) b.properties()).getThrowawayData().getRenderType().get().get()));
    }
}
