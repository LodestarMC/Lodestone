package com.sammy.ortus.systems.rendering.outline;

import com.mojang.blaze3d.vertex.PoseStack;
import com.sammy.ortus.systems.rendering.SuperRenderTypeBuffer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class LineOutline extends Outline {

    protected Vec3 start = Vec3.ZERO;
    protected Vec3 end = Vec3.ZERO;

    public LineOutline set(Vec3 start, Vec3 end) {
        this.start = start;
        this.end = end;
        return this;
    }

    @Override
    public void render(PoseStack ps, SuperRenderTypeBuffer buffer, float pt) {
        renderCuboidLine(ps, buffer, start, end);
    }

}