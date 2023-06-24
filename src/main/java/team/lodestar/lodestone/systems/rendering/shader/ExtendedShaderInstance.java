package team.lodestar.lodestone.systems.rendering.shader;

import com.google.gson.*;
import com.mojang.blaze3d.shaders.*;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.*;
import net.minecraft.resources.*;
import net.minecraft.server.*;
import net.minecraft.server.packs.resources.*;
import net.minecraft.util.*;

import java.io.*;
import java.nio.*;
import java.util.*;
import java.util.function.*;

public abstract class ExtendedShaderInstance extends ShaderInstance {

    public final Map<String, Consumer<Uniform>> defaultUniformData = new HashMap<>();

    public ExtendedShaderInstance(ResourceProvider pResourceProvider, ResourceLocation location, VertexFormat pVertexFormat) throws IOException {
        super(pResourceProvider, location, pVertexFormat);
    }

    public void setUniformDefaults() {
        for (Map.Entry<String, Consumer<Uniform>> defaultDataEntry : defaultUniformData.entrySet()) {
            defaultDataEntry.getValue().accept(uniformMap.get(defaultDataEntry.getKey()));
        }
    }

    public abstract ShaderHolder getShaderHolder();

    @Override
    public void parseUniformNode(JsonElement pJson) throws ChainedJsonException {
        super.parseUniformNode(pJson);

        JsonObject jsonobject = GsonHelper.convertToJsonObject(pJson, "uniform");
        String uniformName = GsonHelper.getAsString(jsonobject, "name");
        if (getShaderHolder().uniformsToCache.contains(uniformName)) {
            Uniform uniform = uniforms.get(uniforms.size()-1);

            Consumer<Uniform> consumer;
            if (uniform.getType() <= 3) {
                final IntBuffer buffer = uniform.getIntBuffer();
                buffer.position(0);
                int[] array = new int[]{buffer.get(0), buffer.get(1), buffer.get(2), buffer.get(3)};
                consumer = u -> u.setSafe(array[0], array[1], array[2], array[3]);
            }
            else {
                final FloatBuffer buffer = uniform.getFloatBuffer();
                buffer.position(0);
                float[] array = new float[]{buffer.get(0), buffer.get(1), buffer.get(2), buffer.get(3)};
                consumer = uniform.getType() <= 7 ? u -> u.setSafe(array[0], array[1], array[2], array[3]) : u -> u.set(array);
            }

            defaultUniformData.put(uniformName, consumer);
        }
    }
}
