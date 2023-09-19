package team.lodestar.lodestone.systems.rendering.trail;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector4f;
import net.minecraft.world.phys.Vec2;
import team.lodestar.lodestone.systems.rendering.*;

public class TrailRenderPoint {

    public final float xp;
    public final float xn;
    public final float yp;
    public final float yn;
    public final float z;

    public TrailRenderPoint(float xp, float xn, float yp, float yn, float z) {
        this.xp = xp;
        this.xn = xn;
        this.yp = yp;
        this.yn = yn;
        this.z = z;
    }

    public TrailRenderPoint(Vector4f pos, Vec2 perp) {
        this(pos.x() + perp.x, pos.x() - perp.x, pos.y() + perp.y, pos.y() - perp.y, pos.z());
    }

    public void renderStart(VertexConsumer builder, VFXBuilders.WorldVFXBuilder.WorldVertexPlacementSupplier supplier, float u0, float v0, float u1, float v1) {
        supplier.placeVertex(builder, null, xp, yp, z, u0, v0);
        supplier.placeVertex(builder, null, xn, yn, z, u1, v0);
    }

    public void renderEnd(VertexConsumer builder, VFXBuilders.WorldVFXBuilder.WorldVertexPlacementSupplier supplier, float u0, float v0, float u1, float v1) {
        supplier.placeVertex(builder, null, xn, yn, z, u1, v1);
        supplier.placeVertex(builder, null, xp, yp, z, u0, v1);
    }

    public void renderMid(VertexConsumer builder, VFXBuilders.WorldVFXBuilder.WorldVertexPlacementSupplier supplier, float u0, float v0, float u1, float v1) {
        renderEnd(builder, supplier, u0, v0, u1, v1);
        renderStart(builder, supplier, u0, v0, u1, v1);
    }
}