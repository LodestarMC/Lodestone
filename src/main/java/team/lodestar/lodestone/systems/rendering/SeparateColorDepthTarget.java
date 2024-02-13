package team.lodestar.lodestone.systems.rendering;

import com.mojang.blaze3d.pipeline.*;
import net.minecraft.client.*;

public class SeparateColorDepthTarget {
    protected final RenderTarget colorTarget;
    protected final RenderTarget depthTarget;

    public SeparateColorDepthTarget(RenderTarget colorTarget, RenderTarget depthTarget) {
        this.colorTarget = colorTarget;
        this.depthTarget = depthTarget;
    }

    public void destroyBuffers() {
        getColorTarget().destroyBuffers();
        getDepthTarget().destroyBuffers();
    }

    public RenderTarget getColorTarget() {
        return colorTarget;
    }

    public RenderTarget getDepthTarget() {
        return depthTarget;
    }

    public void clear() {
        getColorTarget().clear(Minecraft.ON_OSX);
        getDepthTarget().clear(Minecraft.ON_OSX);
    }

    public void bind() {
        clear();
        getColorTarget().copyDepthFrom(Minecraft.getInstance().getMainRenderTarget());
        getColorTarget().bindWrite(false);
        getDepthTarget().bindWrite(false);
    }

    public void unbind() {
        getColorTarget().unbindWrite();
        getDepthTarget().unbindWrite();
    }
}