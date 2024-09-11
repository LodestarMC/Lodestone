package team.lodestar.lodestone.systems.particle.world.type;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.world.level.block.*;
import team.lodestar.lodestone.systems.particle.world.LodestoneWorldParticle;
import team.lodestar.lodestone.systems.particle.world.options.*;

import javax.annotation.Nullable;

public class LodestoneWorldParticleType extends AbstractLodestoneParticleType<WorldParticleOptions> {

    //Lodestone particles do not support commands or networking as is, and as such, we do not need to implement a proper codec for em
    public final MapCodec<WorldParticleOptions> CODEC = RecordCodecBuilder.mapCodec(obj -> obj.stable(new WorldParticleOptions(this)));
    public final StreamCodec<RegistryFriendlyByteBuf, WorldParticleOptions> STREAM_CODEC = StreamCodec.unit(new WorldParticleOptions(this));


    public LodestoneWorldParticleType() {
        super();
    }

    @Override
    public MapCodec<WorldParticleOptions> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, WorldParticleOptions> streamCodec() {
        return STREAM_CODEC;
    }

    public static class Factory implements ParticleProvider<WorldParticleOptions> {
        private final SpriteSet sprite;

        public Factory(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Nullable
        @Override
        public Particle createParticle(WorldParticleOptions data, ClientLevel world, double x, double y, double z, double mx, double my, double mz) {
            return new LodestoneWorldParticle(world, data, (ParticleEngine.MutableSpriteSet) sprite, x, y, z, mx, my, mz);
        }
    }
}