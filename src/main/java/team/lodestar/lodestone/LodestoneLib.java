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

    public static Block TEST_BLOCK;
    public static BlockEntityType<?> TEST_BLOCK_ENTITY;

    @Override
    public void onInitialize() {

        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            TEST_BLOCK = new TestBlock<>(FabricBlockSettings.create());
            TEST_BLOCK_ENTITY = FabricBlockEntityTypeBuilder.create(TestBlockEntity::new, TEST_BLOCK).build();
            Registry.register(BuiltInRegistries.BLOCK, lodestonePath("test_block"), TEST_BLOCK);
            Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, lodestonePath("test_block_entity"), TEST_BLOCK_ENTITY);
        }

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

    //TEST code
    static class TestBlockEntity extends LodestoneBlockEntity {

        public TestBlockEntity(BlockPos pos, BlockState state) {
            super(TEST_BLOCK_ENTITY, pos, state);
        }

        @Override
        public void tick() {
            super.tick();
            if (level.isClientSide) {
                var firstColor = Color.red;
                var secondColor = Color.yellow;
                var random = level.random;
                final ColorParticleData colorData = ColorParticleData.create(firstColor, secondColor).setCoefficient(1.5f).setEasing(Easing.BOUNCE_IN_OUT).build();
                int lifeTime = RandomHelper.randomBetween(random, 10, 15);
                float scale = RandomHelper.randomBetween(random, 0.15f, 0.2f);
                float velocity = RandomHelper.randomBetween(random, 0.02f, 0.03f);
                WorldParticleBuilder.create(LodestoneParticleRegistry.WISP_PARTICLE)
                        .setRenderTarget(RenderHandler.LATE_DELAYED_RENDER)
                        .setScaleData(GenericParticleData.create(scale, 0).setEasing(Easing.SINE_IN).build())
                        .setTransparencyData(GenericParticleData.create(0.4f, 0.8f, 0.2f).setEasing(Easing.QUAD_OUT).build())
                        .setColorData(colorData)
                        .setSpinData(SpinParticleData.create(0.2f, 0.4f).setSpinOffset((level.getGameTime() * 0.2f) % 6.28f).setEasing(Easing.QUARTIC_IN).build())
                        .setLifetime(lifeTime)
                        .addMotion(0, velocity * 1.5f, 0)
                        .enableNoClip()
                        .spawn(level, getBlockPos().getX() + 0.5, getBlockPos().getY() + 1, getBlockPos().getZ() + 0.5f);
            }
        }

    }

    static class TestBlock<T extends TestBlockEntity> extends LodestoneEntityBlock<T> {
        public TestBlock(FabricBlockSettings settings) {
            super(settings);
            setBlockEntity(() -> (BlockEntityType<T>) TEST_BLOCK_ENTITY);
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