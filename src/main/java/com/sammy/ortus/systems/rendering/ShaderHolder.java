package com.sammy.ortus.systems.rendering;

import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.ShaderInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ShaderHolder {
    public ShaderInstance instance;
    public ArrayList<String> uniforms;
    public ArrayList<UniformData> defaultUniformData = new ArrayList<>();
    public final RenderStateShard.ShaderStateShard shard = new RenderStateShard.ShaderStateShard(getInstance());

    public ShaderHolder(String... uniforms) {
        this.uniforms = new ArrayList<>(List.of(uniforms));
    }

    public void setInstance(ShaderInstance instance) {
        this.instance = instance;
    }

    public Supplier<ShaderInstance> getInstance() {
        return () -> instance;
    }

}