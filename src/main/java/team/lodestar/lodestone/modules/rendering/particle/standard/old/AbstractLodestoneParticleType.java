//package team.lodestar.lodestone.modules.rendering.particle.standard.old;
//
//import com.mojang.serialization.*;
//import net.minecraft.core.particles.*;
//import net.minecraft.network.*;
//import net.minecraft.network.codec.*;
//import team.lodestar.lodestone.modules.rendering.particle.standard.world.options.*;
//
//public abstract class AbstractLodestoneParticleType<T extends WorldParticleConfig> extends ParticleType<T> {
//
//    protected AbstractLodestoneParticleType() {
//        super(false);
//    }
//
//    public AbstractLodestoneParticleType<T> getType() {
//        return this;
//    }
//
//    @Override
//    public MapCodec<T> codec() {
//        throw new UnsupportedOperationException();
//    }
//
//    @Override
//    public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
//        throw new UnsupportedOperationException();
//    }
//}