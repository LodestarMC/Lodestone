package team.lodestar.lodestone.systems.rendering;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.*;
import team.lodestar.lodestone.systems.rendering.rendeertype.*;

public class LodestoneBufferWrapper implements MultiBufferSource {

    public final RenderTypeProvider provider;
    public final MultiBufferSource buffer;

    public LodestoneBufferWrapper(RenderTypeProvider provider, MultiBufferSource buffer) {
        this.provider = provider;
        this.buffer = buffer;
    }

    @Override
    public VertexConsumer getBuffer(RenderType renderType) {
        if (renderType instanceof RenderType.CompositeRenderType composite)
            return buffer.getBuffer(provider.applyAndCache(RenderTypeToken.createCachedToken(composite.state.textureState)));
        return buffer.getBuffer(renderType);
    }
}