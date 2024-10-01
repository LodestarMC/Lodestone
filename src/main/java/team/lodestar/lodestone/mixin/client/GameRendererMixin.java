package team.lodestar.lodestone.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.shaders.Program;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import team.lodestar.lodestone.events.LodestoneRenderEvents;
import team.lodestar.lodestone.events.LodestoneShaderRegistrationEvent;
import team.lodestar.lodestone.events.Stage;
import team.lodestar.lodestone.handlers.*;
import team.lodestar.lodestone.systems.postprocess.PostProcessHandler;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "resize", at = @At(value = "HEAD"))
    public void lodestone$injectionResizeListener(int width, int height, CallbackInfo ci) {
        RenderHandler.resize(width, height);
        PostProcessHandler.resize(width, height);
    }

    @Inject(method = "reloadShaders", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
    private void lodestone$registerShaders(ResourceProvider resourceProvider, CallbackInfo ci, List<Program> list, List<Pair<ShaderInstance, Consumer<ShaderInstance>>> list2) throws IOException {
        LodestoneShaderRegistrationEvent.EVENT.invoker().register(resourceProvider, list2);
    }

    @Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V"))
    private void lodestone$injectAfterLevelEvent(DeltaTracker deltaTracker, CallbackInfo ci) {
        LodestoneRenderEvents.AFTER_LEVEL.invoker().render(poseStack, deltaTracker.getGameTimeDeltaTicks(), Stage.AFTER_LEVEL);
    }
}
