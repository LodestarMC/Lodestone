package team.lodestar.lodestone.systems.rendering.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.*;
import net.minecraft.server.packs.resources.*;
import team.lodestar.lodestone.systems.rendering.*;

import java.io.*;
import java.util.*;
import java.util.function.Supplier;

public class ShaderHolder {

    public final ResourceLocation shaderLocation;
    public final VertexFormat shaderFormat;

    protected ExtendedShaderInstance shaderInstance;
    public Collection<String> uniformsToCache;
    private final RenderStateShard.ShaderStateShard shard = new RenderStateShard.ShaderStateShard(getInstance());

    public ShaderHolder(ResourceLocation shaderLocation, VertexFormat shaderFormat, String... uniformsToCache) {
        this.shaderLocation = shaderLocation;
        this.shaderFormat = shaderFormat;
        this.uniformsToCache = new ArrayList<>(List.of(uniformsToCache));
    }

    public ExtendedShaderInstance createInstance(ResourceProvider provider) throws IOException {
        ShaderHolder shaderHolder = this;
        ExtendedShaderInstance shaderInstance = new ExtendedShaderInstance(provider, shaderLocation, shaderFormat) {
            @Override
            public ShaderHolder getShaderHolder() {
                return shaderHolder;
            }
        };
        this.shaderInstance = shaderInstance;
        return shaderInstance;
    }

    public Supplier<ShaderInstance> getInstance() {
        return () -> shaderInstance;
    }

    public RenderStateShard.ShaderStateShard getShard() {
        return shard;
    }
}