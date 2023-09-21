package team.lodestar.lodestone.systems.rendering.rendeertype;

import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import team.lodestar.lodestone.systems.rendering.StateShards;

/**
 * Stores all relevant data from a RenderType.
 */
public class RenderTypeData {
    public final String name;
    public final VertexFormat format;
    public final VertexFormat.Mode mode;
    public final RenderStateShard.ShaderStateShard shader;
    public RenderStateShard.TransparencyStateShard transparency = StateShards.ADDITIVE_TRANSPARENCY;
    public final RenderStateShard.EmptyTextureStateShard texture;

    public RenderTypeData(String name, VertexFormat format, VertexFormat.Mode mode, RenderStateShard.ShaderStateShard shader, RenderStateShard.EmptyTextureStateShard texture) {
        this.name = name;
        this.format = format;
        this.mode = mode;
        this.shader = shader;
        this.texture = texture;
    }

    public RenderTypeData(String name, VertexFormat format, VertexFormat.Mode mode, RenderStateShard.ShaderStateShard shader, RenderStateShard.TransparencyStateShard transparency, RenderStateShard.EmptyTextureStateShard texture) {
        this(name, format, mode, shader, texture);
        this.transparency = transparency;
    }

    public RenderTypeData(String name, RenderType.CompositeRenderType type) {
        this(name, type.format(), type.mode(), type.state.shaderState, type.state.transparencyState, type.state.textureState);
    }

    public RenderTypeData(RenderType.CompositeRenderType type) {
        this(type.name, type);
    }
}
