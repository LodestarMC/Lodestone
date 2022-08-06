package team.lodestar.lodestone.systems.rendering.outline;

import com.mojang.blaze3d.vertex.PoseStack;
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
    public void render(PoseStack ps, float pt) {
        renderCuboidLine(ps, start, end);
    }

}