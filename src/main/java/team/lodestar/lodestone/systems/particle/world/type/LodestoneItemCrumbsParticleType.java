package team.lodestar.lodestone.systems.particle.world.type;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.Nullable;
import team.lodestar.lodestone.systems.particle.world.options.*;
import team.lodestar.lodestone.systems.particle.world.LodestoneItemCrumbParticle;

public class LodestoneItemCrumbsParticleType extends AbstractLodestoneParticleType<LodestoneItemCrumbsParticleOptions> {

    //Lodestone particles do not support commands or networking as is, and as such, we do not need to implement a proper codec for em
    public final MapCodec<LodestoneItemCrumbsParticleOptions> CODEC = RecordCodecBuilder.mapCodec(obj -> obj.stable(new LodestoneItemCrumbsParticleOptions(this, ItemStack.EMPTY)));
    public final StreamCodec<RegistryFriendlyByteBuf, LodestoneItemCrumbsParticleOptions> STREAM_CODEC = StreamCodec.unit(new LodestoneItemCrumbsParticleOptions(this, ItemStack.EMPTY));


    public LodestoneItemCrumbsParticleType() {
        super();
    }

    @Override
    public MapCodec<LodestoneItemCrumbsParticleOptions> codec() {
        return CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, LodestoneItemCrumbsParticleOptions> streamCodec() {
        return STREAM_CODEC;
    }

    public static class Factory implements ParticleProvider<LodestoneItemCrumbsParticleOptions> {

        public Factory() {
        }

        @Nullable
        @Override
        public Particle createParticle(LodestoneItemCrumbsParticleOptions data, ClientLevel world, double x, double y, double z, double mx, double my, double mz) {
            return new LodestoneItemCrumbParticle(world, data, x, y, z, mx, my, mz);
        }
    }
}