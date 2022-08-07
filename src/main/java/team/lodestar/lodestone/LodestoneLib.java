package team.lodestar.lodestone;

import team.lodestar.lodestone.config.ClientConfig;
import team.lodestar.lodestone.data.LodestoneBlockTagDatagen;
import team.lodestar.lodestone.data.LodestoneItemTagDatagen;
import team.lodestar.lodestone.data.LodestoneLangDatagen;
import team.lodestar.lodestone.setup.*;
import team.lodestar.lodestone.compability.CuriosCompat;
import team.lodestar.lodestone.compability.JeiCompat;
import team.lodestar.lodestone.setup.LodestonePaintings;
import team.lodestar.lodestone.setup.LodestoneCommandRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

@Mod(LodestoneLib.LODESTONE)
public class LodestoneLib {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String LODESTONE = "lodestone";
    public static final Random RANDOM = new Random();

    public LodestoneLib() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        LodestoneCommandRegistry.registerArgumentTypes();

        LodestoneBlockEntityRegistry.BLOCK_ENTITY_TYPES.register(modBus);
        LodestoneParticleRegistry.PARTICLES.register(modBus);
        LodestoneAttributeRegistry.ATTRIBUTES.register(modBus);
        LodestonePaintings.register(modBus);

        JeiCompat.init();
        CuriosCompat.init();

        modBus.addListener(this::gatherData);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> LodestoneLibClient.onLodestone(modBus, forgeBus));
    }

    public static ResourceLocation lodestonePath(String path) {
        return new ResourceLocation(LODESTONE, path);
    }

    public void gatherData(GatherDataEvent event) {
        event.getGenerator().addProvider(new LodestoneLangDatagen(event.getGenerator()));
        LodestoneBlockTagDatagen blockTagDatagen = new LodestoneBlockTagDatagen(event.getGenerator(), event.getExistingFileHelper());
        event.getGenerator().addProvider(blockTagDatagen);
        event.getGenerator().addProvider(new LodestoneItemTagDatagen(event.getGenerator(), blockTagDatagen, event.getExistingFileHelper()));
    }
}