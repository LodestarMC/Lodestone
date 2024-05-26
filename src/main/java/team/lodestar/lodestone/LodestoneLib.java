package team.lodestar.lodestone;

import io.github.fabricators_of_create.porting_lib.entity.events.*;
import io.github.fabricators_of_create.porting_lib.entity.events.living.LivingHurtEvent;
import io.github.fabricators_of_create.porting_lib.tags.data.DataGenerators;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.impl.datagen.FabricDataGenHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import org.apache.logging.log4j.*;
import team.lodestar.lodestone.compability.*;
import team.lodestar.lodestone.component.LodestonePlayerComponent;
import team.lodestar.lodestone.events.EntityAttributeModificationEvent;
import team.lodestar.lodestone.events.LodestoneInteractionEvent;
import team.lodestar.lodestone.handlers.*;
import team.lodestar.lodestone.registry.common.*;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.item.LodestoneItemProperties;

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
        EntityAttributeModificationEvent.ADD.register(LodestoneAttributeRegistry::modifyEntityAttributes);

        PlayerEvents.ON_JOIN_WORLD.register(WorldEventHandler::playerJoin);
        PlayerEvents.ON_JOIN_WORLD.register(LodestonePlayerComponent::playerJoin);

        PlacementAssistantHandler.registerPlacementAssistants();
        LodestoneInteractionEvent.RIGHT_CLICK_BLOCK.register(PlacementAssistantHandler::placeBlock);
        PlayerTickEvents.END.register(LodestonePlayerComponent::playerTick);
        ServerLivingEntityEvents.ALLOW_DEATH.register(ItemEventHandler::respondToDeath);
        LivingHurtEvent.HURT.register(ItemEventHandler::respondToHurt);
        LivingHurtEvent.HURT.register(LodestoneAttributeEventHandler::processAttributes);

        TrinketsCompat.init();
        ItemGroupEvents.MODIFY_ENTRIES_ALL.register(LodestoneItemProperties::populateItemGroups);

        if (!FabricDataGenHelper.ENABLED) {
            //TODO ThrowawayBlockDataHandler.wipeCache();
        }
    }
}