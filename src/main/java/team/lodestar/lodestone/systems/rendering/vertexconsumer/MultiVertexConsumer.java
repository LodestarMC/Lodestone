package team.lodestar.lodestone.systems.rendering.vertexconsumer;

import com.mojang.blaze3d.vertex.VertexConsumer;

/**
 * A {@link VertexConsumer} that forwards all calls to multiple other {@link VertexConsumer}s.
 * <p>Useful for rendering the same geometry to multiple buffers.</p>
 */
public class MultiVertexConsumer extends VertexConsumerWrapper {
    protected final VertexConsumer[] consumers;

    public MultiVertexConsumer(VertexConsumer... consumers) {
        this.consumers = consumers;
    }

    @Override
    public VertexConsumer[] getWrappedConsumers() {
        return consumers;
    }
}
