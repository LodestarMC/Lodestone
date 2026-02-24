package team.lodestar.lodestone.systems.rendering.shader;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.shaders.Uniform;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.util.GsonHelper;
import team.lodestar.lodestone.*;

import java.io.IOException;
import java.io.Reader;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;
import java.util.function.Consumer;

public class LodestoneCoreShaderInstance extends ShaderInstance {

    private static final Set<String> EXCLUDED_UNIFORMS = Set.of(
            "ModelViewMat",
            "ProjMat",
            "TextureMat",
            "ScreenSize",
            "ColorModulator",
            "Light0_Direction",
            "Light1_Direction",
            "GlintAlpha",
            "FogStart",
            "FogEnd",
            "FogColor",
            "FogShape",
            "LineWidth",
            "GameTime",
            "ChunkOffset"
    );

    protected final ShaderHolder shaderHolder;

    protected final Map<String, Consumer<Uniform>> defaultUniformData = new HashMap<>();

    public LodestoneCoreShaderInstance(ResourceProvider pResourceProvider, ShaderHolder shaderHolder) throws IOException {
        super(pResourceProvider, shaderHolder.getShaderLocation(), shaderHolder.getShaderFormat());
        this.shaderHolder = shaderHolder;
        var jsonLocation = shaderHolder.getShaderLocation().withPath(p -> "shaders/core/" + p + ".json");
        try (Reader reader = pResourceProvider.openAsReader(jsonLocation)) {
            JsonObject shaderJson = GsonHelper.parse(reader);
            parseDefaultUniformValues(shaderJson);
        }
    }

    public void restoreUniformValues() {
        for (Map.Entry<String, Consumer<Uniform>> entry : getDefaultUniformData().entrySet()) {
            var uniform = uniformMap.get(entry.getKey());
            entry.getValue().accept(uniform);
        }
    }

    public ShaderHolder getShaderHolder() {
        return shaderHolder;
    }

    public Map<String, Consumer<Uniform>> getDefaultUniformData() {
        return defaultUniformData;
    }

    public void parseDefaultUniformValues(JsonObject shaderJson) {
        var shaderUniforms = GsonHelper.getAsJsonArray(shaderJson, "uniforms", null);
        if (shaderUniforms == null) {
            return;
        }
        for (JsonElement uniformJson : shaderUniforms) {
            JsonObject uniformObject = GsonHelper.convertToJsonObject(uniformJson, "uniform");
            var uniformName = GsonHelper.getAsString(uniformObject, "name");
            if (EXCLUDED_UNIFORMS.contains(uniformName)) {
                continue;
            }
            Uniform uniform = getUniform(uniformName);
            if (uniform == null) {
                LodestoneLib.LOGGER.warn(
                        "Shader json {} has a uniform {} that is not present in the shader instance uniform map. This may cause issues.",
                        shaderHolder.getShaderLocation(), uniformName);
                continue;
            }
            Consumer<Uniform> consumer;
            if (uniform.getType() <= 3) {
                IntBuffer buffer = uniform.getIntBuffer();
                buffer.position(0);
                int[] array = new int[uniform.getCount()];
                for (int i = 0; i < uniform.getCount(); i++) {
                    array[i] = buffer.get(i);
                }
                consumer = u -> {
                    buffer.position(0);
                    buffer.put(array);
                };
            } else {
                FloatBuffer buffer = uniform.getFloatBuffer();
                buffer.position(0);
                float[] array = new float[uniform.getCount()];
                for (int i = 0; i < uniform.getCount(); i++) {
                    array[i] = buffer.get(i);
                }
                consumer = u -> {
                    buffer.position(0);
                    buffer.put(array);
                };
            }

            getDefaultUniformData().put(uniformName, consumer);
        }
    }
}
