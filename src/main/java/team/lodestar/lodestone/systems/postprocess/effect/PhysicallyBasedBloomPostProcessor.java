package team.lodestar.lodestone.systems.postprocess.effect;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.helpers.StateShardHelper;
import team.lodestar.lodestone.systems.postprocess.PostProcessor;

public class PhysicallyBasedBloomPostProcessor extends PostProcessor {
    private RenderTarget bloomTarget;
    private RenderTarget BLURX2, BLURY2, BLURX4, BLURY4, BLURX8, BLURY8;

    private boolean forceDisabled;

    public PhysicallyBasedBloomPostProcessor() {
        this.setActive(false);
    }
    @Override
    public ResourceLocation getPostChainLocation() {
        return LodestoneLib.lodestonePath("pb_bloom");
    }

    @Override
    public void init() {
        super.init();
        if (this.postChain != null) {
            this.bloomTarget = this.postChain.getTempTarget("bloomColor");

            this.BLURX2 = this.postChain.getTempTarget("blurX2");
            this.BLURY2 = this.postChain.getTempTarget("blurY2");
            this.BLURX4 = this.postChain.getTempTarget("blurX4");
            this.BLURY4 = this.postChain.getTempTarget("blurY4");
            this.BLURX8 = this.postChain.getTempTarget("blurX8");
            this.BLURY8 = this.postChain.getTempTarget("blurY8");
            var window = Minecraft.getInstance().getWindow();
            this.resize(window.getWidth(), window.getHeight());
        }
    }

    @Override
    public void beforeProcess(Matrix4f viewModelMatrix) {
    }

    @Override
    public void afterProcess() {
        if (this.bloomTarget == null) return;
        this.bloomTarget.clear(Minecraft.ON_OSX);
    }

    @Override
    public void resize(int width, int height) {
        if (this.postChain == null) return;
        this.postChain.screenWidth = width;
        this.postChain.screenHeight = this.postChain.screenTarget.height;
        this.postChain.updateOrthoMatrix();

        for(PostPass postpass : this.postChain.passes) {
            postpass.setOrthoMatrix(this.postChain.shaderOrthoMatrix);
        }

        this.bloomTarget.resize(width, height, Minecraft.ON_OSX);

        resize(this.BLURX2, width, height, 2);
        resize(this.BLURY2, width, height, 2);
        resize(this.BLURX4, width, height, 4);
        resize(this.BLURY4, width, height, 4);
        resize(this.BLURX8, width, height, 8);
        resize(this.BLURY8, width, height, 8);
    }

    private void resize(RenderTarget target, int width, int height, int divisor) {
        if (target == null) return;
        target.resize(width / divisor, height / divisor, Minecraft.ON_OSX);
        target.setFilterMode(GL11.GL_LINEAR);
    }

    public void forceDisable() {
        this.forceDisabled = true;
        this.setActive(false);
    }

    @Override
    public void setActive(boolean active) {
        if (this.forceDisabled) active = false;
        super.setActive(active);
    }

    public RenderStateShard.OutputStateShard getBloomOutput() {
        return StateShardHelper.createOutputState("bloomTarget", () -> { if (this.bloomTarget != null) this.bloomTarget.bindWrite(false); });
    }

    public RenderTarget getBloomTarget() {
        return bloomTarget;
    }

    public void copyDepthFromMain() {
        this.copyDepthFrom(Minecraft.getInstance().getMainRenderTarget());
    }

    public void copyDepthFrom(RenderTarget src) {
        if (this.bloomTarget == null || src == null) return;
        this.bloomTarget.copyDepthFrom(src);
        GlStateManager._glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, src.frameBufferId);
    }
}
