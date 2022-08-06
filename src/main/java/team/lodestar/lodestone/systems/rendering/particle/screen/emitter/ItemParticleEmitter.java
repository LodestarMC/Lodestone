package team.lodestar.lodestone.systems.rendering.particle.screen.emitter;

import team.lodestar.lodestone.systems.rendering.particle.screen.base.ScreenParticle;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface ItemParticleEmitter {
    @OnlyIn(value = Dist.CLIENT)
    void particleTick(ItemStack stack, float x, float y, ScreenParticle.RenderOrder renderOrder);
}
