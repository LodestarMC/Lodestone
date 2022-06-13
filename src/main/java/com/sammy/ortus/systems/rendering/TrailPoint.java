package com.sammy.ortus.systems.rendering;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector4f;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.phys.Vec2;

public class TrailPoint {

    public final float xp;
    public final float xn;
    public final float yp;
    public final float yn;
    public final float z;

    public TrailPoint(float xp, float xn, float yp, float yn, float z) {
        this.xp = xp;
        this.xn = xn;
        this.yp = yp;
        this.yn = yn;
        this.z = z;
    }

    public TrailPoint(Vector4f pos, Vec2 perp) {
        this(pos.x() + perp.x, pos.x() - perp.x, pos.y() + perp.y, pos.y() - perp.y, pos.z());
    }

    public void renderStart(VertexConsumer builder, Matrix4f last, VFXBuilders.WorldVFXBuilder.WorldVertexPlacementSupplier supplier, float u0, float v0, float u1, float v1) {
        supplier.placeVertex(builder, last, xp, yp, z, u0, v0);
        supplier.placeVertex(builder, last, xn, yn, z, u1, v0);
    }

    public void renderEnd(VertexConsumer builder, Matrix4f last, VFXBuilders.WorldVFXBuilder.WorldVertexPlacementSupplier supplier, float u0, float v0, float u1, float v1) {
        supplier.placeVertex(builder, last, xn, yn, z, u1, v1);
        supplier.placeVertex(builder, last, xp, yp, z, u0, v1);
    }

    public void renderMid(VertexConsumer builder, Matrix4f last, VFXBuilders.WorldVFXBuilder.WorldVertexPlacementSupplier supplier, float u0, float v0, float u1, float v1) {
        renderEnd(builder, last, supplier, u0, v0, u1, v1);
        renderStart(builder, last, supplier, u0, v0, u1, v1);
    }
}