package team.lodestar.lodestone.systems.worldevent;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class WorldEventRenderer<T extends WorldEventInstance> {

    public abstract boolean canRender(T instance);

    public abstract void render(T instance, PoseStack poseStack, MultiBufferSource bufferSource, float partialTicks);
}
