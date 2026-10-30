package team.lodestar.lodestone.systems.rendering.rendeertype;

import net.minecraft.client.renderer.*;
import net.minecraft.resources.*;
import team.lodestar.lodestone.registry.client.*;
import team.lodestar.lodestone.systems.rendering.uniform.UniformData;

import java.util.*;
import java.util.function.*;

/**
 * A token representing a render type
 * Currently does not support a definition without a texture
 * Each unique token will create a unique render type and as such tokens are cached depending on the texture.
 */
public class RenderTypeToken {

    private static final HashMap<ResourceLocation, RenderTypeToken> CACHED_TEXTURE_TOKENS = new HashMap<>();
    private static final HashMap<RenderStateShard.TextureStateShard, RenderTypeToken> CACHED_STATE_TOKENS = new HashMap<>();

    private final UUID identifier;
    private final ResourceLocation texture;

    protected RenderTypeToken(RenderStateShard.TextureStateShard texture) {
        this(texture.cutoutTexture().orElseThrow());
    }

    protected RenderTypeToken(ResourceLocation texture) {
        this(UUID.randomUUID(), texture);
    }

    public RenderTypeToken(UUID identifier, ResourceLocation texture) {
        this.identifier = identifier;
        this.texture = texture;
    }

    public static RenderTypeToken createToken(ResourceLocation texture) {
        return CACHED_TEXTURE_TOKENS.computeIfAbsent(texture, RenderTypeToken::new);
    }

    public static RenderTypeToken createToken(RenderStateShard.TextureStateShard texture) {
        return CACHED_STATE_TOKENS.computeIfAbsent(texture, RenderTypeToken::new);
    }

    public ComplexRenderTypeToken addUniformData(UniformData data) {
        return new ComplexRenderTypeToken(this).addUniformData(data);
    }

    public ComplexRenderTypeToken addModifier(Consumer<LodestoneRenderTypes.LodestoneCompositeStateBuilder> modifier) {
        return new ComplexRenderTypeToken(this).addModifier(modifier);
    }

    protected RenderTypeToken unique() {
        return new RenderTypeToken(identifier, texture);
    }

    public UUID getIdentifier() {
        return identifier;
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    @Override
    public String toString() {
        return "RenderTypeToken{" +
                "identifier=" + identifier +
                ", texture=" + texture +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenderTypeToken that = (RenderTypeToken) o;
        return Objects.equals(identifier, that.identifier) && Objects.equals(texture, that.texture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, texture);
    }
}