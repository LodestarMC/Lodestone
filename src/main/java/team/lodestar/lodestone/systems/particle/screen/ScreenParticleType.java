package team.lodestar.lodestone.systems.particle.screen;

import net.minecraft.client.multiplayer.ClientLevel;
import team.lodestar.lodestone.systems.particle.screen.base.ScreenParticle;

public class ScreenParticleType {

    public ScreenParticleProvider provider;

    public ScreenParticleType() {
    }

    public interface ScreenParticleProvider {
        ScreenParticle createParticle(ClientLevel pLevel, ScreenParticleOptions options, double x, double y, double pXSpeed, double pYSpeed);
    }
}