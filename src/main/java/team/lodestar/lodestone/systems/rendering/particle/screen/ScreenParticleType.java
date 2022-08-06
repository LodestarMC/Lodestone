package team.lodestar.lodestone.systems.rendering.particle.screen;

import team.lodestar.lodestone.systems.rendering.particle.screen.base.ScreenParticle;
import net.minecraft.client.multiplayer.ClientLevel;

public class ScreenParticleType<T extends ScreenParticleOptions>  extends net.minecraftforge.registries.ForgeRegistryEntry<ScreenParticleType<?>> {

   public ParticleProvider<T> provider;
   public ScreenParticleType() {
   }

   public interface ParticleProvider<T extends ScreenParticleOptions> {
      ScreenParticle createParticle(ClientLevel pLevel, T options, double pX, double pY, double pXSpeed, double pYSpeed);
   }
}