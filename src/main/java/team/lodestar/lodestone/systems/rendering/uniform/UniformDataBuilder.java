package team.lodestar.lodestone.systems.rendering.uniform;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class UniformDataBuilder {

    protected final ConcurrentHashMap<String, Float[]> uniformValues = new ConcurrentHashMap<>();
    protected final ConcurrentHashMap<String, Integer> samplerValues = new ConcurrentHashMap<>();

    public UniformDataBuilder accept(Consumer<UniformDataBuilder> modifier) {
        modifier.accept(this);
        return this;
    }

    public UniformDataBuilder accept(UniformData data) {
        if (data == null) {
            return this;
        }
        var builder = UniformData.create();
        for (String key : data.uniformValues.keySet()) {
            float[] primitive = ArrayUtils.toPrimitive(data.uniformValues.get(key));
            builder.setUniform(key, primitive);
        }
        for (String key : data.samplerValues.keySet()) {
            builder.setSampler(key, data.samplerValues.get(key));
        }
        return builder;
    }

    public UniformDataBuilder useLuminescenceAsAlpha() {
        return setUniform("LumiTransparency", 1f);
    }

    public UniformDataBuilder applyDepthFade() {
        return applyDepthFade(1.5f);
    }

    public UniformDataBuilder applyDepthFade(float value) {
        return setUniform("DepthFade", value);
    }

    public UniformDataBuilder setUniform(String uniformName, float... values) {
        uniformValues.put(uniformName, ArrayUtils.toObject(values));
        return this;
    }

    public UniformDataBuilder setUniform(String uniformName, Collection<Float> values) {
        uniformValues.put(uniformName, values.toArray(Float[]::new));
        return this;
    }

    public UniformDataBuilder setSampler(String samplerName, int textureId) {
        samplerValues.put(samplerName, textureId);
        return this;
    }

    public UniformData build() {
        return new UniformData(uniformValues, samplerValues);
    }
}
