package team.lodestar.lodestone.systems.rendering.shader;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.util.GsonHelper;
import team.lodestar.lodestone.*;
import team.lodestar.lodestone.systems.rendering.uniform.UniformData;
import team.lodestar.lodestone.systems.rendering.uniform.UniformDataBuilder;

import java.io.IOException;
import java.io.Reader;
import java.util.*;

public class ExtendedShaderInstance extends ShaderInstance {

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

    protected final UniformData defaultUniforms;

    public ExtendedShaderInstance(ResourceProvider pResourceProvider, ShaderHolder shaderHolder) throws IOException {
        super(pResourceProvider, shaderHolder.getShaderLocation(), shaderHolder.getShaderFormat());
        this.shaderHolder = shaderHolder;
        var jsonLocation = shaderHolder.getShaderLocation().withPath(p -> "shaders/core/" + p + ".json");
        try (Reader reader = pResourceProvider.openAsReader(jsonLocation)) {
            JsonObject shaderJson = GsonHelper.parse(reader);
            defaultUniforms = parseDefaultUniformValues(shaderJson);
        }
    }

    public void applyUniformDefaults() {
        defaultUniforms.applyData(this);
    }

    public ShaderHolder getShaderHolder() {
        return shaderHolder;
    }

    public UniformData parseDefaultUniformValues(JsonObject shaderJson) {
        var shaderUniforms = GsonHelper.getAsJsonArray(shaderJson, "uniforms", null);
        if (shaderUniforms == null) {
            return null;
        }
        UniformDataBuilder builder = UniformData.create();

        for (JsonElement uniformJson : shaderUniforms) {
            var uniformObject = GsonHelper.convertToJsonObject(uniformJson, "uniform");
            var uniformName = GsonHelper.getAsString(uniformObject, "name");
            if (EXCLUDED_UNIFORMS.contains(uniformName)) {
                continue;
            }
            var uniform = uniformMap.get(uniformName);
            if (uniform == null) {
                LodestoneLib.LOGGER.warn(
                        "Shader json {} has a uniform {} that is not present in the shader instance uniform map. This may cause issues.",
                        shaderHolder.getShaderLocation(), uniformName);
                continue;
            }

            var array = GsonHelper.getAsJsonArray(uniformObject, "values");


            var values = new ArrayList<Float>();
            for (int i = 0; i < array.size(); i++) {
                var jsonElement = array.get(i).getAsFloat();
                values.add(jsonElement);
            }
            builder.setUniform(uniformName, values);
        }
        return builder.build();
    }
}
