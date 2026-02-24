package team.lodestar.lodestone.systems.rendering.vertexconsumer.offset;

import com.mojang.blaze3d.vertex.*;
import org.jetbrains.annotations.*;
import team.lodestar.lodestone.systems.rendering.vertexconsumer.*;
import team.lodestar.lodestone.systems.rendering.vertexconsumer.*;

/**
 * A {@link VertexConsumer} that allows for an offset to be applied to the UV coordinates.
 */
@SuppressWarnings("unused")
public class UVOffsetVertexConsumer extends VertexConsumerWrapper {

    protected final VertexConsumer consumer;
    protected final VertexConsumer[] cachedArray;
    protected float uOffset;
    protected float vOffset;

    public UVOffsetVertexConsumer(VertexConsumer consumer) {
        this.consumer = consumer;
        this.cachedArray = new VertexConsumer[]{consumer};
    }

    @Override
    public VertexConsumer[] getWrappedConsumers() {
        return cachedArray;
    }

    public UVOffsetVertexConsumer setOffset(float uOffset, float vOffset) {
        this.uOffset = uOffset;
        this.vOffset = vOffset;
        return this;
    }

    @Override
    public @NotNull VertexConsumer setUv(float u, float v) {
        return super.setUv(u+uOffset, v+vOffset);
    }
}
