package team.lodestar.lodestone.registry.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.systems.rendering.shader.ExtendedShaderInstance;
import team.lodestar.lodestone.systems.rendering.shader.ShaderHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static team.lodestar.lodestone.LodestoneLib.lodestonePath;

public class LodestoneShaderRegistry {

    public static List<Pair<ShaderInstance, Consumer<ShaderInstance>>> shaderList;

    public static ShaderHolder LODESTONE_TEXTURE = new ShaderHolder(lodestonePath("lodestone_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, "LumiTransparency");

    public static ShaderHolder PARTICLE = new ShaderHolder(lodestonePath("particle/particle"), DefaultVertexFormat.PARTICLE, "LumiTransparency");

    public static ShaderHolder SCREEN_PARTICLE = new ShaderHolder(lodestonePath("screen/screen_particle"), DefaultVertexFormat.POSITION_TEX_COLOR);
    public static ShaderHolder DISTORTED_TEXTURE = new ShaderHolder(lodestonePath("screen/distorted_texture"), DefaultVertexFormat.POSITION_COLOR_TEX, "Speed", "TimeOffset", "Intensity", "XFrequency", "YFrequency", "UVCoordinates");

    public static ShaderHolder SCROLLING_TEXTURE = new ShaderHolder(lodestonePath("shapes/scrolling_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, "Speed", "LumiTransparency");
    public static ShaderHolder TRIANGLE_TEXTURE = new ShaderHolder(lodestonePath("shapes/triangle_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, "LumiTransparency");
    public static ShaderHolder SCROLLING_TRIANGLE_TEXTURE = new ShaderHolder(lodestonePath("shapes/scrolling_triangle_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, "Speed", "LumiTransparency");


    public static void shaderRegistry(ResourceProvider manager) throws IOException {
        shaderList = new ArrayList<>();
        registerShader(LODESTONE_TEXTURE.createInstance(manager));
        registerShader(PARTICLE.createInstance(manager));
        registerShader(SCREEN_PARTICLE.createInstance(manager));
        registerShader(DISTORTED_TEXTURE.createInstance(manager));
        registerShader(SCROLLING_TEXTURE.createInstance(manager));
        registerShader(TRIANGLE_TEXTURE.createInstance(manager));
        registerShader(SCROLLING_TRIANGLE_TEXTURE.createInstance(manager));
    }

    public static void registerShader(ExtendedShaderInstance extendedShaderInstance) {
        registerShader(extendedShaderInstance, (shader) -> ((ExtendedShaderInstance) shader).getShaderHolder());
    }

    public static void registerShader(ShaderInstance shader, Consumer<ShaderInstance> onLoaded) {
        shaderList.add(Pair.of(shader, onLoaded));
    }
}