package team.lodestar.lodestone.systems.particle.options;

import com.mojang.brigadier.*;
import com.mojang.serialization.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.*;
import net.minecraft.world.item.*;

public class LodestoneItemCrumbsParticleOptions extends AbstractWorldParticleOptions {

    public static Codec<LodestoneItemCrumbsParticleOptions> brokenItemCodec(ParticleType<?> type) {
        return Codec.unit(() -> new LodestoneItemCrumbsParticleOptions(type));
    }

    public final ItemStack stack;

    public LodestoneItemCrumbsParticleOptions(ParticleType<?> type, ItemStack stack) {
        super(type);
        this.stack = stack;
    }

    public LodestoneItemCrumbsParticleOptions(ParticleType<?> type) {
        this(type, null);
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

    public static final Deserializer<LodestoneItemCrumbsParticleOptions> DESERIALIZER = new Deserializer<>() {
        @Override
        public LodestoneItemCrumbsParticleOptions fromCommand(ParticleType<LodestoneItemCrumbsParticleOptions> type, StringReader reader) {
            return new LodestoneItemCrumbsParticleOptions(type);
        }

        @Override
        public LodestoneItemCrumbsParticleOptions fromNetwork(ParticleType<LodestoneItemCrumbsParticleOptions> type, FriendlyByteBuf buf) {
            return new LodestoneItemCrumbsParticleOptions(type);
        }
    };
}