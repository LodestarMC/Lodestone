package com.sammy.ortus.util.transforms;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;

import net.minecraft.core.Direction;

public interface Transform<Self extends Transform<Self>> extends Translate<Self>, Rotate<Self> {
    Self mulPose(Matrix4f pose);

    Self mulNormal(Matrix3f normal);

    default Self transform(Matrix4f pose, Matrix3f normal) {
        mulPose(pose);
        return mulNormal(normal);
    }
}