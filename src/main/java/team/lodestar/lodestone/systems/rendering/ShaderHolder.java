package team.lodestar.lodestone.systems.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.TextureAtlas;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ShaderHolder {
    protected ExtendedShaderInstance instance;
    public ArrayList<String> uniforms;
    public ArrayList<UniformData> defaultUniformData = new ArrayList<>();
    protected final RenderStateShard.ShaderStateShard shard = new RenderStateShard.ShaderStateShard(getInstanceSupplier());

    public ShaderHolder(String... uniforms) {
        this.uniforms = new ArrayList<>(List.of(uniforms));
    }

    public void setUniformDefaults() {
        RenderSystem.setShaderTexture(1, TextureAtlas.LOCATION_BLOCKS);
        defaultUniformData.forEach(u -> u.setUniformValue(instance.safeGetUniform(u.uniformName)));
    }

    public void setInstance(ExtendedShaderInstance instance) {
        this.instance = instance;
    }

    public Supplier<ShaderInstance> getInstanceSupplier() {
        return () -> instance;
    }

    public ExtendedShaderInstance getExtendedShaderInstance() {
        return instance;
    }

    public RenderStateShard.ShaderStateShard getShard() {
        return shard;
    }
}