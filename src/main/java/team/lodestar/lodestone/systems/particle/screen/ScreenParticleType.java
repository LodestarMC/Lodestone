package team.lodestar.lodestone.systems.particle.screen;

import net.minecraft.client.multiplayer.ClientLevel;
import team.lodestar.lodestone.systems.particle.options.ScreenParticleOptions;
import team.lodestar.lodestone.systems.particle.screen.base.ScreenParticle;

public class ScreenParticleType<T extends ScreenParticleOptions> {

    public ParticleProvider<T> provider;

    public ScreenParticleType() {
    }

    public interface ParticleProvider<T extends ScreenParticleOptions> {
        ScreenParticle createParticle(ClientLevel pLevel, T options, double x, double y, double pXSpeed, double pYSpeed);
    }
}