package team.lodestar.lodestone.registry.client;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.datafixers.util.Pair;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.registry.client.LodestoneShaderRegistry;
import team.lodestar.lodestone.systems.rendering.ShaderUniformHandler;
import team.lodestar.lodestone.systems.rendering.StateShards;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.function.Function;

import static com.mojang.blaze3d.vertex.DefaultVertexFormat.*;
import static team.lodestar.lodestone.LodestoneLib.LODESTONE;

public class LodestoneRenderTypeRegistry extends RenderStateShard {
    public LodestoneRenderTypeRegistry(String p_110161_, Runnable p_110162_, Runnable p_110163_) {
        super(p_110161_, p_110162_, p_110163_);
    }

    /**
     * Stores many copies of render types, a copy is a new instance of a render type with the same properties.
     * It's useful when we want to apply different uniform changes with each separate use of our render type.
     * Use the {@link #copy(int, RenderType)} method to create copies.
     */
    public static final HashMap<Pair<Integer, RenderType>, RenderType> COPIES = new HashMap<>();

    /**
     * Static, one off Render Types. Should be self-explanatory.
     */
    public static final RenderType ADDITIVE_PARTICLE = createGenericRenderType(LODESTONE, "additive_particle", PARTICLE, VertexFormat.Mode.QUADS, LodestoneShaderRegistry.PARTICLE.getShard(), StateShards.ADDITIVE_TRANSPARENCY, TextureAtlas.LOCATION_PARTICLES);
    public static final RenderType ADDITIVE_BLOCK = createGenericRenderType(LODESTONE, "block", POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, LodestoneShaderRegistry.LODESTONE_TEXTURE.getShard(), StateShards.ADDITIVE_TRANSPARENCY, TextureAtlas.LOCATION_BLOCKS);
    public static final RenderType ADDITIVE_SOLID = createGenericRenderType(LODESTONE, "additive_solid", POSITION_COLOR_LIGHTMAP, VertexFormat.Mode.QUADS, RenderStateShard.POSITION_COLOR_LIGHTMAP_SHADER, StateShards.ADDITIVE_TRANSPARENCY);

    public static final RenderType TRANSPARENT_PARTICLE = createGenericRenderType(LODESTONE, "transparent_particle", PARTICLE, VertexFormat.Mode.QUADS, LodestoneShaderRegistry.PARTICLE.getShard(), StateShards.NORMAL_TRANSPARENCY, TextureAtlas.LOCATION_PARTICLES);
    public static final RenderType TRANSPARENT_BLOCK = createGenericRenderType(LODESTONE, "transparent_block", POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, RenderStateShard.POSITION_COLOR_LIGHTMAP_SHADER, StateShards.NORMAL_TRANSPARENCY, TextureAtlas.LOCATION_PARTICLES);
    public static final RenderType TRANSPARENT_SOLID = createGenericRenderType(LODESTONE, "transparent_solid", POSITION_COLOR_LIGHTMAP, VertexFormat.Mode.QUADS, RenderStateShard.POSITION_COLOR_LIGHTMAP_SHADER, StateShards.NORMAL_TRANSPARENCY);

    /**
     * Render Functions. You can create Render Types by statically applying these to your texture. Alternatively, use {@link #GENERIC} if none of the presets suit your needs.
     * For Static Definitions use {@link RenderTypeProvider#apply(ResourceLocation)}, otherwise use {@link RenderTypeProvider#applyAndCache(ResourceLocation)}
     */
    public static final RenderTypeProvider TEXTURE = new RenderTypeProvider((texture) -> createGenericRenderType(texture.getNamespace(), "texture", POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, LodestoneShaderRegistry.LODESTONE_TEXTURE.getShard(), StateShards.NO_TRANSPARENCY, texture));

    public static final RenderTypeProvider TRANSPARENT_TEXTURE = new RenderTypeProvider((texture) -> createGenericRenderType(texture.getNamespace(), "transparent_texture", POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, LodestoneShaderRegistry.LODESTONE_TEXTURE.getShard(), StateShards.NORMAL_TRANSPARENCY, texture));
    public static final RenderTypeProvider TRANSPARENT_TEXTURE_TRIANGLE = new RenderTypeProvider((texture) -> createGenericRenderType(texture.getNamespace(), "transparent_texture_triangle", POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, LodestoneShaderRegistry.TRIANGLE_TEXTURE.getShard(), StateShards.NORMAL_TRANSPARENCY, texture));

    public static final RenderTypeProvider ADDITIVE_TEXTURE = new RenderTypeProvider((texture) -> createGenericRenderType(texture.getNamespace(), "additive_texture", POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, LodestoneShaderRegistry.LODESTONE_TEXTURE.getShard(), StateShards.ADDITIVE_TRANSPARENCY, texture));
    public static final RenderTypeProvider ADDITIVE_TEXTURE_TRIANGLE = new RenderTypeProvider((texture) -> createGenericRenderType(texture.getNamespace(), "additive_texture_triangle", POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, LodestoneShaderRegistry.TRIANGLE_TEXTURE.getShard(), StateShards.ADDITIVE_TRANSPARENCY, texture));

    public static final RenderTypeProvider RADIAL_NOISE = new RenderTypeProvider((texture) -> createGenericRenderType(texture.getNamespace(), "radial_noise", POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, LodestoneShaderRegistry.RADIAL_NOISE.getShard(), StateShards.ADDITIVE_TRANSPARENCY, texture));
    public static final RenderTypeProvider RADIAL_SCATTER_NOISE = new RenderTypeProvider((texture) -> createGenericRenderType(texture.getNamespace(), "radial_scatter_noise", POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, LodestoneShaderRegistry.RADIAL_SCATTER_NOISE.getShard(), StateShards.ADDITIVE_TRANSPARENCY, texture));
    public static final RenderTypeProvider SCROLLING_TEXTURE = new RenderTypeProvider((texture) -> createGenericRenderType(texture.getNamespace(), "scrolling_texture", POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, LodestoneShaderRegistry.SCROLLING_TEXTURE.getShard(), StateShards.ADDITIVE_TRANSPARENCY, texture));
    public static final RenderTypeProvider SCROLLING_TEXTURE_TRIANGLE = new RenderTypeProvider((texture) -> createGenericRenderType(texture.getNamespace(), "scrolling_texture_triangle", POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, LodestoneShaderRegistry.SCROLLING_TRIANGLE_TEXTURE.getShard(), StateShards.ADDITIVE_TRANSPARENCY, texture));

    public static final Function<RenderTypeData, RenderType> GENERIC = (data) -> createGenericRenderType(data.name, data.format, data.mode, data.shader, data.transparency, data.texture);

    public static RenderType createGenericRenderType(String modId, String name, VertexFormat format, VertexFormat.Mode mode, ShaderStateShard shader, TransparencyStateShard transparency, ResourceLocation texture) {
        return createGenericRenderType(modId + ":" + name, format, mode, shader, transparency, new TextureStateShard(texture, false, false));
    }
    public static RenderType createGenericRenderType(String modId, String name, VertexFormat format, VertexFormat.Mode mode, ShaderStateShard shader, TransparencyStateShard transparency, EmptyTextureStateShard texture) {
        return createGenericRenderType(modId + ":" + name, format, mode, shader, transparency, texture);
    }
    public static RenderType createGenericRenderType(String modId, String name, VertexFormat format, VertexFormat.Mode mode, ShaderStateShard shader, TransparencyStateShard transparency) {
        return createGenericRenderType(modId + ":" + name, format, mode, shader, transparency, RenderStateShard.NO_TEXTURE);
    }

    public static RenderType createGenericRenderType(String name, VertexFormat format, VertexFormat.Mode mode, ShaderStateShard shader, TransparencyStateShard transparency, EmptyTextureStateShard texture) {
        return createGenericRenderType(name, format, mode, RenderType.CompositeState.builder()
                .setShaderState(shader)
                .setTransparencyState(transparency)
                .setTextureState(texture)
                .setLightmapState(LIGHTMAP)
                .setCullState(CULL));
    }

    /**
     * Creates a custom render type and creates a buffer builder for it.
     */
    public static RenderType createGenericRenderType(String name, VertexFormat format, VertexFormat.Mode mode, RenderType.CompositeState.CompositeStateBuilder builder) {
        RenderType type = RenderType.create(name, format, mode, 256, false, false, builder.createCompositeState(true));
        RenderHandler.addRenderType(type);
        return type;
    }

    /**
     * Queues shader uniform changes for a render type. When we end batches in {@link RenderHandler}}, we do so one render type at a time.
     * Prior to ending a batch, we run {@link ShaderUniformHandler#updateShaderData(ShaderInstance)} if one is present for a given render type.
     */
    public static RenderType queueUniformChanges(RenderType type, ShaderUniformHandler handler) {
        RenderHandler.UNIFORM_HANDLERS.put(type, handler);
        return type;
    }

    /**
     * Creates a copy of a render type. These are stored in the {@link #COPIES} hashmap, with the key being a pair of original render type and copy index.
     */
    public static RenderType copy(int index, RenderType type) {
        return COPIES.computeIfAbsent(Pair.of(index, type), (p) -> GENERIC.apply(new RenderTypeData((RenderType.CompositeRenderType) type)));
    }

    /**
     * Stores all relevant data from a RenderType.
     */
    public static class RenderTypeData {
        public final String name;
        public final VertexFormat format;
        public final VertexFormat.Mode mode;
        public final ShaderStateShard shader;
        public TransparencyStateShard transparency = StateShards.ADDITIVE_TRANSPARENCY;
        public final EmptyTextureStateShard texture;

        public RenderTypeData(String name, VertexFormat format, VertexFormat.Mode mode, ShaderStateShard shader, EmptyTextureStateShard texture) {
            this.name = name;
            this.format = format;
            this.mode = mode;
            this.shader = shader;
            this.texture = texture;
        }

        public RenderTypeData(String name, VertexFormat format, VertexFormat.Mode mode, ShaderStateShard shader, TransparencyStateShard transparency, EmptyTextureStateShard texture) {
            this(name, format, mode, shader, texture);
            this.transparency = transparency;
        }

        public RenderTypeData(RenderType.CompositeRenderType type) {
            this(type.name, type.format(), type.mode(), type.state.shaderState, type.state.transparencyState, type.state.textureState);
        }
    }

    public static class RenderTypeProvider {
        private final Function<ResourceLocation, RenderType> function;
        private final Function<ResourceLocation, RenderType> memorizedFunction;

        public RenderTypeProvider(Function<ResourceLocation, RenderType> function) {
            this.function = function;
            this.memorizedFunction = Util.memoize(function);
        }

        public RenderType apply(ResourceLocation texture) {
            return function.apply(texture);
        }

        public RenderType applyAndCache(ResourceLocation texture) {
            return this.memorizedFunction.apply(texture);
        }
    }
}