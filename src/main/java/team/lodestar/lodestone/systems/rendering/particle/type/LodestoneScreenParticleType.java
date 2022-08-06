package team.lodestar.lodestone.systems.rendering.particle.type;

import team.lodestar.lodestone.systems.rendering.particle.screen.GenericScreenParticle;
import team.lodestar.lodestone.systems.rendering.particle.screen.ScreenParticleOptions;
import team.lodestar.lodestone.systems.rendering.particle.screen.ScreenParticleType;
import team.lodestar.lodestone.systems.rendering.particle.screen.base.ScreenParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.SpriteSet;

public class LodestoneScreenParticleType extends ScreenParticleType<ScreenParticleOptions> {

    public LodestoneScreenParticleType() {
        super();
    }

    public static class Factory implements ParticleProvider<ScreenParticleOptions> {
        public final SpriteSet sprite;

        public Factory(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Override
        public ScreenParticle createParticle(ClientLevel pLevel, ScreenParticleOptions options, double pX, double pY, double pXSpeed, double pYSpeed) {
            return new GenericScreenParticle(pLevel, options, (ParticleEngine.MutableSpriteSet) sprite, pX, pY, pXSpeed, pYSpeed);
        }
    }
}