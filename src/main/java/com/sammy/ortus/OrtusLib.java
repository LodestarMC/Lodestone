package com.sammy.ortus;

import com.sammy.ortus.compability.CuriosCompat;
import com.sammy.ortus.compability.JeiCompat;
import com.sammy.ortus.config.ClientConfig;
import com.sammy.ortus.data.OrtusBlockTagDatagen;
import com.sammy.ortus.data.OrtusLangDatagen;
import com.sammy.ortus.misc.PaintingRegistry;
import com.sammy.ortus.setup.OrtusCommandRegistry;
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

import static com.sammy.ortus.setup.OrtusAttributeRegistry.ATTRIBUTES;
import static com.sammy.ortus.setup.OrtusBlockEntityRegistry.BLOCK_ENTITY_TYPES;
import static com.sammy.ortus.setup.OrtusParticleRegistry.PARTICLES;

@Mod(OrtusLib.ORTUS)
public class OrtusLib {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String ORTUS = "ortus";
    public static final Random RANDOM = new Random();

    public OrtusLib() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        OrtusCommandRegistry.registerArgumentTypes();

        BLOCK_ENTITY_TYPES.register(modBus);
        PARTICLES.register(modBus);
        ATTRIBUTES.register(modBus);
        PaintingRegistry.register(modBus);

        JeiCompat.init();
        CuriosCompat.init();

        modBus.addListener(this::gatherData);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> OrtusLibClient.onOrtus(modBus, forgeBus));
    }

    public static ResourceLocation ortusPrefix(String path) {
        return new ResourceLocation(ORTUS, path);
    }

    public void gatherData(GatherDataEvent event) {
        event.getGenerator().addProvider(new OrtusLangDatagen(event.getGenerator()));
        event.getGenerator().addProvider(new OrtusBlockTagDatagen(event.getGenerator(), event.getExistingFileHelper()));
    }
}