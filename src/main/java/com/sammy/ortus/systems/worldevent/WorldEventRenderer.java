package com.sammy.ortus.systems.worldevent;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class WorldEventRenderer<T extends WorldEventInstance> {

    public boolean canRender(T instance) {
        return false;
    }

    public void render(T instance, PoseStack poseStack, MultiBufferSource bufferSource, float partialTicks) {

    }
}
