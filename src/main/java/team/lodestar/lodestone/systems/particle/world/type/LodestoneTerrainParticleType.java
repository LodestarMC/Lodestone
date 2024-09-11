package team.lodestar.lodestone.systems.particle.world.type;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import team.lodestar.lodestone.systems.particle.world.options.*;
import team.lodestar.lodestone.systems.particle.world.LodestoneTerrainParticle;
import team.lodestar.lodestone.systems.particle.world.behaviors.*;

import javax.annotation.Nullable;

public class LodestoneTerrainParticleType extends AbstractLodestoneParticleType<LodestoneTerrainParticleOptions> {

    //Lodestone particles do not support commands or networking as is, and as such, we do not need to implement a proper codec for em
    public final MapCodec<LodestoneTerrainParticleOptions> CODEC = RecordCodecBuilder.mapCodec(obj -> obj.stable(new LodestoneTerrainParticleOptions(this, Blocks.AIR.defaultBlockState())));
    public final StreamCodec<RegistryFriendlyByteBuf, LodestoneTerrainParticleOptions> STREAM_CODEC = StreamCodec.unit(new LodestoneTerrainParticleOptions(this, Blocks.AIR.defaultBlockState()));


    public LodestoneTerrainParticleType() {
        super();
    }

    @Override
    public MapCodec<LodestoneTerrainParticleOptions> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, LodestoneTerrainParticleOptions> streamCodec() {
        return STREAM_CODEC;
    }

    public static class Factory implements ParticleProvider<LodestoneTerrainParticleOptions> {

        public Factory() {
        }

        @Nullable
        @Override
        public Particle createParticle(LodestoneTerrainParticleOptions data, ClientLevel world, double x, double y, double z, double mx, double my, double mz) {
            return new LodestoneTerrainParticle(world, data, x, y, z, mx, my, mz);
        }
    }
}