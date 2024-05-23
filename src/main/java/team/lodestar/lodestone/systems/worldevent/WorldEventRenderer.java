package team.lodestar.lodestone.systems.worldevent;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public abstract class WorldEventRenderer<T extends WorldEventInstance> {

    public boolean canRender(T instance) {
        return false;
    }

    public void render(T instance, PoseStack poseStack, MultiBufferSource bufferSource, float partialTicks) {

    }
}
