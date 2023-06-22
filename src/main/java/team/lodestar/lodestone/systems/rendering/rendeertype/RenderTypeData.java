package team.lodestar.lodestone.systems.rendering.rendeertype;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.*;
import team.lodestar.lodestone.systems.rendering.*;

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

    public RenderTypeData(RenderType.CompositeRenderType type) {
        this(type.name, type.format(), type.mode(), type.state.shaderState, type.state.transparencyState, type.state.textureState);
    }
}
