package team.lodestar.lodestone.registry.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.systems.rendering.shader.ShaderHolder;

import java.io.IOException;

import static team.lodestar.lodestone.LodestoneLib.lodestonePath;

@EventBusSubscriber(value = Dist.CLIENT, modid = LodestoneLib.LODESTONE, bus = EventBusSubscriber.Bus.MOD)
public class LodestoneShaderRegistry {

    public static ShaderHolder LODESTONE_TEXTURE = new ShaderHolder(lodestonePath("lodestone_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, "LumiTransparency");
    public static ShaderHolder LODESTONE_TEXT = new ShaderHolder(lodestonePath("lodestone_text"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP);

    public static ShaderHolder PARTICLE = new ShaderHolder(lodestonePath("particle/lodestone_particle"), DefaultVertexFormat.PARTICLE, "LumiTransparency", "DepthFade");

    public static ShaderHolder SCREEN_PARTICLE = new ShaderHolder(lodestonePath("screen/screen_particle"), DefaultVertexFormat.POSITION_TEX_COLOR);
    public static ShaderHolder DISTORTED_TEXTURE = new ShaderHolder(lodestonePath("screen/distorted_texture"), DefaultVertexFormat.POSITION_TEX_COLOR , "Speed", "TimeOffset", "Intensity", "XFrequency", "YFrequency", "UVCoordinates");

    public static ShaderHolder SCROLLING_TEXTURE = new ShaderHolder(lodestonePath("shapes/scrolling_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, "Speed", "LumiTransparency");
    public static ShaderHolder TRIANGLE_TEXTURE = new ShaderHolder(lodestonePath("shapes/triangle_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, "LumiTransparency");
    public static ShaderHolder SCROLLING_TRIANGLE_TEXTURE = new ShaderHolder(lodestonePath("shapes/scrolling_triangle_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, "Speed", "LumiTransparency");



    @SubscribeEvent
    public static void shaderRegistry(RegisterShadersEvent event) {
        registerShader(event, LODESTONE_TEXTURE);
        registerShader(event, LODESTONE_TEXT);
        registerShader(event, PARTICLE);
        registerShader(event, SCREEN_PARTICLE);
        registerShader(event, DISTORTED_TEXTURE);
        registerShader(event, SCROLLING_TEXTURE);
        registerShader(event, TRIANGLE_TEXTURE);
        registerShader(event, SCROLLING_TRIANGLE_TEXTURE);
    }

    public static void registerShader(RegisterShadersEvent event, ShaderHolder shaderHolder) {
        try {
            ResourceProvider provider = event.getResourceProvider();
            event.registerShader(shaderHolder.createInstance(provider), shaderHolder::setShaderInstance);
        } catch (IOException e) {
            LodestoneLib.LOGGER.error("Error registering shader", e);
            e.printStackTrace();
        }
    }
}