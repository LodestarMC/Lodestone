package team.lodestar.lodestone.systems.particle.world;

import com.mojang.brigadier.StringReader;
import com.mojang.serialization.Codec;
import net.minecraft.client.particle.ParticleRenderType;
import team.lodestar.lodestone.systems.particle.SimpleParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import team.lodestar.lodestone.systems.particle.screen.GenericScreenParticle;

import java.util.function.Consumer;

public class WorldParticleOptions extends SimpleParticleOptions implements net.minecraft.core.particles.ParticleOptions {

    public static Codec<WorldParticleOptions> codecFor(ParticleType<?> type) {
        return Codec.unit(() -> new WorldParticleOptions(type));
    }

    public final ParticleType<?> type;
    public ParticleRenderType renderType;
    public Consumer<GenericParticle> actor;

    public boolean noClip = false;

    public WorldParticleOptions(ParticleType<?> type) {
        this.type = type;
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