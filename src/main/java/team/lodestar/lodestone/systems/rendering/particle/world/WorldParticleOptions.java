package team.lodestar.lodestone.systems.rendering.particle.world;

import com.mojang.brigadier.StringReader;
import com.mojang.math.Vector3f;
import com.mojang.serialization.Codec;
import net.minecraft.client.particle.ParticleRenderType;
import team.lodestar.lodestone.systems.rendering.particle.LodestoneWorldParticleRenderType;
import team.lodestar.lodestone.systems.rendering.particle.SimpleParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;

public class WorldParticleOptions extends SimpleParticleOptions implements net.minecraft.core.particles.ParticleOptions {

    public static Codec<WorldParticleOptions> codecFor(ParticleType<?> type) {
        return Codec.unit(() -> new WorldParticleOptions(type));
    }

    ParticleType<?> type;
    public Vector3f startingMotion = Vector3f.ZERO, endingMotion = Vector3f.ZERO;
    public ParticleRenderType renderType = LodestoneWorldParticleRenderType.ADDITIVE;

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