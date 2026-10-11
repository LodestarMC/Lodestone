package team.lodestar.lodestone.modules.rendering.particle.standard.screen;

import net.minecraft.client.multiplayer.ClientLevel;
import team.lodestar.lodestone.modules.rendering.particle.standard.screen.base.ScreenParticle;

public class ScreenParticleType<T extends ScreenParticleConfig> {

    public ParticleProvider<T> provider;

    public ScreenParticleType() {
    }

    public interface ParticleProvider<T extends ScreenParticleConfig> {
        ScreenParticle createParticle(ClientLevel pLevel, T options, double x, double y, double pXSpeed, double pYSpeed);
    }
}