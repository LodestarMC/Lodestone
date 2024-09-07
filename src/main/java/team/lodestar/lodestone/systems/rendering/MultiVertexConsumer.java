package team.lodestar.lodestone.systems.rendering;

import com.mojang.blaze3d.vertex.VertexConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A {@link VertexConsumer} that forwards all calls to multiple other {@link VertexConsumer}s.
 * <p>Useful for rendering the same geometry to multiple buffers.</p>
 */
@SuppressWarnings("unused")
public class MultiVertexConsumer implements VertexConsumer {
    private final List<VertexConsumer> consumers;

    public MultiVertexConsumer(VertexConsumer... consumers) {
        this(List.of(consumers));
    }

    public MultiVertexConsumer(List<VertexConsumer> consumers) {
        this.consumers = consumers;
    }

    @Override
    public @NotNull VertexConsumer addVertex(float v, float v1, float v2) {
        consumers.forEach(consumer -> consumer.addVertex(v, v1, v2));
        return this;
    }

    @Override
    public @NotNull VertexConsumer setColor(int i, int i1, int i2, int i3) {
        consumers.forEach(consumer -> consumer.setColor(i, i1, i2, i3));
        return this;
    }

    @Override
    public @NotNull VertexConsumer setUv(float v, float v1) {
        consumers.forEach(consumer -> consumer.setUv(v, v1));
        return this;
    }

    @Override
    public @NotNull VertexConsumer setUv1(int i, int i1) {
        consumers.forEach(consumer -> consumer.setUv1(i, i1));
        return this;
    }

    @Override
    public @NotNull VertexConsumer setUv2(int i, int i1) {
        consumers.forEach(consumer -> consumer.setUv2(i, i1));
        return this;
    }

    @Override
    public @NotNull VertexConsumer setNormal(float v, float v1, float v2) {
        consumers.forEach(consumer -> consumer.setNormal(v, v1, v2));
        return this;
    }
}
