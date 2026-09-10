package team.lodestar.lodestone.systems.rendering.uniform;

import net.minecraft.client.renderer.ShaderInstance;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

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

    public UniformData accept(Consumer<UniformData> modifier) {
        modifier.accept(this);
        return this;
    }

    public void applyData(ShaderInstance instance) {
        for (String key : uniformValues.keySet()) {
            float[] value = ArrayUtils.toPrimitive(uniformValues.get(key));
            instance.safeGetUniform(key).set(value);
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