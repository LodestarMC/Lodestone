package com.sammy.ortus.util.transforms;

import com.mojang.blaze3d.vertex.PoseStack;

public interface TransformStack extends Transform<TransformStack>{
    static TransformStack cast(PoseStack stack) {
        return (TransformStack) stack;
    }
}