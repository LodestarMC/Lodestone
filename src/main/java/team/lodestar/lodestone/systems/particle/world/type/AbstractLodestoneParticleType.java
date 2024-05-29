package team.lodestar.lodestone.systems.particle.world.type;

import com.mojang.brigadier.*;
import com.mojang.serialization.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.*;
import team.lodestar.lodestone.systems.particle.world.*;
import team.lodestar.lodestone.systems.particle.world.behaviors.*;
import team.lodestar.lodestone.systems.particle.world.options.*;

import javax.annotation.*;

public class AbstractLodestoneParticleType<T extends WorldParticleOptions<?>> extends ParticleType<T> {

    public AbstractLodestoneParticleType() {
        super(false, new ParticleOptions.Deserializer<>() {
            //TODO: this hellspawn abomination should be nuked from orbit.
            @Override
            public T fromCommand(ParticleType<T> type, StringReader reader) {
                return (T) new WorldParticleOptions(() -> type, null);
            }

            @Override
            public T fromNetwork(ParticleType<T> type, FriendlyByteBuf buf) {
                return (T) new WorldParticleOptions(() -> type, null);
            }
        });
    }

    @Override
    public Codec<T> codec() {
        return genericCodec(this);
    }

    //TODO: this hellspawn abomination also needs to be nuked.
    // Ideally, lodestone particles would be completely ignored when it comes to networking/commands
    public static <K extends WorldParticleOptions<?>> Codec<K> genericCodec(ParticleType<?> type) {
        return Codec.unit(() -> (K) new WorldParticleOptions<>(() -> type, null));
    }
}