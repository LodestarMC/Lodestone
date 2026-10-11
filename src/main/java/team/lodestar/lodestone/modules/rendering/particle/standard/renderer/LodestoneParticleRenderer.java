package team.lodestar.lodestone.modules.rendering.particle.standard.renderer;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.*;
import team.lodestar.lodestone.modules.rendering.particle.standard.*;

public interface LodestoneParticleRenderer {

    default void tick(LodestoneParticleBody particle) {

    }

    void render(LodestoneParticleBody particle, VertexConsumer consumer, Camera camera, float partialTicks);
}
