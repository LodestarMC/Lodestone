package team.lodestar.lodestone.systems.rendering.rendeertype;

import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import team.lodestar.lodestone.systems.rendering.*;

/**
 * Stores all relevant data from a RenderType.
 */
public class RenderTypeData {
    public final String name;
    public final VertexFormat format;
    public final VertexFormat.Mode mode;
    public final RenderStateShard.ShaderStateShard shader;
    public final RenderStateShard.EmptyTextureStateShard texture;
    public final RenderStateShard.CullStateShard cull;
    public final RenderStateShard.LightmapStateShard lightmap;
    public RenderStateShard.TransparencyStateShard transparency = StateShards.ADDITIVE_TRANSPARENCY;

    public RenderTypeData(String name, VertexFormat format, VertexFormat.Mode mode, RenderStateShard.ShaderStateShard shader, RenderStateShard.EmptyTextureStateShard texture, RenderStateShard.CullStateShard cull, RenderStateShard.LightmapStateShard lightmap) {
        this.name = name;
        this.format = format;
        this.mode = mode;
        this.shader = shader;
        this.texture = texture;
        this.cull = cull;
        this.lightmap = lightmap;
    }

    public RenderTypeData(String name, VertexFormat format, VertexFormat.Mode mode, RenderStateShard.ShaderStateShard shader, RenderStateShard.TransparencyStateShard transparency, RenderStateShard.EmptyTextureStateShard texture, RenderStateShard.CullStateShard cull, RenderStateShard.LightmapStateShard lightmap) {
        this(name, format, mode, shader, texture, cull, lightmap);
        this.transparency = transparency;
    }

    public RenderTypeData(String name, LodestoneRenderType type) {
        this(name, type.format(), type.mode(), type.state.shaderState, type.state.transparencyState, type.state.textureState, type.state.cullState, type.state.lightmapState);
    }

    public RenderTypeData(LodestoneRenderType type) {
        this(type.name, type);
    }
}
