package team.lodestar.lodestone.systems.particle.world.type;

import com.mojang.serialization.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import team.lodestar.lodestone.systems.particle.world.options.*;
import team.lodestar.lodestone.systems.particle.world.options.*;

public abstract class AbstractLodestoneParticleType<T extends WorldParticleOptions> extends ParticleType<T> {

    protected AbstractLodestoneParticleType() {
        super(false);
    }

    public AbstractLodestoneParticleType<T> getType() {
        return this;
    }

    @Override
    public MapCodec<T> codec() {
        throw new UnsupportedOperationException();
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
        throw new UnsupportedOperationException();
    }
}