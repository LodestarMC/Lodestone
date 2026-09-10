package team.lodestar.lodestone.registry.client;

import com.mojang.blaze3d.platform.*;
import com.mojang.blaze3d.systems.*;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.*;
import team.lodestar.lodestone.systems.rendering.*;
import team.lodestar.lodestone.systems.rendering.rendeertype.*;
import team.lodestar.lodestone.systems.rendering.shader.ShaderHolder;
import team.lodestar.lodestone.systems.rendering.uniform.UniformData;

import javax.annotation.*;
import java.util.function.*;

import static com.mojang.blaze3d.vertex.DefaultVertexFormat.*;
import static com.mojang.blaze3d.vertex.VertexFormat.Mode.*;

@SuppressWarnings({"deprecation", "unused", "UnusedReturnValue"})
public class LodestoneRenderTypes extends RenderStateShard {

    public static final Runnable TRANSPARENT_FUNCTION = () -> RenderSystem.blendFuncSeparate(
            GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
            GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

    public static final Runnable ADDITIVE_FUNCTION = () -> RenderSystem.blendFunc(
            GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);

    public static final EmptyTextureStateShard NO_TEXTURE = RenderStateShard.NO_TEXTURE;
    public static final LightmapStateShard LIGHTMAP = RenderStateShard.LIGHTMAP;
    public static final LightmapStateShard NO_LIGHTMAP = RenderStateShard.NO_LIGHTMAP;
    public static final CullStateShard CULL = RenderStateShard.CULL;
    public static final CullStateShard NO_CULL = RenderStateShard.NO_CULL;
    public static final WriteMaskStateShard COLOR_DEPTH_WRITE = RenderStateShard.COLOR_DEPTH_WRITE;
    public static final WriteMaskStateShard COLOR_WRITE = RenderStateShard.COLOR_WRITE;

    public LodestoneRenderTypes(String p_110161_, Runnable p_110162_, Runnable p_110163_) {
        super(p_110161_, p_110162_, p_110163_);
    }

    public static final LodestoneRenderType ADDITIVE_PARTICLE = createGenericRenderType(
            RenderTypeToken.createToken(TextureAtlas.LOCATION_PARTICLES), "additive_particle", PARTICLE, QUADS,
            b -> b.setStateShards(StateShards.ADDITIVE_TRANSPARENCY, LodestoneShaders.PARTICLE, NO_CULL, LIGHTMAP, COLOR_WRITE)
    );
    public static final LodestoneRenderType ADDITIVE_BLOCK_PARTICLE = createGenericRenderType(
            RenderTypeToken.createToken(TextureAtlas.LOCATION_BLOCKS), "additive_block_particle", PARTICLE, QUADS,
            b -> b.setStateShards(StateShards.ADDITIVE_TRANSPARENCY, LodestoneShaders.PARTICLE, NO_CULL, LIGHTMAP, COLOR_WRITE)
    );
    public static final LodestoneRenderType ADDITIVE_BLOCK = createGenericRenderType(
            RenderTypeToken.createToken(TextureAtlas.LOCATION_BLOCKS), "additive_block", POSITION_COLOR_TEX_LIGHTMAP, QUADS,
            b -> b.setStateShards(StateShards.ADDITIVE_TRANSPARENCY, LodestoneShaders.LODESTONE_TEXTURE, NO_CULL, LIGHTMAP, COLOR_WRITE)
    );

    public static final LodestoneRenderType TRANSPARENT_PARTICLE = createGenericRenderType(
            RenderTypeToken.createToken(TextureAtlas.LOCATION_PARTICLES), "transparent_particle", PARTICLE, QUADS,
            b -> b.setStateShards(StateShards.NORMAL_TRANSPARENCY, LodestoneShaders.PARTICLE, NO_CULL, LIGHTMAP, COLOR_WRITE)
    );
    public static final LodestoneRenderType TRANSPARENT_BLOCK_PARTICLE = createGenericRenderType(
            RenderTypeToken.createToken(TextureAtlas.LOCATION_BLOCKS), "transparent_block_particle", PARTICLE, QUADS,
            b -> b.setStateShards(StateShards.NORMAL_TRANSPARENCY, LodestoneShaders.PARTICLE, NO_CULL, LIGHTMAP, COLOR_WRITE)
    );
    public static final LodestoneRenderType TRANSPARENT_BLOCK = createGenericRenderType(
            RenderTypeToken.createToken(TextureAtlas.LOCATION_BLOCKS), "transparent_block", POSITION_COLOR_TEX_LIGHTMAP, QUADS,
            b -> b.setStateShards(StateShards.NORMAL_TRANSPARENCY, LodestoneShaders.LODESTONE_TEXTURE, NO_CULL, LIGHTMAP, COLOR_WRITE)
    );

    public static final LodestoneRenderType LUMITRANSPARENT_PARTICLE = TRANSPARENT_PARTICLE.copyAndModify("lumitransparent", UniformData.LUMITRANSPARENT);
    public static final LodestoneRenderType LUMITRANSPARENT_BLOCK_PARTICLE = TRANSPARENT_BLOCK_PARTICLE.copyAndModify("lumitransparent", UniformData.LUMITRANSPARENT);
    public static final LodestoneRenderType LUMITRANSPARENT_BLOCK = TRANSPARENT_BLOCK.copyAndModify("lumitransparent", UniformData.LUMITRANSPARENT);

    public static final RenderTypeProvider TEXTURE = new RenderTypeProvider((token) ->
            createGenericRenderType(token, "texture", POSITION_COLOR_TEX_LIGHTMAP,
                    QUADS, builder(token, StateShards.NO_TRANSPARENCY, LodestoneShaders.LODESTONE_TEXTURE, CULL, LIGHTMAP)));

    public static final RenderTypeProvider CUTOUT_TEXTURE = new RenderTypeProvider((token) ->
            createGenericRenderType(token, "cutout_texture", POSITION_COLOR_TEX_LIGHTMAP,
                    QUADS, builder(token, StateShards.NO_TRANSPARENCY, LodestoneShaders.LODESTONE_TEXTURE, CULL, LIGHTMAP)));

    public static final RenderTypeProvider TRANSPARENT_TEXT = new RenderTypeProvider((token) ->
            createGenericRenderType(token, "transparent_text", POSITION_COLOR_TEX_LIGHTMAP,
                    QUADS, builder(token, StateShards.NORMAL_TRANSPARENCY, LodestoneShaders.LODESTONE_TEXT, LIGHTMAP)));

    public static final RenderTypeProvider ADDITIVE_TEXT = new RenderTypeProvider((token) ->
            createGenericRenderType(token, "additive_text", POSITION_COLOR_TEX_LIGHTMAP,
                    QUADS, builder(token, StateShards.ADDITIVE_TRANSPARENCY, LodestoneShaders.LODESTONE_TEXT, LIGHTMAP, COLOR_WRITE)));

    public static final RenderTypeProvider TRANSPARENT_TEXTURE = new RenderTypeProvider((token) ->
            createTransparentRenderType("transparent_texture", token, LodestoneShaders.LODESTONE_TEXTURE));
    public static final RenderTypeProvider TRANSPARENT_DISTORTED_TEXTURE = new RenderTypeProvider((token) ->
            createTransparentRenderType("transparent_distorted_texture", token, LodestoneShaders.DISTORTED_TEXTURE));
    public static final RenderTypeProvider TRANSPARENT_TEXTURE_TRIANGLE = new RenderTypeProvider((token) ->
            createTransparentRenderType("transparent_triangle_texture", token, LodestoneShaders.TRIANGLE_TEXTURE));
    public static final RenderTypeProvider TRANSPARENT_TWO_SIDED_TEXTURE_TRIANGLE = new RenderTypeProvider((token) ->
            createTransparentRenderType("transparent_two_sided_triangle_texture", token, LodestoneShaders.TWO_SIDED_TRIANGLE_TEXTURE));
    public static final RenderTypeProvider TRANSPARENT_ROUNDED_TEXTURE_TRIANGLE = new RenderTypeProvider((token) ->
            createTransparentRenderType("transparent_rounded_triangle_texture", token, LodestoneShaders.ROUNDED_TRIANGLE_TEXTURE));
    public static final RenderTypeProvider TRANSPARENT_SCROLLING_TEXTURE_TRIANGLE = new RenderTypeProvider((token) ->
            createTransparentRenderType("transparent_scrolling_texture_triangle", token, LodestoneShaders.ROUNDED_TRIANGLE_TEXTURE));
    public static final RenderTypeProvider TRANSPARENT_NINE_SLICE_TEXTURE = new RenderTypeProvider((token) ->
            createTransparentRenderType("transparent_nine_slice_texture", token, LodestoneShaders.NINE_SLICE));
    public static final RenderTypeProvider TRANSPARENT_DISTORTED_NINE_SLICE_TEXTURE = new RenderTypeProvider((token) ->
            createTransparentRenderType("transparent_distorted_nine_slice_texture", token, LodestoneShaders.DISTORTED_NINE_SLICE_TEXTURE));

    public static final RenderTypeProvider ADDITIVE_TEXTURE = new RenderTypeProvider((token) ->
            createAdditiveRenderType("additive_texture", token, LodestoneShaders.LODESTONE_TEXTURE));
    public static final RenderTypeProvider ADDITIVE_DISTORTED_TEXTURE = new RenderTypeProvider((token) ->
            createAdditiveRenderType("additive_distorted_texture", token, LodestoneShaders.DISTORTED_TEXTURE));
    public static final RenderTypeProvider ADDITIVE_TEXTURE_TRIANGLE = new RenderTypeProvider((token) ->
            createAdditiveRenderType("additive_triangle_texture", token, LodestoneShaders.TRIANGLE_TEXTURE));
    public static final RenderTypeProvider ADDITIVE_TWO_SIDED_TEXTURE_TRIANGLE = new RenderTypeProvider((token) ->
            createAdditiveRenderType("additive_two_sided_triangle_texture", token, LodestoneShaders.TWO_SIDED_TRIANGLE_TEXTURE));
    public static final RenderTypeProvider ADDITIVE_ROUNDED_TEXTURE_TRIANGLE = new RenderTypeProvider((token) ->
            createAdditiveRenderType("additive_rounded_triangle_texture", token, LodestoneShaders.ROUNDED_TRIANGLE_TEXTURE));
    public static final RenderTypeProvider ADDITIVE_SCROLLING_TEXTURE_TRIANGLE = new RenderTypeProvider((token) ->
            createAdditiveRenderType("additive_scrolling_texture_triangle", token, LodestoneShaders.ROUNDED_TRIANGLE_TEXTURE));
    public static final RenderTypeProvider ADDITIVE_NINE_SLICE_TEXTURE = new RenderTypeProvider((token) ->
            createAdditiveRenderType("additive_nine_slice_texture", token, LodestoneShaders.NINE_SLICE));
    public static final RenderTypeProvider ADDITIVE_DISTORTED_NINE_SLICE_TEXTURE = new RenderTypeProvider((token) ->
            createAdditiveRenderType("additive_distorted_nine_slice_texture", token, LodestoneShaders.DISTORTED_NINE_SLICE_TEXTURE));

    /**
     * A render type that fades out the texture's alpha channel based on depth proximity.
     */
    public static final RenderTypeProvider TEXTURE_FADE = new RenderTypeProvider((token) ->
            createGenericRenderType(token, "texture_fade", POSITION_COLOR_TEX_LIGHTMAP,
                    QUADS, builder(token, StateShards.NORMAL_TRANSPARENCY, LodestoneShaders.TEXTURE_FADE, CULL, LIGHTMAP)));

    /**
     * Who knows what this one might do.
     */
    public static final RenderTypeProvider DEBUG_SDF = new RenderTypeProvider((token) ->
            createGenericRenderType(token, "debug_sdf", POSITION,
                    QUADS, builder(token, LodestoneShaders.DEBUG_SDF, CULL)));

    public static final RenderType DEBUG_POS_TEX = RenderType.create("pos_tex", POSITION_TEX, QUADS, 256, RenderType.CompositeState.builder()
            .setShaderState(LodestoneShaders.DEBUG_POS_TEX.getShard())
            .setTextureState(new TextureStateShard(LodestoneLib.lodestonePath("textures/painting/lefunny.png"), false, false))
            .setCullState(NO_CULL)
            .createCompositeState(false)
    );

    public static final RenderType DEBUG_TRAIL = RenderType.create("trail", POSITION_TEX_COLOR, TRIANGLE_STRIP, 256, RenderType.CompositeState.builder()
            .setShaderState(LodestoneShaders.DEBUG_TRAIL.getShard())
            .setTextureState(new TextureStateShard(LodestoneLib.lodestonePath("textures/painting/lefunny.png"), false, false))
            .setTransparencyState(StateShards.NORMAL_TRANSPARENCY)
            .setCullState(NO_CULL)
            .createCompositeState(false)
    );

    public static final RenderType DEBUG_POS_TEX_MAT = RenderType.create("pos_tex", POSITION_TEX, QUADS, 256, RenderType.CompositeState.builder()
            .setShaderState(LodestoneShaders.DEBUG_POS_TEX_MAT.getShard())
            .setTextureState(new TextureStateShard(LodestoneLib.lodestonePath("textures/painting/lefunny.png"), false, false))
            .setCullState(NO_CULL)
            .createCompositeState(false)
    );

    public static LodestoneRenderType createTransparentRenderType(String name, RenderTypeToken token, ShaderHolder shader) {
        return createGenericRenderType(token, name, POSITION_COLOR_TEX_LIGHTMAP,
                QUADS, builder(token, StateShards.NORMAL_TRANSPARENCY, shader, CULL, LIGHTMAP, COLOR_WRITE));
    }

    public static LodestoneRenderType createAdditiveRenderType(String name, RenderTypeToken token, ShaderHolder shader) {
        return createGenericRenderType(token, name, POSITION_COLOR_TEX_LIGHTMAP,
                QUADS, builder(token, StateShards.ADDITIVE_TRANSPARENCY, shader, CULL, LIGHTMAP, COLOR_WRITE));
    }

    public static LodestoneRenderType createGenericRenderType(@Nullable RenderTypeToken token, String name, VertexFormat format, VertexFormat.Mode mode, Consumer<LodestoneCompositeStateBuilder> modifier) {
        LodestoneCompositeStateBuilder builder = new LodestoneCompositeStateBuilder();
        if (token != null) {
            builder.setTextureState(token);
        }
        modifier.accept(builder);
        return createGenericRenderType(token, name, format, mode, builder);
    }

    public static LodestoneRenderType createGenericRenderType(@Nullable RenderTypeToken token, String name, VertexFormat format, VertexFormat.Mode mode, LodestoneCompositeStateBuilder builder) {
        UniformData uniformData = null;
        if (token instanceof ComplexRenderTypeToken complex) {
            uniformData = complex.getUniformData();
            var modifier = complex.getModifier();
            if (modifier != null) {
                modifier.accept(builder);
            }
        }
        var renderTypeMode = builder.modeOverride != null ? builder.modeOverride : mode;
        var compositeState = builder.createCompositeState();
        return new LodestoneRenderType(name, format, renderTypeMode, 256, false, true, compositeState, uniformData);
    }

    public static LodestoneCompositeStateBuilder builder(Object... objects) {
        return builder().setStateShards(objects);
    }
    public static LodestoneCompositeStateBuilder builder() {
        return new LodestoneCompositeStateBuilder();
    }

    @SuppressWarnings("UnusedReturnValue")
    public static class LodestoneCompositeStateBuilder extends RenderType.CompositeState.CompositeStateBuilder {

        protected VertexFormat.Mode modeOverride;
        LodestoneCompositeStateBuilder() {
            super();
        }

        public LodestoneCompositeStateBuilder replaceVertexFormat(VertexFormat.Mode mode) {
            this.modeOverride = mode;
            return this;
        }

        public LodestoneCompositeStateBuilder accepts(Consumer<LodestoneCompositeStateBuilder> modifier) {
            modifier.accept(this);
            return this;
        }

        public LodestoneCompositeStateBuilder setStateShards(Object... objects) {
            for (Object object : objects) {
                if (object instanceof RenderType.CompositeState state) {
                    copyState(state);
                }
            }

            for (Object object : objects) {
                switch (object) {
                    case ResourceLocation texture -> setTextureState(texture);
                    case RenderTypeToken token -> setTextureState(token);
                    case EmptyTextureStateShard shard -> setTextureState(shard);
                    case ShaderInstance shaderInstance -> setShaderState(shaderInstance);
                    case ShaderHolder shaderHolder -> setShaderState(shaderHolder);
                    case ShaderStateShard shard -> setShaderState(shard);
                    case TransparencyStateShard shard -> setTransparencyState(shard);
                    case DepthTestStateShard shard -> setDepthTestState(shard);
                    case CullStateShard shard -> setCullState(shard);
                    case LightmapStateShard shard -> setLightmapState(shard);
                    case OverlayStateShard shard -> setOverlayState(shard);
                    case LayeringStateShard shard -> setLayeringState(shard);
                    case OutputStateShard shard -> setOutputState(shard);
                    case TexturingStateShard shard -> setTexturingState(shard);
                    case WriteMaskStateShard shard -> setWriteMaskState(shard);
                    case LineStateShard shard -> setLineState(shard);
                    case ColorLogicStateShard shard -> setColorLogicState(shard);
                    case null -> LodestoneLib.LOGGER.warn("Null object passed for composite state, ignoring.");
                    default -> LodestoneLib.LOGGER.warn("Unsupported object passed for composite state: {}, {}", object.getClass().getName(), object);
                }
            }
            return this;
        }

        public LodestoneCompositeStateBuilder copyState(RenderType.CompositeState state) {
            setTextureState(state.textureState);
            setShaderState(state.shaderState);
            setTransparencyState(state.transparencyState);
            setDepthTestState(state.depthTestState);
            setCullState(state.cullState);
            setLightmapState(state.lightmapState);
            setOverlayState(state.overlayState);
            setLayeringState(state.layeringState);
            setOutputState(state.outputState);
            setTexturingState(state.texturingState);
            setWriteMaskState(state.writeMaskState);
            setLineState(state.lineState);
            setColorLogicState(state.colorLogicState);
            return this;
        }

        public LodestoneCompositeStateBuilder setTextureState(RenderTypeToken token) {
            return setTextureState(token.getTexture());
        }

        public LodestoneCompositeStateBuilder setTextureState(ResourceLocation texture) {
            return setTextureState(new RenderStateShard.TextureStateShard(texture, false, false));
        }

        public LodestoneCompositeStateBuilder setShaderState(ShaderInstance shaderInstance) {
            return setShaderState(new ShaderStateShard(() -> shaderInstance));
        }

        public LodestoneCompositeStateBuilder setShaderState(ShaderHolder shaderHolder) {
            return setShaderState(shaderHolder.getShard());
        }

        public RenderType.CompositeState createCompositeState() {
            return super.createCompositeState(true);
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