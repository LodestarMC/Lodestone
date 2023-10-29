package team.lodestar.lodestone.systems.particle.options;

import com.mojang.brigadier.StringReader;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.state.BlockState;

public class LodestoneTerrainParticleOptions extends AbstractWorldParticleOptions {

    public static Codec<LodestoneTerrainParticleOptions> terrainCodec(ParticleType<?> type) {
        return Codec.unit(() -> new LodestoneTerrainParticleOptions(type));
    }

    public final BlockState blockState;
    public final BlockPos blockPos;

    public LodestoneTerrainParticleOptions(ParticleType<?> type, BlockState blockState, BlockPos blockPos) {
        super(type);
        this.blockState = blockState;
        this.blockPos = blockPos;
    }

    public LodestoneTerrainParticleOptions(ParticleType<?> type, BlockState blockState) {
        this(type, blockState, null);
    }

    public LodestoneTerrainParticleOptions(ParticleType<?> type) {
        this(type, null, null);
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