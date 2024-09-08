package team.lodestar.lodestone;

import io.github.fabricators_of_create.porting_lib.entity.events.PlayerEvents;
import io.github.fabricators_of_create.porting_lib.entity.events.PlayerTickEvents;
import io.github.fabricators_of_create.porting_lib.entity.events.living.LivingHurtEvent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.impl.datagen.FabricDataGenHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import team.lodestar.lodestone.compability.TrinketsCompat;
import team.lodestar.lodestone.component.LodestonePlayerComponent;
import team.lodestar.lodestone.events.LodestoneInteractionEvent;
import team.lodestar.lodestone.handlers.ItemEventHandler;
import team.lodestar.lodestone.handlers.LodestoneAttributeEventHandler;
import team.lodestar.lodestone.handlers.PlacementAssistantHandler;
import team.lodestar.lodestone.handlers.WorldEventHandler;
import team.lodestar.lodestone.helpers.ShadersHelper;
import team.lodestar.lodestone.registry.common.*;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.item.LodestoneItemProperties;

public class LodestoneLib implements ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger();
    public static final boolean debugMode = true;

    public static final String LODESTONE = "lodestone";
    public static final RandomSource RANDOM = RandomSource.create();

    public static ResourceLocation lodestonePath(String path) {
        return new ResourceLocation(LODESTONE, path);
    }

    @Override
    public void onInitialize() {

        LodestoneArgumentTypeRegistry.registerArgumentTypes();
        LodestoneCommandRegistry.registerCommands();
        LodestonePlacementFillerRegistry.registerTypes();
        LodestonePacketRegistry.registerPackets();
        LodestonePaintingRegistry.register();

        LodestoneBlockEntityRegistry.BLOCK_ENTITY_TYPES.register();
        LodestoneParticleRegistry.PARTICLES.register();
        LodestoneAttributeRegistry.ATTRIBUTES.register();
        LodestoneRecipeSerializerRegistry.RECIPE_SERIALIZERS.register();

        PlayerEvents.ON_JOIN_WORLD.register(WorldEventHandler::playerJoin);
        PlayerEvents.ON_JOIN_WORLD.register(LodestonePlayerComponent::playerJoin);

        PlacementAssistantHandler.registerPlacementAssistants();
        LodestoneInteractionEvent.RIGHT_CLICK_BLOCK.register(PlacementAssistantHandler::placeBlock);
        PlayerTickEvents.END.register(LodestonePlayerComponent::playerTick);
        ServerLivingEntityEvents.ALLOW_DEATH.register(ItemEventHandler::respondToDeath);
        LivingHurtEvent.HURT.register(ItemEventHandler::respondToHurt);
        LivingHurtEvent.HURT.register(LodestoneAttributeEventHandler::processAttributes);

        TrinketsCompat.init();
        ShadersHelper.init();

        ItemGroupEvents.MODIFY_ENTRIES_ALL.register(LodestoneItemProperties::populateItemGroups);

        if (!FabricDataGenHelper.ENABLED) {
            //TODO ThrowawayBlockDataHandler.wipeCache();
        }
    }

    public static void debug() {
        debug("");
    }

    public static void debug(String message) {
        if (debugMode) {
            // Retrieve the class name of the caller
            String className = new Exception().getStackTrace()[1].getClassName();
            try {
                Class<?> clazz = Class.forName(className);
                LOGGER.info("Debugging: {}. {}", clazz.getSimpleName(), message);
            } catch (ClassNotFoundException e) {
                LOGGER.error("Class not found for name: {}", className, e);
            }
        }
    }
}