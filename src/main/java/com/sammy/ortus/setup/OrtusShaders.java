package com.sammy.ortus.setup;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.sammy.ortus.OrtusLib;
import com.sammy.ortus.systems.rendering.ExtendedShaderInstance;
import com.sammy.ortus.systems.rendering.ShaderHolder;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = OrtusLib.ORTUS, bus = Mod.EventBusSubscriber.Bus.MOD)
public class OrtusShaders {

    public static ShaderHolder ADDITIVE_TEXTURE = new ShaderHolder();
    public static ShaderHolder ADDITIVE_PARTICLE = new ShaderHolder();

    public static ShaderHolder DISTORTED_TEXTURE = new ShaderHolder("Speed", "TimeOffset", "Intensity", "XFrequency", "YFrequency", "UVCoordinates");
    public static ShaderHolder METALLIC_NOISE = new ShaderHolder("Intensity", "Size", "Speed", "Brightness");
    public static ShaderHolder RADIAL_NOISE = new ShaderHolder("Speed", "XFrequency", "YFrequency", "Intensity", "ScatterPower", "ScatterFrequency", "DistanceFalloff");
    public static ShaderHolder RADIAL_SCATTER_NOISE = new ShaderHolder("Speed", "XFrequency", "YFrequency", "Intensity", "ScatterPower", "ScatterFrequency", "DistanceFalloff");

    public static ShaderHolder SCROLLING_TEXTURE = new ShaderHolder("Speed");
    public static ShaderHolder TRIANGLE_TEXTURE = new ShaderHolder();
    public static ShaderHolder SCROLLING_TRIANGLE_TEXTURE = new ShaderHolder("Speed");


    @SubscribeEvent
    public static void shaderRegistry(RegisterShadersEvent event) throws IOException {
        registerShader(event, ExtendedShaderInstance.createShaderInstance(ADDITIVE_TEXTURE, event.getResourceManager(), OrtusLib.prefix("additive_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP));
        registerShader(event, ExtendedShaderInstance.createShaderInstance(ADDITIVE_PARTICLE, event.getResourceManager(), OrtusLib.prefix("additive_particle"), DefaultVertexFormat.PARTICLE));

        registerShader(event, ExtendedShaderInstance.createShaderInstance(DISTORTED_TEXTURE, event.getResourceManager(), OrtusLib.prefix("noise/distorted_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP));
        registerShader(event, ExtendedShaderInstance.createShaderInstance(METALLIC_NOISE, event.getResourceManager(), OrtusLib.prefix("noise/metallic"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP));
        registerShader(event, ExtendedShaderInstance.createShaderInstance(RADIAL_NOISE, event.getResourceManager(), OrtusLib.prefix("noise/radial_noise"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP));
        registerShader(event, ExtendedShaderInstance.createShaderInstance(RADIAL_SCATTER_NOISE, event.getResourceManager(), OrtusLib.prefix("noise/radial_scatter_noise"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP));

        registerShader(event, ExtendedShaderInstance.createShaderInstance(SCROLLING_TEXTURE, event.getResourceManager(), OrtusLib.prefix("vfx/scrolling_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP));
        registerShader(event, ExtendedShaderInstance.createShaderInstance(TRIANGLE_TEXTURE, event.getResourceManager(), OrtusLib.prefix("vfx/triangle_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP));
        registerShader(event, ExtendedShaderInstance.createShaderInstance(SCROLLING_TRIANGLE_TEXTURE, event.getResourceManager(), OrtusLib.prefix("vfx/scrolling_triangle_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP));
    }

    public static void registerShader(RegisterShadersEvent event, ExtendedShaderInstance extendedShaderInstance) {
        event.registerShader(extendedShaderInstance, s -> ((ExtendedShaderInstance) s).getHolder().setInstance(((ExtendedShaderInstance) s)));
    }
}