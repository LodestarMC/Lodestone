package team.lodestar.lodestone.registry.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.systems.rendering.shader.ExtendedShaderInstance;
import team.lodestar.lodestone.systems.rendering.shader.ShaderHolder;

import java.io.IOException;

import static team.lodestar.lodestone.LodestoneLib.lodestonePath;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = LodestoneLib.LODESTONE, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LodestoneShaderRegistry {

    public static ShaderHolder LODESTONE_TEXTURE = new ShaderHolder(lodestonePath("lodestone_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, "LumiTransparency");
    public static ShaderHolder LODESTONE_TEXT = new ShaderHolder(lodestonePath("lodestone_text"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP);

    public static ShaderHolder PARTICLE = new ShaderHolder(lodestonePath("particle/particle"), DefaultVertexFormat.PARTICLE, "LumiTransparency");

    public static ShaderHolder SCREEN_PARTICLE = new ShaderHolder(lodestonePath("screen/screen_particle"), DefaultVertexFormat.POSITION_TEX_COLOR);
    public static ShaderHolder DISTORTED_TEXTURE = new ShaderHolder(lodestonePath("screen/distorted_texture"), DefaultVertexFormat.POSITION_COLOR_TEX, "Speed", "TimeOffset", "Intensity", "XFrequency", "YFrequency", "UVCoordinates");

    public static ShaderHolder SCROLLING_TEXTURE = new ShaderHolder(lodestonePath("shapes/scrolling_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, "Speed", "LumiTransparency");
    public static ShaderHolder TRIANGLE_TEXTURE = new ShaderHolder(lodestonePath("shapes/triangle_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, "LumiTransparency");
    public static ShaderHolder SCROLLING_TRIANGLE_TEXTURE = new ShaderHolder(lodestonePath("shapes/scrolling_triangle_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, "Speed", "LumiTransparency");



    @SubscribeEvent
    public static void shaderRegistry(RegisterShadersEvent event) throws IOException {
        ResourceProvider provider = event.getResourceProvider();

        registerShader(event, LODESTONE_TEXTURE.createInstance(provider));
        registerShader(event, LODESTONE_TEXT.createInstance(provider));
        registerShader(event, PARTICLE.createInstance(provider));
        registerShader(event, SCREEN_PARTICLE.createInstance(provider));
        registerShader(event, DISTORTED_TEXTURE.createInstance(provider));
        registerShader(event, SCROLLING_TEXTURE.createInstance(provider));
        registerShader(event, TRIANGLE_TEXTURE.createInstance(provider));
        registerShader(event, SCROLLING_TRIANGLE_TEXTURE.createInstance(provider));
    }

    public static void registerShader(RegisterShadersEvent event, ExtendedShaderInstance extendedShaderInstance) {
        event.registerShader(extendedShaderInstance, s -> {
        });
    }
}