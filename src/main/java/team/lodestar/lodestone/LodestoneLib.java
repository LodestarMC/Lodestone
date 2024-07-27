package team.lodestar.lodestone;

import net.minecraft.resources.*;
import net.minecraft.util.*;
import net.neoforged.bus.api.*;
import net.neoforged.fml.*;
import net.neoforged.fml.common.*;
import net.neoforged.fml.config.*;
import net.neoforged.neoforge.common.*;
import net.neoforged.neoforge.data.event.*;
import org.apache.logging.log4j.*;
import team.lodestar.lodestone.compability.*;
import team.lodestar.lodestone.config.*;
import team.lodestar.lodestone.data.*;
import team.lodestar.lodestone.registry.common.*;
import team.lodestar.lodestone.registry.common.particle.*;
import team.lodestar.lodestone.systems.item.*;

@Mod(LodestoneLib.LODESTONE)
public class LodestoneLib {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String LODESTONE = "lodestone";
    public static final RandomSource RANDOM = RandomSource.create();

    public LodestoneLib() {
        IEventBus modBus = ModLoadingContext.get().getActiveContainer().getEventBus();
        IEventBus forgeBus = NeoForge.EVENT_BUS;
        ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);

        LodestoneBlockEntityRegistry.BLOCK_ENTITY_TYPES.register(modBus);
        LodestoneParticleRegistry.PARTICLES.register(modBus);
        LodestoneAttributeRegistry.ATTRIBUTES.register(modBus);
        LodestoneRecipeSerializerRegistry.RECIPE_SERIALIZERS.register(modBus);
        LodestoneArgumentTypeRegistry.register(modBus);

        CuriosCompat.init();

        modBus.addListener(this::gatherData);
        modBus.addListener(LodestoneItemProperties::populateItemGroups);
    }

    public static ResourceLocation lodestonePath(String path) {
        return ResourceLocation.fromNamespaceAndPath(LODESTONE, path);
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