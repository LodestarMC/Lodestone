package team.lodestar.lodestone;

import io.github.fabricators_of_create.porting_lib.entity.events.*;
import io.github.fabricators_of_create.porting_lib.entity.events.living.LivingHurtEvent;
import io.github.fabricators_of_create.porting_lib.tags.data.DataGenerators;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.impl.datagen.FabricDataGenHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.*;
import team.lodestar.lodestone.compability.*;
import team.lodestar.lodestone.component.LodestonePlayerComponent;
import team.lodestar.lodestone.events.LodestoneInteractionEvent;
import team.lodestar.lodestone.handlers.*;
import team.lodestar.lodestone.helpers.RandomHelper;
import team.lodestar.lodestone.registry.common.*;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.block.LodestoneEntityBlock;
import team.lodestar.lodestone.systems.blockentity.LodestoneBlockEntity;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.item.LodestoneFuelItem;
import team.lodestar.lodestone.systems.item.LodestoneItemProperties;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;

import java.awt.*;

import static org.apache.commons.lang3.ClassUtils.getSimpleName;

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