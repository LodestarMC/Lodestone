package team.lodestar.lodestone.modules.rendering.particle.standard.screen;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.SpriteSet;
import team.lodestar.lodestone.modules.rendering.particle.standard.screen.base.ScreenParticle;

public class LodestoneScreenParticleType extends ScreenParticleType<ScreenParticleConfig> {

    public LodestoneScreenParticleType() {
        super();
    }

    public record Factory(SpriteSet sprite) implements ParticleProvider<ScreenParticleConfig> {

        @Override
        public ScreenParticle createParticle(ClientLevel pLevel, ScreenParticleConfig options, double x, double y, double pXSpeed, double pYSpeed) {
            return new LodestoneScreenParticle(pLevel, options, (ParticleEngine.MutableSpriteSet) sprite, x, y, pXSpeed, pYSpeed);
        }
    }
}