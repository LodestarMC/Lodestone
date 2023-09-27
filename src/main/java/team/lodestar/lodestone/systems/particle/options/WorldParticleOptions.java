package team.lodestar.lodestone.systems.particle.options;

import com.mojang.brigadier.StringReader;
import com.mojang.serialization.Codec;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.world.phys.*;
import team.lodestar.lodestone.systems.particle.*;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import team.lodestar.lodestone.systems.particle.world.*;

import java.util.function.Consumer;

public class WorldParticleOptions extends AbstractWorldParticleOptions<WorldParticleOptions> {

    public static Codec<WorldParticleOptions> worldCodec(ParticleType<?> type) {
        return Codec.unit(() -> new WorldParticleOptions(type));
    }

    public WorldParticleOptions(ParticleType<?> type) {
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

    public static final Deserializer<WorldParticleOptions> DESERIALIZER = new Deserializer<>() {
        @Override
        public WorldParticleOptions fromCommand(ParticleType<WorldParticleOptions> type, StringReader reader) {
            return new WorldParticleOptions(type);
        }

        @Override
        public WorldParticleOptions fromNetwork(ParticleType<WorldParticleOptions> type, FriendlyByteBuf buf) {
            return new WorldParticleOptions(type);
        }
    };
}