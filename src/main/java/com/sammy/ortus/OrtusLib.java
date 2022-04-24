package com.sammy.ortus;

import com.sammy.ortus.compability.CuriosCompat;
import com.sammy.ortus.compability.JeiCompat;
import com.sammy.ortus.config.ClientConfig;
import com.sammy.ortus.registry.OrtusCommands;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

import static com.sammy.ortus.registry.OrtusAttributes.ATTRIBUTES;
import static com.sammy.ortus.registry.OrtusBlockEntities.BLOCK_ENTITY_TYPES;
import static com.sammy.ortus.registry.OrtusParticles.PARTICLES;

@Mod(OrtusLib.ORTUS)
public class OrtusLib {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String ORTUS = "ortus";
    public static final Random RANDOM = new Random();

    public OrtusLib() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        OrtusCommands.registerArgumentTypes();

        BLOCK_ENTITY_TYPES.register(modBus);
        PARTICLES.register(modBus);
        ATTRIBUTES.register(modBus);

        JeiCompat.init();
        CuriosCompat.init();
    }

    public static ResourceLocation prefix(String path) {
        return new ResourceLocation(ORTUS, path);
    }
}