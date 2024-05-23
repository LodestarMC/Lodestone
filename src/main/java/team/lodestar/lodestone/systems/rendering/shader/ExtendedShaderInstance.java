package team.lodestar.lodestone.systems.rendering.shader;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ChainedJsonException;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.util.GsonHelper;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class ExtendedShaderInstance extends ShaderInstance {

    protected Map<String, Consumer<Uniform>> defaultUniformData;

    public ExtendedShaderInstance(ResourceProvider pResourceProvider, ResourceLocation location, VertexFormat pVertexFormat) throws IOException {
        super(pResourceProvider, location.toString(), pVertexFormat);
    }

    public void setUniformDefaults() {
        for (Map.Entry<String, Consumer<Uniform>> defaultDataEntry : getDefaultUniformData().entrySet()) {
            final Uniform t = uniformMap.get(defaultDataEntry.getKey());
            defaultDataEntry.getValue().accept(t);
            float f = 0;
        }
    }

    public abstract ShaderHolder getShaderHolder();

    public Map<String, Consumer<Uniform>> getDefaultUniformData() {
        if (defaultUniformData == null) {
            defaultUniformData = new HashMap<>();
        }
        return defaultUniformData;
    }

    @Override
    public void parseUniformNode(JsonElement pJson) throws ChainedJsonException {
        super.parseUniformNode(pJson);

        JsonObject jsonobject = GsonHelper.convertToJsonObject(pJson, "uniform");
        String uniformName = GsonHelper.getAsString(jsonobject, "name");
        if (getShaderHolder().uniformsToCache.contains(uniformName)) {
            Uniform uniform = uniforms.get(uniforms.size() - 1);

            Consumer<Uniform> consumer;
            if (uniform.getType() <= 3) {
                final IntBuffer buffer = uniform.getIntBuffer();
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
                final FloatBuffer buffer = uniform.getFloatBuffer();
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
