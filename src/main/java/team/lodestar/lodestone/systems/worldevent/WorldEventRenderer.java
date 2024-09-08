package team.lodestar.lodestone.systems.worldevent;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;

@Environment(EnvType.CLIENT)
public abstract class WorldEventRenderer<T extends WorldEventInstance> {

    public abstract boolean canRender(T instance);

    public abstract void render(T instance, PoseStack poseStack, MultiBufferSource bufferSource, float partialTicks);
}
