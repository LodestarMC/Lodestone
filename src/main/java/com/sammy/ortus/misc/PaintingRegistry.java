package com.sammy.ortus.misc;

import com.sammy.ortus.OrtusLib;
import com.sammy.ortus.systems.textureloader.OrtusTextureLoader;
import net.minecraft.world.entity.decoration.Motive;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


public class PaintingRegistry {
    public static final DeferredRegister<Motive> PAINTING_MOTIVES= DeferredRegister.create(ForgeRegistries.PAINTING_TYPES, OrtusLib.ORTUS);


    public static void register(IEventBus bus){
        PAINTING_MOTIVES.register(bus);
        PAINTING_MOTIVES.register("lefunny",()-> new Motive(64,64));
        OrtusTextureLoader.copyTextureWithChanges(OrtusLib.ortusPrefix("lefunny"),OrtusLib.ortusPrefix("painting/microfunny"),OrtusLib.ortusPrefix("textures/painting/microfunny.png"),(image) -> image);
        PAINTING_MOTIVES.register("microfunny",()-> new Motive(16,16));
    }
}
