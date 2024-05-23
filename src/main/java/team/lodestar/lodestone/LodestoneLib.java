package team.lodestar.lodestone;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import org.apache.logging.log4j.*;
import team.lodestar.lodestone.compability.*;
import team.lodestar.lodestone.config.*;
import team.lodestar.lodestone.data.*;
import team.lodestar.lodestone.registry.common.*;
import team.lodestar.lodestone.registry.common.particle.*;

import java.util.concurrent.*;

public class LodestoneLib implements ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger();
    public static final String LODESTONE = "lodestone";
    public static final RandomSource RANDOM = RandomSource.create();

    public static ResourceLocation lodestonePath(String path) {
        return new ResourceLocation(LODESTONE, path);
    }

    @Override
    public void onInitialize() {

        LodestoneCommandRegistry.registerCommands();
        LodestonePlacementFillerRegistry.registerTypes();
        LodestonePacketRegistry.registerPackets();
        LodestonePaintingRegistry.register();

        LodestoneBlockEntityRegistry.BLOCK_ENTITY_TYPES.register();
        LodestoneParticleRegistry.PARTICLES.register();
        LodestoneAttributeRegistry.ATTRIBUTES.register();
        LodestoneRecipeSerializerRegistry.RECIPE_SERIALIZERS.register();

        CuriosCompat.init();
    }
}