package team.lodestar.lodestone.systems.particle.options;

import com.mojang.brigadier.*;
import com.mojang.serialization.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.*;
import team.lodestar.lodestone.systems.particle.data.*;

public class SparkParticleOptions extends AbstractWorldParticleOptions {

    public static Codec<SparkParticleOptions> sparkCodec(ParticleType<?> type) {
        return Codec.unit(() -> new SparkParticleOptions(type));
    }

    public GenericParticleData lengthData = DEFAULT_GENERIC;

    public SparkParticleOptions(ParticleType<?> type) {
        super(type);
    }

    @Override
    public ParticleType<?> getType() {
        return type;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
    }

    @Override
    public String writeToString() {
        return "";
    }

    public static final Deserializer<SparkParticleOptions> DESERIALIZER = new Deserializer<>() {
        @Override
        public SparkParticleOptions fromCommand(ParticleType<SparkParticleOptions> type, StringReader reader) {
            return new SparkParticleOptions(type);
        }

        @Override
        public SparkParticleOptions fromNetwork(ParticleType<SparkParticleOptions> type, FriendlyByteBuf buf) {
            return new SparkParticleOptions(type);
        }
    };
}