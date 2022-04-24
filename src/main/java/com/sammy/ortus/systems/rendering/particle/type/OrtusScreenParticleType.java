package com.sammy.ortus.systems.rendering.particle.type;

import com.sammy.ortus.systems.rendering.particle.screen.GenericScreenParticle;
import com.sammy.ortus.systems.rendering.particle.screen.ScreenParticleOptions;
import com.sammy.ortus.systems.rendering.particle.screen.ScreenParticleType;
import com.sammy.ortus.systems.rendering.particle.screen.base.ScreenParticle;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.SpriteSet;

public class OrtusScreenParticleType extends ScreenParticleType<ScreenParticleOptions> {

    public OrtusScreenParticleType() {
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