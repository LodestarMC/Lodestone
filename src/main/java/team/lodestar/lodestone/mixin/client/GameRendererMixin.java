package team.lodestar.lodestone.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import team.lodestar.lodestone.handlers.rendering.*;
import team.lodestar.lodestone.systems.postprocess.PostProcessHandler;
import team.lodestar.lodestone.systems.rendering.LodestoneRenderSystem;
import team.lodestar.lodestone.systems.rendering.renderpass.RenderPassHandler;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "resize", at = @At(value = "HEAD"))
    public void lodestone$injectionResizeListener(int width, int height, CallbackInfo ci) {
        LodestoneDepthBufferCache.resize(width, height);
        PostProcessHandler.resize(width, height);
        RenderPassHandler.resize(width, height);
    }

    @Inject(
            method = "renderLevel",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/LevelRenderer;prepareCullFrustum(Lnet/minecraft/world/phys/Vec3;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;)V"
            )
    )
    private void lodestone$beforeRenderLevel(DeltaTracker deltaTracker, CallbackInfo ci, @Local Camera camera, @Local(ordinal = 0) Matrix4f projMat, @Local(ordinal = 1) Matrix4f viewMat) {
        RenderPassHandler.render(deltaTracker, camera, (GameRenderer) (Object) this, new Matrix4f(viewMat), new Matrix4f(projMat));
    }

    @ModifyArgs(method = "bobView", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V"))
    public void lodestone$extractViewBob(Args args) {
        LodestoneRenderSystem.setViewBobOffset(-(float) args.get(0), -(float) args.get(1), -(float) args.get(2));
    }

    @Inject(method = "renderLevel", at = @At("HEAD"))
    public void lodestone$clearViewBob(CallbackInfo ci) {
        if (!Minecraft.getInstance().options.bobView().get()) {
            LodestoneRenderSystem.setViewBobOffset(0, 0, 0);
        }
    }
}
