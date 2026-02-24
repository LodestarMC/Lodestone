package team.lodestar.lodestone.handlers;

import net.minecraft.client.renderer.*;
import net.minecraft.core.registries.*;
import net.neoforged.fml.event.lifecycle.*;
import team.lodestar.lodestone.helpers.*;
import team.lodestar.lodestone.systems.block.*;

import java.util.*;

public class ThrowawayBlockDataHandler {

    public static HashMap<LodestoneBlockProperties, LodestoneThrowawayBlockData> THROWAWAY_DATA_CACHE = new HashMap<>();

    public static void wipeCaches(InterModEnqueueEvent event) {
        THROWAWAY_DATA_CACHE = null;
    }

    public static void setRenderLayers(FMLClientSetupEvent event) {
        DataHelper.getAll(BuiltInRegistries.BLOCK.stream().toList(),
                        b -> b.properties() instanceof LodestoneBlockProperties blockProperties && blockProperties.getThrowawayData().hasCustomRenderType())
                .forEach(b -> ItemBlockRenderTypes.setRenderLayer(b, ((LodestoneBlockProperties) b.properties()).getThrowawayData().getRenderType().get().get()));
    }
}
