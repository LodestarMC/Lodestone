package team.lodestar.lodestone.registry.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.server.packs.resources.*;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.systems.rendering.shader.ExtendedShaderInstance;
import team.lodestar.lodestone.systems.rendering.shader.ShaderHolder;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;

import static team.lodestar.lodestone.LodestoneLib.lodestonePath;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = LodestoneLib.LODESTONE, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LodestoneShaderRegistry {

    public static ShaderHolder LODESTONE_TEXTURE = new ShaderHolder(lodestonePath("lodestone_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, "LumiTransparency");

    public static ShaderHolder PARTICLE = new ShaderHolder(lodestonePath("particle/particle"), DefaultVertexFormat.PARTICLE,"LumiTransparency");

    public static ShaderHolder SCREEN_PARTICLE = new ShaderHolder(lodestonePath("screen/screen_particle"), DefaultVertexFormat.POSITION_TEX_COLOR);
    public static ShaderHolder DISTORTED_TEXTURE = new ShaderHolder(lodestonePath("screen/distorted_texture"), DefaultVertexFormat.POSITION_TEX_COLOR,"Speed", "TimeOffset", "Intensity", "XFrequency", "YFrequency", "UVCoordinates");

    public static ShaderHolder SCROLLING_TEXTURE = new ShaderHolder(lodestonePath("shapes/scrolling_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP,"Speed", "LumiTransparency");
    public static ShaderHolder TRIANGLE_TEXTURE = new ShaderHolder(lodestonePath("shapes/triangle_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP,"LumiTransparency");
    public static ShaderHolder SCROLLING_TRIANGLE_TEXTURE = new ShaderHolder(lodestonePath("shapes/scrolling_triangle_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP,"Speed", "LumiTransparency");


    @SubscribeEvent
    public static void shaderRegistry(RegisterShadersEvent event) throws IOException {
        ResourceManager resourceManager = event.getResourceManager();

        registerShader(event, LODESTONE_TEXTURE.createInstance(resourceManager));
        registerShader(event, PARTICLE.createInstance(resourceManager));
        registerShader(event, SCREEN_PARTICLE.createInstance(resourceManager));
        registerShader(event, DISTORTED_TEXTURE.createInstance(resourceManager));
        registerShader(event, SCROLLING_TEXTURE.createInstance(resourceManager));
        registerShader(event, TRIANGLE_TEXTURE.createInstance(resourceManager));
        registerShader(event, SCROLLING_TRIANGLE_TEXTURE.createInstance(resourceManager));
    }

    public static void registerShader(RegisterShadersEvent event, ExtendedShaderInstance extendedShaderInstance) {
        event.registerShader(extendedShaderInstance, s -> {});
    }
}