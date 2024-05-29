package team.lodestar.lodestone.systems.particle.world.behaviors;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.*;
import team.lodestar.lodestone.systems.particle.world.*;

public interface LodestoneParticleBehavior<T extends LodestoneParticleBehavior<T>> {

    void render(LodestoneWorldParticle<T> particle, VertexConsumer consumer, Camera camera, float partialTicks);
}
