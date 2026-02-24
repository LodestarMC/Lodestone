package team.lodestar.lodestone.systems.rendering.shader;

import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import team.lodestar.lodestone.LodestoneLib;

import java.io.IOException;
import java.util.function.Supplier;

public class ShaderHolder implements LodestoneShader {

    protected final ResourceLocation shaderLocation;
    protected final VertexFormat shaderFormat;

    protected LodestoneCoreShaderInstance shaderInstance;

    protected RenderStateShard.ShaderStateShard shard;

    public ShaderHolder(ResourceLocation shaderLocation, VertexFormat shaderFormat) {
        this.shaderLocation = shaderLocation;
        this.shaderFormat = shaderFormat;
    }

    public LodestoneCoreShaderInstance createInstance(ResourceProvider provider) throws IOException {
        return new LodestoneCoreShaderInstance(provider, this);
    }

    public ResourceLocation getShaderLocation() {
        return shaderLocation;
    }

    public VertexFormat getShaderFormat() {
        return shaderFormat;
    }

    public LodestoneCoreShaderInstance getShaderInstance() {
        return shaderInstance;
    }

    public Supplier<ShaderInstance> supplyShaderInstance() {
        return this::getShaderInstance;
    }

    public void setShaderInstance(ShaderInstance reloadedShaderInstance) {
        this.shaderInstance = (LodestoneCoreShaderInstance) reloadedShaderInstance;
    }

    public RenderStateShard.ShaderStateShard getShard() {
        if (shard == null) {
            shard = new RenderStateShard.ShaderStateShard(supplyShaderInstance());
        }
        return shard;
    }

    @Override
    public void register(RegisterShadersEvent event) {
        try {
            ResourceProvider provider = event.getResourceProvider();
            event.registerShader(createInstance(provider), this::setShaderInstance);
        } catch (IOException e) {
            LodestoneLib.LOGGER.error("Error registering shader", e);
            e.printStackTrace();
        }
    }
}