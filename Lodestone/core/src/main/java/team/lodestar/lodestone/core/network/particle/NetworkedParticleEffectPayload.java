package team.lodestar.lodestone.core.network.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import team.lodestar.lodestone.core.network.OneSidedPayloadData;

import javax.annotation.Nullable;

public class NetworkedParticleEffectPayload extends OneSidedPayloadData {

    private final NetworkedParticleEffectType<?> effect;
    @Nullable
    private final NetworkedParticleEffectPositionData positionData;
    @Nullable
    private final NetworkedParticleEffectColorData colorData;
    @Nullable
    private final NetworkedParticleEffectExtraData extraData;

    public NetworkedParticleEffectPayload(NetworkedParticleEffectType<?> effect, @Nullable NetworkedParticleEffectPositionData positionData, @Nullable NetworkedParticleEffectColorData colorData, @Nullable NetworkedParticleEffectExtraData extraData) {
        this.effect = effect;
        this.positionData = positionData;
        this.colorData = colorData;
        this.extraData = extraData;
    }

    public NetworkedParticleEffectPayload(RegistryFriendlyByteBuf buf) {
        this.effect = getEffectType(buf.readUtf());
        this.positionData = effect.getPositionCodec().isPresent() ? effect.getPositionCodec().get().decode(buf) : null;
        this.colorData = effect.getColorCodec().isPresent() ? effect.getColorCodec().get().decode(buf) : null;
        this.extraData = effect.getExtraCodec().isPresent() ? effect.getExtraCodec().get().decode(buf) : null;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void serialize(RegistryFriendlyByteBuf buf) {
        buf.writeUtf(effect.id);
        if (effect.getPositionCodec().isPresent()) {
            if (positionData == null) {
                throw new IllegalArgumentException("Networked Particle Effect expected position data, did not receive any.");
            }
            StreamCodec codec = effect.getPositionCodec().get();
            codec.encode(buf, positionData);
        }
        if (effect.getColorCodec().isPresent()) {
            if (colorData == null) {
                throw new IllegalArgumentException("Networked Particle Effect expected color data, did not receive any.");
            }
            StreamCodec codec = effect.getColorCodec().get();
            codec.encode(buf, colorData);
        }
        if (effect.getExtraCodec().isPresent()) {
            if (extraData == null) {
                throw new IllegalArgumentException("Networked Particle Effect expected extra data, did not receive any.");
            }
            StreamCodec codec = effect.getExtraCodec().get();
            codec.encode(buf, extraData);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle(IPayloadContext iPayloadContext) {
        Minecraft instance = Minecraft.getInstance();
        ClientLevel level = instance.level;
        effect.castAndAct(level, level.random, positionData, colorData, extraData);
    }

    public NetworkedParticleEffectType<?> getEffectType(String id) {
        NetworkedParticleEffectType<?> particleEffectType = NetworkedParticleEffectType.EFFECT_TYPES.get(id);
        if (particleEffectType == null) {
            throw new NullPointerException("This shouldn't be happening. The particle effect type with id: \"" + id + "\" does not exist. This is most likely a networking error.");
        }
        return particleEffectType;
    }
}