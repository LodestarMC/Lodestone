package team.lodestar.lodestone.systems.network.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import team.lodestar.lodestone.systems.particle.data.color.*;
import team.lodestar.lodestone.systems.particle.data.color.*;

import java.util.Collections;
import java.util.List;

public class NetworkedParticleEffectColorData {

    public static final Codec<NetworkedParticleEffectColorData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ColorParticleData.CODEC.listOf().fieldOf("colors").forGetter(data -> data.colors)
    ).apply(instance, NetworkedParticleEffectColorData::new));

    public static final StreamCodec<ByteBuf, NetworkedParticleEffectColorData> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

    protected final List<ColorParticleData> colors;
    protected int colorCycleCounter;

    public static NetworkedParticleEffectColorData fromColors(List<? extends ColorParticleDataWrapper> colors) {
        return new NetworkedParticleEffectColorData(colors);
    }

    public static NetworkedParticleEffectColorData fromColor(ColorParticleDataWrapper color) {
        return fromColors(List.of(color));
    }

    public NetworkedParticleEffectColorData(List<? extends ColorParticleDataWrapper> colors) {
        this.colors = colors.isEmpty() ? Collections.emptyList() : colors.stream().map(ColorParticleDataWrapper::unwrap).toList();
    }

    public NetworkedParticleEffectColorData(ColorParticleDataWrapper... colors) {
        this(List.of(colors));
    }

    public ColorParticleData getColor() {
        if (colors.size() == 1) {
            return colors.getFirst();
        }
        return colors.get(colorCycleCounter++ % colors.size());
    }

    public List<ColorParticleData> getColors() {
        return colors;
    }
}