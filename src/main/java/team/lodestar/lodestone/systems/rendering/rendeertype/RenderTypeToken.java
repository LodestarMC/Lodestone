package team.lodestar.lodestone.systems.rendering.rendeertype;

import net.minecraft.client.renderer.*;
import net.minecraft.resources.*;

import java.util.function.*;

public class RenderTypeToken implements Supplier<RenderStateShard.EmptyTextureStateShard> {

    private final String identifier;
    private final RenderStateShard.EmptyTextureStateShard texture;

    public RenderTypeToken(String identifier, ResourceLocation texture) {
        this(identifier, texture, false, false);
    }

    public RenderTypeToken(String identifier, ResourceLocation texture, boolean blur, boolean mipmap) {
        this(identifier, new RenderStateShard.TextureStateShard(texture, blur, mipmap));
    }

    public RenderTypeToken(String identifier, RenderStateShard.EmptyTextureStateShard texture) {
        this.identifier = identifier;
        this.texture = texture;
    }

    @Override
    public RenderStateShard.EmptyTextureStateShard get() {
        return texture;
    }

    public String getIdentifier() {
        return identifier;
    }
}
