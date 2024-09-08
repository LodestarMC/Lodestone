package team.lodestar.lodestone;

import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.minecraftforge.common.*;
import net.minecraftforge.data.event.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.config.*;
import net.minecraftforge.fml.javafmlmod.*;
import org.apache.logging.log4j.*;
import team.lodestar.lodestone.compability.*;
import team.lodestar.lodestone.config.*;
import team.lodestar.lodestone.data.*;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.helpers.ShadersHelper;
import team.lodestar.lodestone.registry.common.*;
import team.lodestar.lodestone.registry.common.particle.*;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.item.*;

@Mod(LodestoneLib.LODESTONE)
public class LodestoneLib {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String LODESTONE = "lodestone";
    public static final RandomSource RANDOM = RandomSource.create();

    public LodestoneLib() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);

        LodestoneBlockEntityRegistry.BLOCK_ENTITY_TYPES.register(modBus);
        LodestoneParticleRegistry.PARTICLES.register(modBus);
        LodestoneAttributeRegistry.ATTRIBUTES.register(modBus);
        LodestoneRecipeSerializerRegistry.RECIPE_SERIALIZERS.register(modBus);
        LodestonePaintingRegistry.register(modBus);
        LodestoneArgumentTypeRegistry.register(modBus);

        CuriosCompat.init();
        ShadersHelper.init();

        modBus.addListener(this::gatherData);
        modBus.addListener(LodestoneItemProperties::populateItemGroups);
        modBus.addListener(LodestoneWorldEventTypeRegistry::postRegistryEvent);
    }

    public static ResourceLocation lodestonePath(String path) {
        return new ResourceLocation(LODESTONE, path);
    }

    public void gatherData(GatherDataEvent event) {
        var lookupProvider = event.getLookupProvider();
        var packOutput = event.getGenerator().getPackOutput();
        var existingFileHelper = event.getExistingFileHelper();
        LodestoneBlockTagDatagen blockTagDatagen = new LodestoneBlockTagDatagen(packOutput, lookupProvider, existingFileHelper);
        event.getGenerator().addProvider(true, new LodestoneLangDatagen(packOutput));
        event.getGenerator().addProvider(true, blockTagDatagen);
        event.getGenerator().addProvider(true, new LodestoneItemTagDatagen(packOutput, lookupProvider, blockTagDatagen.contentsGetter(), existingFileHelper));
        event.getGenerator().addProvider(true, new LodestoneDamageTypeDatagen(packOutput, lookupProvider, existingFileHelper));
    }
}