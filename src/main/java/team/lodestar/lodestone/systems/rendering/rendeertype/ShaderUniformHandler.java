package team.lodestar.lodestone.systems.rendering.rendeertype;

import net.minecraft.client.renderer.ShaderInstance;

import javax.annotation.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

public class ShaderUniformHandler {

    public final ConcurrentHashMap<String, Float[]> uniformChanges = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<String, Integer> samplerChanges = new ConcurrentHashMap<>();

    public static final ShaderUniformHandler DEPTH_FADE = new ShaderUniformHandler().withDepthFade().lock();
    public static final ShaderUniformHandler LUMITRANSPARENT = new ShaderUniformHandler().withLumiTransparency().lock();
    public static final ShaderUniformHandler LUMITRANSPARENT_DEPTH_FADE = new ShaderUniformHandler().withLumiTransparency().withDepthFade().lock();

    private boolean locked;

    public ShaderUniformHandler() {
    }

    public ShaderUniformHandler(@Nullable ShaderUniformHandler original) {
        if (original == null) {
            return;
        }
        this.locked = original.locked;
        for (Map.Entry<String, Float[]> entry : original.uniformChanges.entrySet()) {
            this.uniformChanges.put(entry.getKey(), Arrays.copyOf(entry.getValue(), entry.getValue().length));
        }
        this.samplerChanges.putAll(original.samplerChanges);
    }

    public ShaderUniformHandler accept(Consumer<ShaderUniformHandler> modifier) {
        if (locked) {
            return this;
        }
        modifier.accept(this);
        return this;
    }

    public ShaderUniformHandler withLumiTransparency() {
        return modifyUniform("LumiTransparency", 1f);
    }

    public ShaderUniformHandler withDepthFade() {
        return modifyUniform("DepthFade", 1.5f);
    }

    public ShaderUniformHandler withDepthFade(float value) {
        return modifyUniform("DepthFade", value);
    }

    public ShaderUniformHandler modifyUniform(String uniformName, float... values) {
        if (locked || values == null || values.length == 0) {
            return this;
        }

        Float[] newValues = new Float[values.length];
        for (int i = 0; i < values.length; i++) {
            newValues[i] = values[i];
        }

        uniformChanges.put(uniformName, newValues);
        return this;
    }

    public ShaderUniformHandler setSamplerTexture(String samplerName, int textureId) {
        if (locked) {
            return this;
        }
        samplerChanges.put(samplerName, textureId);
        return this;
    }

    public void updateShaderData(ShaderInstance instance) {
        for (Map.Entry<String, Float[]> uniformChange : uniformChanges.entrySet()) {
            instance.safeGetUniform(uniformChange.getKey()).set(toPrimitive(uniformChange.getValue()));
        }
        for (Map.Entry<String, Integer> samplerChange : samplerChanges.entrySet()) {
            instance.setSampler(samplerChange.getKey(), samplerChange.getValue());
        }
    }

    private float[] toPrimitive(Float[] boxed) {
        float[] result = new float[boxed.length];
        for (int i = 0; i < boxed.length; i++) {
            result[i] = boxed[i];
        }
        return result;
    }

    public ShaderUniformHandler lock() {
        this.locked = true;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShaderUniformHandler that)) {
            return false;
        }
        if (uniformChanges.size() != that.uniformChanges.size()) {
            return false;
        }
        for (Map.Entry<String, Float[]> entry : uniformChanges.entrySet()) {
            Float[] otherValues = that.uniformChanges.get(entry.getKey());
            if (!Arrays.equals(entry.getValue(), otherValues)) {
                return false;
            }
        }

        if (samplerChanges.size() != that.samplerChanges.size()) {
            return false;
        }
        for (Map.Entry<String, Integer> entry : samplerChanges.entrySet()) {
            if (!Objects.equals(entry.getValue(), that.samplerChanges.get(entry.getKey()))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;

        List<String> uniformKeys = new ArrayList<>(uniformChanges.keySet());
        Collections.sort(uniformKeys);
        for (String key : uniformKeys) {
            result = 31 * result + key.hashCode();
            result = 31 * result + Arrays.hashCode(uniformChanges.get(key));
        }

        List<String> samplerKeys = new ArrayList<>(samplerChanges.keySet());
        Collections.sort(samplerKeys);
        for (String key : samplerKeys) {
            result = 31 * result + key.hashCode();
            result = 31 * result + Objects.hashCode(samplerChanges.get(key));
        }

        return result;
    }
}