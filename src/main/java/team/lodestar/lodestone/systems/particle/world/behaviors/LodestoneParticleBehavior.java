package team.lodestar.lodestone.systems.particle.world.behaviors;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.*;
import team.lodestar.lodestone.systems.particle.world.*;

public interface LodestoneParticleBehavior {

    void render(LodestoneWorldParticle particle, VertexConsumer consumer, Camera camera, float partialTicks);
}
