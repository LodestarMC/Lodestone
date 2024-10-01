package team.lodestar.lodestone.registry.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.events.LodestoneShaderRegistrationEvent;
import team.lodestar.lodestone.systems.rendering.shader.ExtendedShaderInstance;
import team.lodestar.lodestone.systems.rendering.shader.ShaderHolder;

import java.io.IOException;
import java.util.function.Consumer;

import static team.lodestar.lodestone.LodestoneLib.lodestonePath;

public class LodestoneShaders {

    public static ShaderHolder LODESTONE_TEXTURE = new ShaderHolder(lodestonePath("lodestone_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, "LumiTransparency");
    public static ShaderHolder LODESTONE_TEXT = new ShaderHolder(lodestonePath("lodestone_text"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP);

    public static ShaderHolder PARTICLE = new ShaderHolder(lodestonePath("particle/lodestone_particle"), DefaultVertexFormat.PARTICLE, "LumiTransparency", "DepthFade");

    public static ShaderHolder SCREEN_PARTICLE = new ShaderHolder(lodestonePath("screen/screen_particle"), DefaultVertexFormat.POSITION_TEX_COLOR);
    public static ShaderHolder DISTORTED_TEXTURE = new ShaderHolder(lodestonePath("screen/distorted_texture"), DefaultVertexFormat.POSITION_TEX_COLOR , "Speed", "TimeOffset", "Intensity", "XFrequency", "YFrequency", "UVCoordinates");

    public static ShaderHolder SCROLLING_TEXTURE = new ShaderHolder(lodestonePath("shapes/scrolling_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, "Speed", "LumiTransparency");
    public static ShaderHolder TRIANGLE_TEXTURE = new ShaderHolder(lodestonePath("shapes/triangle_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, "LumiTransparency");
    public static ShaderHolder SCROLLING_TRIANGLE_TEXTURE = new ShaderHolder(lodestonePath("shapes/scrolling_triangle_texture"), DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, "Speed", "LumiTransparency");


    public static void init() {
        LodestoneShaderRegistrationEvent.EVENT.register((provider, shaderList1) -> {
            shaderList1.add(Pair.of(LODESTONE_TEXTURE.createInstance(provider), getConsumer()));
            shaderList1.add(Pair.of(LODESTONE_TEXT.createInstance(provider), getConsumer()));
            shaderList1.add(Pair.of(PARTICLE.createInstance(provider), getConsumer()));
            shaderList1.add(Pair.of(SCREEN_PARTICLE.createInstance(provider), getConsumer()));
            shaderList1.add(Pair.of(DISTORTED_TEXTURE.createInstance(provider), getConsumer()));
            shaderList1.add(Pair.of(SCROLLING_TEXTURE.createInstance(provider), getConsumer()));
            shaderList1.add(Pair.of(TRIANGLE_TEXTURE.createInstance(provider), getConsumer()));
            shaderList1.add(Pair.of(SCROLLING_TRIANGLE_TEXTURE.createInstance(provider), getConsumer()));
        });
    }

    public static Consumer<ShaderInstance> getConsumer() {
        return (shader) -> ((ExtendedShaderInstance) shader).getShaderHolder();
    }
}