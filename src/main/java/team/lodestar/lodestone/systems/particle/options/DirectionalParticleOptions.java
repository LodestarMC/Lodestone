package team.lodestar.lodestone.systems.particle.options;

import com.mojang.brigadier.StringReader;
import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public class DirectionalParticleOptions extends AbstractWorldParticleOptions {

    public static Codec<DirectionalParticleOptions> directionalCodec(ParticleType<?> type) {
        return Codec.unit(() -> new DirectionalParticleOptions(type));
    }

    public Vec3 direction = new Vec3(0, 1, 0);

    public DirectionalParticleOptions(ParticleType<?> type) {
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

    public static final Deserializer<DirectionalParticleOptions> DESERIALIZER = new Deserializer<>() {
        @Override
        public DirectionalParticleOptions fromCommand(ParticleType<DirectionalParticleOptions> type, StringReader reader) {
            return new DirectionalParticleOptions(type);
        }

        @Override
        public DirectionalParticleOptions fromNetwork(ParticleType<DirectionalParticleOptions> type, FriendlyByteBuf buf) {
            return new DirectionalParticleOptions(type);
        }
    };
}