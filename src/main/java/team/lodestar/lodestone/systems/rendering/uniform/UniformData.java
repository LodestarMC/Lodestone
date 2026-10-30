package team.lodestar.lodestone.systems.rendering.uniform;

import com.mojang.blaze3d.shaders.AbstractUniform;
import com.mojang.blaze3d.shaders.Uniform;
import net.minecraft.client.renderer.ShaderInstance;
import org.apache.commons.lang3.ArrayUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;
import java.util.concurrent.*;

public class UniformData {

    protected final ConcurrentHashMap<String, Float[]> uniformValues;
    protected final ConcurrentHashMap<String, Integer> samplerValues;

    public static final UniformData DEPTH_FADE = create().applyDepthFade().build();
    public static final UniformData LUMITRANSPARENT = create().useLuminescenceAsAlpha().build();
    public static final UniformData LUMITRANSPARENT_DEPTH_FADE = create().useLuminescenceAsAlpha().applyDepthFade().build();

    public static UniformDataBuilder create() {
        return new UniformDataBuilder();
    }

    public UniformData(ConcurrentHashMap<String, Float[]> uniformValues, ConcurrentHashMap<String, Integer> samplerValues) {
        this.uniformValues = uniformValues;
        this.samplerValues = samplerValues;
    }

    public UniformData fuse(UniformData other) {
        var builder = UniformData.create();
        return builder.accept(this).accept(other).build();
    }

    public void setValues(ShaderInstance instance) {
        for (String key : uniformValues.keySet()) {
            if (instance.getUniform(key) instanceof Uniform uniform) {
                int type = uniform.getType();

                float[] floatValues = ArrayUtils.toPrimitive(uniformValues.get(key));
                if (type <= 3) {
                    int[] intValues = new int[floatValues.length];
                    for (int i = 0; i < floatValues.length; i++) {
                        intValues[i] = (int) floatValues[i];
                    }
                    var buffer = uniform.getIntBuffer();
                    buffer.position(0);
                    buffer.put(intValues);
                } else {
                    var buffer = uniform.getFloatBuffer();
                    buffer.position(0);
                    buffer.put(floatValues);
                }
            }
        }
        for (String key : samplerValues.keySet()) {
            int value = samplerValues.get(key);
            instance.setSampler(key, value);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UniformData that)) {
            return false;
        }
        if (uniformValues.size() != that.uniformValues.size()) {
            return false;
        }
        for (Map.Entry<String, Float[]> entry : uniformValues.entrySet()) {
            Float[] otherValues = that.uniformValues.get(entry.getKey());
            if (!Arrays.equals(entry.getValue(), otherValues)) {
                return false;
            }
        }

        if (samplerValues.size() != that.samplerValues.size()) {
            return false;
        }
        for (Map.Entry<String, Integer> entry : samplerValues.entrySet()) {
            if (!Objects.equals(entry.getValue(), that.samplerValues.get(entry.getKey()))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;

        List<String> uniformKeys = new ArrayList<>(uniformValues.keySet());
        Collections.sort(uniformKeys);
        for (String key : uniformKeys) {
            result = 31 * result + key.hashCode();
            result = 31 * result + Arrays.hashCode(uniformValues.get(key));
        }

        List<String> samplerKeys = new ArrayList<>(samplerValues.keySet());
        Collections.sort(samplerKeys);
        for (String key : samplerKeys) {
            result = 31 * result + key.hashCode();
            result = 31 * result + Objects.hashCode(samplerValues.get(key));
        }

        return result;
    }
}