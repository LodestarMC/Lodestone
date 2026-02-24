package team.lodestar.lodestone.systems.network.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import team.lodestar.lodestone.systems.particle.data.color.*;
import team.lodestar.lodestone.systems.particle.data.color.*;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class NetworkedParticleEffectType<T extends NetworkedParticleEffectExtraData> {

    public static final Map<String, NetworkedParticleEffectType<?>> EFFECT_TYPES = new LinkedHashMap<>();

    public static final Codec<NetworkedParticleEffectType<?>> CODEC = Codec.STRING.comapFlatMap(s ->
                    DataResult.success(EFFECT_TYPES.get(s)),
            NetworkedParticleEffectType::getId);

    protected final String id;

    public NetworkedParticleEffectType(String id) {
        this.id = id;
        EFFECT_TYPES.put(id, this);
    }

    public String getId() {
        return id;
    }

    public Optional<StreamCodec<ByteBuf, ? extends NetworkedParticleEffectPositionData>> getPositionCodec() {
        return Optional.of(NetworkedParticleEffectPositionData.STREAM_CODEC);
    }

    public Optional<StreamCodec<ByteBuf, ? extends NetworkedParticleEffectColorData>> getColorCodec() {
        return Optional.of(NetworkedParticleEffectColorData.STREAM_CODEC);
    }

    public Optional<StreamCodec<ByteBuf, ? extends NetworkedParticleEffectExtraData>> getExtraCodec() {
        return Optional.empty();
    }

    public Optional<? extends NetworkedParticleEffectExtraData> getDefaultExtraData() {
        return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    protected void castAndAct(Level level, RandomSource random, NetworkedParticleEffectPositionData positionData, NetworkedParticleEffectColorData colorData, NetworkedParticleEffectExtraData extraData) {
        act(level, random, positionData, colorData, (T) extraData);
    }

    public abstract void act(Level level, RandomSource random, NetworkedParticleEffectPositionData positionData, NetworkedParticleEffectColorData colorData, T extraData);

    public ParticleEffectBuilder<T> createEffect(BlockPos position) {
        return createEffect().at(position);
    }

    public ParticleEffectBuilder<T> createEffect(Vec3 position) {
        return createEffect().at(position);
    }

    public ParticleEffectBuilder<T> createEffect(Entity target) {
        return createEffect().at(target);
    }

    public ParticleEffectBuilder<T> createEffect() {
        return new ParticleEffectBuilder<>(this);
    }

    @SuppressWarnings("UnusedReturnValue")
    public static class ParticleEffectBuilder<T extends NetworkedParticleEffectExtraData> {

        protected final NetworkedParticleEffectType<T> type;
        protected NetworkedParticleEffectPositionData position;
        protected NetworkedParticleEffectColorData color;
        protected T extra;

        public ParticleEffectBuilder(NetworkedParticleEffectType<T> type) {
            this.type = type;
        }

        public ParticleEffectBuilder<T> at(BlockPos position) {
            return at(new NetworkedParticleEffectPositionData(position));
        }

        public ParticleEffectBuilder<T> at(Vec3 position) {
            return at(new NetworkedParticleEffectPositionData(position));
        }

        public ParticleEffectBuilder<T> at(Entity target) {
            return at(new NetworkedParticleEffectPositionData(target));
        }

        public ParticleEffectBuilder<T> at(NetworkedParticleEffectPositionData position) {
            this.position = position;
            return this;
        }

        public ParticleEffectBuilder<T> color(Color color) {
            return color(ColorParticleData.create(color).build());
        }

        public ParticleEffectBuilder<T> color(ColorParticleDataWrapper color) {
            return color(NetworkedParticleEffectColorData.fromColor(color));
        }

        public ParticleEffectBuilder<T> color(List<? extends ColorParticleDataWrapper> colors) {
            return color(NetworkedParticleEffectColorData.fromColors(colors));
        }

        public ParticleEffectBuilder<T> color(NetworkedParticleEffectColorData color) {
            this.color = color;
            return this;
        }

        public ParticleEffectBuilder<T> customData(T extra) {
            this.extra = extra;
            return this;
        }

        @SuppressWarnings("unchecked")
        protected T getCustomData() {
            if (type.getExtraCodec().isEmpty()) {
                return null;
            }
            if (extra == null) {
                var defaultExtra = type.getDefaultExtraData();
                if (defaultExtra.isEmpty()) {
                    throw new IllegalArgumentException("Effect type that demands custom data did not receive any and has no default provided");
                }
                customData((T) defaultExtra.get());
            }
            return extra;
        }

        public ParticleEffectBuilder<T> spawn(ServerLevel level) {
            if (position == null) {
                return spawn(PacketDistributor::sendToAllPlayers);
            }
            return spawn(p -> PacketDistributor.sendToPlayersTrackingChunk(level, new ChunkPos(position.getAsBlockPos()), p));
        }

        public ParticleEffectBuilder<T> spawn(Consumer<NetworkedParticleEffectPayload> sender) {
            sender.accept(new NetworkedParticleEffectPayload(type, position, color, getCustomData()));
            return this;
        }
    }
}