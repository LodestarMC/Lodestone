package team.lodestar.lodestone.systems.particle.options;

import com.mojang.brigadier.*;
import com.mojang.serialization.*;
import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.*;
import net.minecraft.world.level.block.state.*;

public class LodestoneTerrainParticleOptions extends AbstractWorldParticleOptions {

    public static Codec<LodestoneTerrainParticleOptions> terrainCodec(ParticleType<?> type) {
        return Codec.unit(() -> new LodestoneTerrainParticleOptions(type));
    }

    public BlockState blockState;
    public BlockPos blockPos;

    public LodestoneTerrainParticleOptions(ParticleType<?> type) {
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

    public static final Deserializer<LodestoneTerrainParticleOptions> DESERIALIZER = new Deserializer<>() {
        @Override
        public LodestoneTerrainParticleOptions fromCommand(ParticleType<LodestoneTerrainParticleOptions> type, StringReader reader) {
            return new LodestoneTerrainParticleOptions(type);
        }

        @Override
        public LodestoneTerrainParticleOptions fromNetwork(ParticleType<LodestoneTerrainParticleOptions> type, FriendlyByteBuf buf) {
            return new LodestoneTerrainParticleOptions(type);
        }
    };
}