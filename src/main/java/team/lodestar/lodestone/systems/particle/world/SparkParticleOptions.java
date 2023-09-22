package team.lodestar.lodestone.systems.particle.world;

import com.mojang.brigadier.StringReader;
import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;

public class SparkParticleOptions extends WorldParticleOptions {

    public static Codec<SparkParticleOptions> sparkCodec(ParticleType<?> type) {
        return Codec.unit(() -> new SparkParticleOptions(type));
    }

    public GenericParticleData lengthData = DEFAULT_GENERIC;

    public SparkParticleOptions(ParticleType<?> type) {
        super(type);
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
