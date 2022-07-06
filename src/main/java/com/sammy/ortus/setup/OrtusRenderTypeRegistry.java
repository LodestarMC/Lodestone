package com.sammy.ortus.setup;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.datafixers.util.Pair;
import com.sammy.ortus.handlers.RenderHandler;
import com.sammy.ortus.systems.rendering.ShaderUniformHandler;
import com.sammy.ortus.systems.rendering.StateShards;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RenderLevelLastEvent;

import java.util.HashMap;
import java.util.function.Function;

import static com.mojang.blaze3d.vertex.DefaultVertexFormat.*;
import static com.sammy.ortus.OrtusLib.ORTUS;

public class OrtusRenderTypeRegistry extends RenderStateShard {
    public OrtusRenderTypeRegistry(String p_110161_, Runnable p_110162_, Runnable p_110163_) {
        super(p_110161_, p_110162_, p_110163_);
    }

    /**
     * Stores many copies of render types, a copy is a new instance of a render type with the same properties.
     * It's useful when we want to apply different uniform changes with each separate use of our render type.
     * Use the {@link #copy(int, RenderType)} method to create copies.
     */
    public static final HashMap<Pair<Integer, RenderType>, RenderType> COPIES = new HashMap<>();

    public static final RenderType ADDITIVE_PARTICLE = createGenericRenderType(ORTUS, "additive_particle", PARTICLE, VertexFormat.Mode.QUADS, OrtusShaderRegistry.ADDITIVE_PARTICLE.shard, StateShards.ADDITIVE_TRANSPARENCY, TextureAtlas.LOCATION_PARTICLES);
    public static final RenderType ADDITIVE_BLOCK_PARTICLE = createGenericRenderType(ORTUS, "additive_block_particle", PARTICLE, VertexFormat.Mode.QUADS, OrtusShaderRegistry.ADDITIVE_PARTICLE.shard, StateShards.ADDITIVE_TRANSPARENCY, TextureAtlas.LOCATION_BLOCKS);
    public static final RenderType ADDITIVE_BLOCK = createGenericRenderType(ORTUS, "block", POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, OrtusShaderRegistry.ADDITIVE_TEXTURE.shard, StateShards.ADDITIVE_TRANSPARENCY, TextureAtlas.LOCATION_BLOCKS);
    public static final RenderType OUTLINE_SOLID = createGenericRenderType(ORTUS, "outline_solid", NEW_ENTITY, VertexFormat.Mode.QUADS, OrtusShaderRegistry.ADDITIVE_TEXTURE.shard, StateShards.ADDITIVE_TRANSPARENCY, TextureAtlas.LOCATION_BLOCKS);

    public static final RenderType VERTEX_DISTORTION = createGenericRenderType(ORTUS, "vertex_distortion", PARTICLE, VertexFormat.Mode.QUADS, OrtusShaderRegistry.VERTEX_DISTORTION.shard, StateShards.ADDITIVE_TRANSPARENCY, TextureAtlas.LOCATION_PARTICLES);
    public static final RenderType ADDITIVE_SOLID = createGenericRenderType(ORTUS, "additive_solid", POSITION_COLOR_LIGHTMAP, VertexFormat.Mode.QUADS, RenderStateShard.POSITION_COLOR_LIGHTMAP_SHADER, StateShards.ADDITIVE_TRANSPARENCY);
    public static final RenderType TRANSPARENT_SOLID = createGenericRenderType(ORTUS, "transparent_solid", POSITION_COLOR_LIGHTMAP, VertexFormat.Mode.QUADS, RenderStateShard.POSITION_COLOR_LIGHTMAP_SHADER, StateShards.NORMAL_TRANSPARENCY);
    /**
     * Render Functions. You can create Render Types by statically applying these to your texture. Alternatively, use {@link #GENERIC} if none of the presets suit your needs.
     */


    public static final RenderTypeProvider TEXTURE = new RenderTypeProvider((texture) -> createGenericRenderType(texture.getNamespace(), "texture", POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, RenderStateShard.POSITION_COLOR_LIGHTMAP_SHADER, StateShards.NO_TRANSPARENCY, texture));

    public static final RenderTypeProvider TRANSPARENT_TEXTURE = new RenderTypeProvider((texture) -> createGenericRenderType(texture.getNamespace(), "transparent_texture", POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, RenderStateShard.POSITION_COLOR_TEX_LIGHTMAP_SHADER, StateShards.NORMAL_TRANSPARENCY, texture));
    public static final RenderTypeProvider TRANSPARENT_TEXTURE_TRIANGLE = new RenderTypeProvider((texture) -> createGenericRenderType(texture.getNamespace(), "transparent_texture_triangle", POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, OrtusShaderRegistry.TRIANGLE_TEXTURE.shard, StateShards.NORMAL_TRANSPARENCY, texture));

    public static final RenderTypeProvider ADDITIVE_TEXTURE = new RenderTypeProvider((texture) -> createGenericRenderType(texture.getNamespace(), "additive_texture", POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, OrtusShaderRegistry.ADDITIVE_TEXTURE.shard, StateShards.ADDITIVE_TRANSPARENCY, texture));
    public static final RenderTypeProvider ADDITIVE_TEXTURE_TRIANGLE = new RenderTypeProvider((texture) -> createGenericRenderType(texture.getNamespace(), "additive_texture_triangle", POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, OrtusShaderRegistry.TRIANGLE_TEXTURE.shard, StateShards.ADDITIVE_TRANSPARENCY, texture));


    public static final RenderTypeProvider VERTEX_DISTORTION_TEXTURE = new RenderTypeProvider((texture) -> createGenericRenderType(texture.getNamespace(), "vertex_distortion_texture", POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, OrtusShaderRegistry.VERTEX_DISTORTION.shard, StateShards.ADDITIVE_TRANSPARENCY, texture));
    public static final RenderTypeProvider RADIAL_NOISE = new RenderTypeProvider((texture) -> createGenericRenderType(texture.getNamespace(), "radial_noise", POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, OrtusShaderRegistry.RADIAL_NOISE.shard, StateShards.ADDITIVE_TRANSPARENCY, texture));
    public static final RenderTypeProvider RADIAL_SCATTER_NOISE = new RenderTypeProvider((texture) -> createGenericRenderType(texture.getNamespace(), "radial_scatter_noise", POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, OrtusShaderRegistry.RADIAL_SCATTER_NOISE.shard, StateShards.ADDITIVE_TRANSPARENCY, texture));
    public static final RenderTypeProvider SCROLLING_TEXTURE = new RenderTypeProvider((texture) -> createGenericRenderType(texture.getNamespace(), "scrolling_texture", POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, OrtusShaderRegistry.SCROLLING_TEXTURE.shard, StateShards.ADDITIVE_TRANSPARENCY, texture));
    public static final RenderTypeProvider SCROLLING_TEXTURE_TRIANGLE = new RenderTypeProvider((texture) -> createGenericRenderType(texture.getNamespace(), "scrolling_texture_triangle", POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, OrtusShaderRegistry.SCROLLING_TRIANGLE_TEXTURE.shard, StateShards.ADDITIVE_TRANSPARENCY, texture));


    public static final Function<RenderTypeData, RenderType> GENERIC = (data) -> createGenericRenderType(data.name, data.format, data.mode, data.shader, data.transparency, data.texture);


    /**
     * Creates a custom render type with a texture.
     */
    public static RenderType createGenericRenderType(String modId, String name, VertexFormat format, VertexFormat.Mode mode, ShaderStateShard shader, TransparencyStateShard transparency, ResourceLocation texture) {
        return createGenericRenderType(modId + ":" + name, format, mode, shader, transparency, new TextureStateShard(texture, false, false));
    }
    public static RenderType createGenericRenderType(String modId, String name, VertexFormat format, VertexFormat.Mode mode, ShaderStateShard shader, TransparencyStateShard transparency, EmptyTextureStateShard texture) {
        return createGenericRenderType(modId + ":" + name, format, mode, shader, transparency, texture);
    }

    /**
     * Creates a custom render type with an empty texture.
     */
    public static RenderType createGenericRenderType(String modId, String name, VertexFormat format, VertexFormat.Mode mode, ShaderStateShard shader, TransparencyStateShard transparency) {
        return createGenericRenderType(modId + ":" + name, format, mode, shader, transparency, RenderStateShard.NO_TEXTURE);
    }

    /**
     * Creates a custom render type and creates a buffer builder for it.
     */
    public static RenderType createGenericRenderType(String name, VertexFormat format, VertexFormat.Mode mode, ShaderStateShard shader, TransparencyStateShard transparency, EmptyTextureStateShard texture) {
        RenderType type = RenderType.create(
                name, format, mode, 256, false, false, RenderType.CompositeState.builder()
                        .setShaderState(shader)
                        .setWriteMaskState(new WriteMaskStateShard(true, true))
                        .setLightmapState(new LightmapStateShard(false))
                        .setTransparencyState(transparency)
                        .setTextureState(texture)
                        .setCullState(new CullStateShard(true))
                        .createCompositeState(true)
        );
        RenderHandler.addRenderType(type);
        return type;
    }

    /**
     * Queues shader uniform changes for a render type. When we end batches in {@link RenderHandler#renderLast(RenderLevelLastEvent)}, we do so one render type at a time.
     * Prior to ending a batch, we run {@link ShaderUniformHandler#updateShaderData(ShaderInstance)} if one is present for a given render type.
     */
    public static RenderType queueUniformChanges(RenderType type, ShaderUniformHandler handler) {
        RenderHandler.HANDLERS.put(type, handler);
        return type;
    }

    /**
     * Creates a copy of a render type. These are stored in the {@link #COPIES} hashmap, with the key being a pair of original render type and copy index.
     */
    public static RenderType copy(int index, RenderType type) {
        return COPIES.computeIfAbsent(Pair.of(index, type), (p) -> GENERIC.apply(new RenderTypeData((RenderType.CompositeRenderType) type)));
    }

    /*
     * This needs to be rewritten, but I don't have the brainpower to do it rn
     * */
    public static RenderType getOutlineTranslucent(ResourceLocation texture, boolean cull) {
        return RenderType.create(ORTUS + ":outline_translucent",
                DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder()
                        .setShaderState(cull ? RENDERTYPE_ENTITY_TRANSLUCENT_CULL_SHADER : RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
                        .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .setCullState(cull ? CULL : NO_CULL)
                        .setLightmapState(LIGHTMAP)
                        .setOverlayState(OVERLAY)
                        .setWriteMaskState(COLOR_WRITE)
                        .createCompositeState(false));
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