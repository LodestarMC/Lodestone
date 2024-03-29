package team.lodestar.lodestone.setup;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.datafixers.util.Pair;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.systems.rendering.rendeertype.ShaderUniformHandler;
import team.lodestar.lodestone.systems.rendering.StateShards;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.systems.rendering.rendeertype.*;
import team.lodestar.lodestone.systems.rendering.shader.*;

import java.util.HashMap;
import java.util.function.Function;

import static com.mojang.blaze3d.vertex.DefaultVertexFormat.*;
import static com.mojang.blaze3d.vertex.VertexFormat.Mode.QUADS;
import static team.lodestar.lodestone.LodestoneLib.LODESTONE;
import static team.lodestar.lodestone.handlers.RenderHandler.LARGER_BUFFER_SOURCES;

public class LodestoneRenderTypeRegistry extends RenderStateShard {
    public LodestoneRenderTypeRegistry(String p_110161_, Runnable p_110162_, Runnable p_110163_) {
        super(p_110161_, p_110162_, p_110163_);
    }

    /**
     * Stores many copies of render types, a copy is a new instance of a render type with the same properties.
     * It's useful when we want to apply different uniform changes with each separate use of our render type.
     * Use the {@link #copyAndStore(Object, RenderType)} {@link #copy(RenderType)} methods to create copies.
     */
    public static final HashMap<Pair<Object, RenderType>, RenderType> COPIES = new HashMap<>();

    public static final Function<RenderTypeData, RenderType> GENERIC = (data) -> createGenericRenderType(data.name, data.format, data.mode, data.shader, data.transparency, data.texture, data.cull);

    /**
     * Static, one off Render Types. Should be self-explanatory.
     */

    public static final RenderType ADDITIVE_PARTICLE = createGenericRenderType("lodestone:additive_particle", PARTICLE, QUADS, builder()
            .setShaderState(LodestoneShaderRegistry.PARTICLE)
            .setTransparencyState(StateShards.ADDITIVE_TRANSPARENCY)
            .setTextureState(TextureAtlas.LOCATION_PARTICLES)
            .setCullState(RenderStateShard.NO_CULL));

    public static final RenderType ADDITIVE_BLOCK_PARTICLE = createGenericRenderType("lodestone:additive_block_particle", PARTICLE, QUADS, builder()
            .setShaderState(LodestoneShaderRegistry.PARTICLE)
            .setTransparencyState(StateShards.ADDITIVE_TRANSPARENCY)
            .setTextureState(TextureAtlas.LOCATION_BLOCKS)
            .setCullState(RenderStateShard.NO_CULL));

    public static final RenderType ADDITIVE_BLOCK = createGenericRenderType("lodestone:additive_block", POSITION_COLOR_TEX_LIGHTMAP, QUADS, builder()
            .setShaderState(LodestoneShaderRegistry.LODESTONE_TEXTURE)
            .setTransparencyState(StateShards.ADDITIVE_TRANSPARENCY)
            .setTextureState(TextureAtlas.LOCATION_BLOCKS));

    public static final RenderType ADDITIVE_SOLID = createGenericRenderType("lodestone:additive_block", POSITION_COLOR_LIGHTMAP, QUADS, builder()
            .setShaderState(RenderStateShard.POSITION_COLOR_LIGHTMAP_SHADER)
            .setTransparencyState(StateShards.ADDITIVE_TRANSPARENCY)
            .setTextureState(RenderStateShard.NO_TEXTURE));

    public static final RenderType TRANSPARENT_PARTICLE = createGenericRenderType("lodestone:transparent_particle", PARTICLE, QUADS, builder()
            .setShaderState(LodestoneShaderRegistry.PARTICLE)
            .setTransparencyState(StateShards.NORMAL_TRANSPARENCY)
            .setTextureState(TextureAtlas.LOCATION_PARTICLES)
            .setCullState(RenderStateShard.NO_CULL));

    public static final RenderType TRANSPARENT_BLOCK_PARTICLE = createGenericRenderType("lodestone:transparent_block_particle", PARTICLE, QUADS, builder()
            .setShaderState(LodestoneShaderRegistry.PARTICLE)
            .setTransparencyState(StateShards.NORMAL_TRANSPARENCY)
            .setTextureState(TextureAtlas.LOCATION_BLOCKS)
            .setCullState(RenderStateShard.NO_CULL));

    public static final RenderType TRANSPARENT_BLOCK = createGenericRenderType("lodestone:transparent_block", POSITION_COLOR_TEX_LIGHTMAP, QUADS, builder()
            .setShaderState(LodestoneShaderRegistry.LODESTONE_TEXTURE)
            .setTransparencyState(StateShards.NORMAL_TRANSPARENCY)
            .setTextureState(TextureAtlas.LOCATION_BLOCKS));

    public static final RenderType TRANSPARENT_SOLID = createGenericRenderType("lodestone:transparent_block", POSITION_COLOR_LIGHTMAP, QUADS, builder()
            .setShaderState(RenderStateShard.POSITION_COLOR_LIGHTMAP_SHADER)
            .setTransparencyState(StateShards.NORMAL_TRANSPARENCY)
            .setTextureState(RenderStateShard.NO_TEXTURE));

    public static final RenderType LUMITRANSPARENT_PARTICLE = copyWithUniformChanges("lodestone:lumitransparent_particle", TRANSPARENT_PARTICLE, ShaderUniformHandler.LUMITRANSPARENT);
    public static final RenderType LUMITRANSPARENT_BLOCK_PARTICLE = copyWithUniformChanges("lodestone:lumitransparent_block_particle", TRANSPARENT_BLOCK_PARTICLE, ShaderUniformHandler.LUMITRANSPARENT);
    public static final RenderType LUMITRANSPARENT_BLOCK = copyWithUniformChanges("lodestone:lumitransparent_block", TRANSPARENT_BLOCK, ShaderUniformHandler.LUMITRANSPARENT);
    public static final RenderType LUMITRANSPARENT_SOLID = copyWithUniformChanges("lodestone:lumitransparent_solid", TRANSPARENT_SOLID, ShaderUniformHandler.LUMITRANSPARENT);

    /**
     * Render Functions. You can create Render Types by statically applying these to your texture. Alternatively, use {@link #GENERIC} if none of the presets suit your needs.
     * For Static Definitions use {@link RenderTypeProvider#apply(ResourceLocation)}, otherwise use {@link RenderTypeProvider#applyAndCache(ResourceLocation)}
     */
    public static final RenderTypeProvider TEXTURE = new RenderTypeProvider((texture) -> createGenericRenderType(texture.getNamespace(), "texture", POSITION_COLOR_TEX_LIGHTMAP, LodestoneShaderRegistry.LODESTONE_TEXTURE.getShard(), StateShards.NO_TRANSPARENCY, texture));

    public static final RenderTypeProvider TRANSPARENT_TEXTURE = new RenderTypeProvider((texture) -> createGenericRenderType(texture.getNamespace(), "transparent_texture", POSITION_COLOR_TEX_LIGHTMAP, LodestoneShaderRegistry.LODESTONE_TEXTURE.getShard(), StateShards.NORMAL_TRANSPARENCY, texture));
    public static final RenderTypeProvider TRANSPARENT_TEXTURE_TRIANGLE = new RenderTypeProvider((texture) -> createGenericRenderType(texture.getNamespace(), "transparent_texture_triangle", POSITION_COLOR_TEX_LIGHTMAP, LodestoneShaderRegistry.TRIANGLE_TEXTURE.getShard(), StateShards.NORMAL_TRANSPARENCY, texture));

    public static final RenderTypeProvider ADDITIVE_TEXTURE = new RenderTypeProvider((texture) -> createGenericRenderType(texture.getNamespace(), "additive_texture", POSITION_COLOR_TEX_LIGHTMAP, LodestoneShaderRegistry.LODESTONE_TEXTURE.getShard(), StateShards.ADDITIVE_TRANSPARENCY, texture));
    public static final RenderTypeProvider ADDITIVE_TEXTURE_TRIANGLE = new RenderTypeProvider((texture) -> createGenericRenderType(texture.getNamespace(), "additive_texture_triangle", POSITION_COLOR_TEX_LIGHTMAP, LodestoneShaderRegistry.TRIANGLE_TEXTURE.getShard(), StateShards.ADDITIVE_TRANSPARENCY, texture));

    public static final RenderTypeProvider SCROLLING_TEXTURE = new RenderTypeProvider((texture) -> createGenericRenderType(texture.getNamespace(), "scrolling_texture", POSITION_COLOR_TEX_LIGHTMAP, LodestoneShaderRegistry.SCROLLING_TEXTURE.getShard(), StateShards.ADDITIVE_TRANSPARENCY, texture));
    public static final RenderTypeProvider SCROLLING_TEXTURE_TRIANGLE = new RenderTypeProvider((texture) -> createGenericRenderType(texture.getNamespace(), "scrolling_texture_triangle", POSITION_COLOR_TEX_LIGHTMAP, LodestoneShaderRegistry.SCROLLING_TRIANGLE_TEXTURE.getShard(), StateShards.ADDITIVE_TRANSPARENCY, texture));

    public static RenderType createGenericRenderType(String modId, String name, VertexFormat format, ShaderStateShard shader, TransparencyStateShard transparency, ResourceLocation texture) {
        return createGenericRenderType(modId, name, format, QUADS, shader, transparency, new TextureStateShard(texture, false, false), RenderStateShard.CULL);
    }

    public static RenderType createGenericRenderType(String modId, String name, VertexFormat format, VertexFormat.Mode mode, ShaderStateShard shader, TransparencyStateShard transparency, EmptyTextureStateShard texture, CullStateShard cull) {
        return createGenericRenderType(modId + ":" + name, format, mode, shader, transparency, texture, cull);
    }
    public static RenderType createGenericRenderType(String name, VertexFormat format, VertexFormat.Mode mode, ShaderStateShard shader, TransparencyStateShard transparency, EmptyTextureStateShard texture, CullStateShard cull) {
        return createGenericRenderType(name, format, mode, RenderType.CompositeState.builder()
                .setShaderState(shader)
                .setTransparencyState(transparency)
                .setTextureState(texture)
                .setLightmapState(LIGHTMAP)
                .setCullState(cull));
    }

    /**
     * Creates a custom render type and creates a buffer builder for it.
     */
    public static RenderType createGenericRenderType(String name, VertexFormat format, VertexFormat.Mode mode, RenderType.CompositeState.CompositeStateBuilder builder) {
        int size = LARGER_BUFFER_SOURCES ? 262144 : 256;
        RenderType type = RenderType.create(name, format, mode, size, false, false, builder.createCompositeState(true));
        RenderHandler.addRenderType(type);
        return type;
    }

    public static RenderType copyWithUniformChanges(RenderType type, ShaderUniformHandler handler) {
        return applyUniformChanges(copy(type), handler);
    }

    public static RenderType copyWithUniformChanges(String newName, RenderType type, ShaderUniformHandler handler) {
        return applyUniformChanges(copy(newName, type), handler);
    }

    /**
     * Queues shader uniform changes for a render type. When we end batches in {@link RenderHandler}}, we do so one render type at a time.
     * Prior to ending a batch, we run {@link ShaderUniformHandler#updateShaderData(ShaderInstance)} if one is present for a given render type.
     */
    public static RenderType applyUniformChanges(RenderType type, ShaderUniformHandler handler) {
        RenderHandler.UNIFORM_HANDLERS.put(type, handler);
        return type;
    }

    /**
     * Creates a copy of a render type.
     */
    public static RenderType copy(RenderType type) {
        return GENERIC.apply(new RenderTypeData((RenderType.CompositeRenderType) type));
    }

    public static RenderType copy(String newName, RenderType type) {
        return GENERIC.apply(new RenderTypeData(newName, (RenderType.CompositeRenderType) type));
    }

    /**
     * Creates a copy of a render type and stores it in the {@link #COPIES} hashmap, with the key being a pair of original render type and copy index.
     */
    public static RenderType copyAndStore(Object index, RenderType type) {
        return COPIES.computeIfAbsent(Pair.of(index, type), (p) -> GENERIC.apply(new RenderTypeData((RenderType.CompositeRenderType) type)));
    }

    public static LodestoneCompositeStateBuilder builder() {
        return new LodestoneCompositeStateBuilder().setLightmapState(RenderStateShard.LIGHTMAP);
    }

    public static class LodestoneCompositeStateBuilder extends RenderType.CompositeState.CompositeStateBuilder {

        public LodestoneCompositeStateBuilder() {
            super();
        }

        public LodestoneCompositeStateBuilder setTextureState(ResourceLocation texture) {
            return setTextureState(new RenderStateShard.TextureStateShard(texture, false, false));
        }
        public LodestoneCompositeStateBuilder setShaderState(ShaderHolder shaderHolder) {
            return setShaderState(shaderHolder.getShard());
        }

        @Override
        public LodestoneCompositeStateBuilder setTextureState(EmptyTextureStateShard pTextureState) {
            return (LodestoneCompositeStateBuilder) super.setTextureState(pTextureState);
        }
        @Override
        public LodestoneCompositeStateBuilder setShaderState(ShaderStateShard pShaderState) {
            return (LodestoneCompositeStateBuilder) super.setShaderState(pShaderState);
        }

        @Override
        public LodestoneCompositeStateBuilder setTransparencyState(TransparencyStateShard pTransparencyState) {
            return (LodestoneCompositeStateBuilder) super.setTransparencyState(pTransparencyState);
        }

        @Override
        public LodestoneCompositeStateBuilder setDepthTestState(DepthTestStateShard pDepthTestState) {
            return (LodestoneCompositeStateBuilder) super.setDepthTestState(pDepthTestState);
        }

        @Override
        public LodestoneCompositeStateBuilder setCullState(CullStateShard pCullState) {
            return (LodestoneCompositeStateBuilder) super.setCullState(pCullState);
        }

        @Override
        public LodestoneCompositeStateBuilder setLightmapState(LightmapStateShard pLightmapState) {
            return (LodestoneCompositeStateBuilder) super.setLightmapState(pLightmapState);
        }

        @Override
        public LodestoneCompositeStateBuilder setOverlayState(OverlayStateShard pOverlayState) {
            return (LodestoneCompositeStateBuilder) super.setOverlayState(pOverlayState);
        }

        @Override
        public LodestoneCompositeStateBuilder setLayeringState(LayeringStateShard pLayerState) {
            return (LodestoneCompositeStateBuilder) super.setLayeringState(pLayerState);
        }

        @Override
        public LodestoneCompositeStateBuilder setOutputState(OutputStateShard pOutputState) {
            return (LodestoneCompositeStateBuilder) super.setOutputState(pOutputState);
        }

        @Override
        public LodestoneCompositeStateBuilder setTexturingState(TexturingStateShard pTexturingState) {
            return (LodestoneCompositeStateBuilder) super.setTexturingState(pTexturingState);
        }

        @Override
        public LodestoneCompositeStateBuilder setWriteMaskState(WriteMaskStateShard pWriteMaskState) {
            return (LodestoneCompositeStateBuilder) super.setWriteMaskState(pWriteMaskState);
        }

        @Override
        public LodestoneCompositeStateBuilder setLineState(LineStateShard pLineState) {
            return (LodestoneCompositeStateBuilder) super.setLineState(pLineState);
        }
    }
}