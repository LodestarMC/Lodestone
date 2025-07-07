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

@EventBusSubscriber(value = Dist.CLIENT, modid = LodestoneLib.LODESTONE)
public class LodestoneShaders {

    public static ShaderHolder LODESTONE_TEXTURE = new ShaderHolder(lodestonePath("lodestone_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, "LumiTransparency");
    public static ShaderHolder DISTORTED_TEXTURE = new ShaderHolder(lodestonePath("distorted_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP , "LumiTransparency", "Speed", "TimeOffset", "Intensity", "XFrequency", "YFrequency", "UVCoordinates");
    public static ShaderHolder TEXTURE_FADE = new ShaderHolder(lodestonePath("texture_fade"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, "LumiTransparency");
    public static ShaderHolder LODESTONE_TEXT = new ShaderHolder(lodestonePath("lodestone_text"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP);

    public static ShaderHolder PARTICLE = new ShaderHolder(lodestonePath("particle/lodestone_particle"), DefaultVertexFormat.PARTICLE, "LumiTransparency", "DepthFade");

    public static ShaderHolder SCREEN_PARTICLE = new ShaderHolder(lodestonePath("screen/screen_particle"), DefaultVertexFormat.POSITION_TEX_COLOR);
    public static ShaderHolder SCREEN_DISTORTED_TEXTURE = new ShaderHolder(lodestonePath("screen/screen_distorted_texture"), DefaultVertexFormat.POSITION_TEX_COLOR , "LumiTransparency", "Speed", "TimeOffset", "Intensity", "XFrequency", "YFrequency", "UVCoordinates");

    public static ShaderHolder TRIANGLE_TEXTURE = new ShaderHolder(lodestonePath("shapes/triangle_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, "LumiTransparency");
    public static ShaderHolder TWO_SIDED_TRIANGLE_TEXTURE = new ShaderHolder(lodestonePath("shapes/two_sided_triangle_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, "LumiTransparency");
    public static ShaderHolder ROUNDED_TRIANGLE_TEXTURE = new ShaderHolder(lodestonePath("shapes/rounded_triangle_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, "LumiTransparency");

    public static ShaderHolder SCROLLING_TEXTURE = new ShaderHolder(lodestonePath("shapes/scrolling_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, "LumiTransparency", "Speed");
    public static ShaderHolder SCROLLING_TRIANGLE_TEXTURE = new ShaderHolder(lodestonePath("shapes/scrolling_triangle_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, "LumiTransparency", "Speed");

    public static ShaderHolder NINE_SLICE = new ShaderHolder(lodestonePath("nineslice/nine_slice"), DefaultVertexFormat.POSITION_TEX);
    public static ShaderHolder DISTORTED_NINE_SLICE_TEXTURE = new ShaderHolder(lodestonePath("nineslice/distorted/distorted_nine_slice_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP , "LumiTransparency", "Speed", "TimeOffset", "Intensity", "XFrequency", "YFrequency", "UVCoordinates");

    public static ShaderHolder RADIAL_DISTORTED_SCREEN_LIGHT = new ShaderHolder(lodestonePath("screen/radial_distorted_light"), DefaultVertexFormat.POSITION_TEX_COLOR , "Speed", "TimeOffset", "Intensity", "XFrequency", "YFrequency", "UVCoordinates", "Angle", "LightAngleRange", "LightIntensity");

    @SubscribeEvent
    public static void shaderRegistry(RegisterShadersEvent event) {
        registerShader(event, LODESTONE_TEXTURE);
        registerShader(event, DISTORTED_TEXTURE);
        registerShader(event, TEXTURE_FADE);

        registerShader(event, LODESTONE_TEXT);

        registerShader(event, PARTICLE);

        registerShader(event, SCREEN_PARTICLE);
        registerShader(event, SCREEN_DISTORTED_TEXTURE);

        registerShader(event, TRIANGLE_TEXTURE);
        registerShader(event, TWO_SIDED_TRIANGLE_TEXTURE);
        registerShader(event, ROUNDED_TRIANGLE_TEXTURE);


        registerShader(event, SCROLLING_TEXTURE);
        registerShader(event, SCROLLING_TRIANGLE_TEXTURE);

        registerShader(event, NINE_SLICE);
        registerShader(event, DISTORTED_NINE_SLICE_TEXTURE);

        registerShader(event, RADIAL_DISTORTED_SCREEN_LIGHT);
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