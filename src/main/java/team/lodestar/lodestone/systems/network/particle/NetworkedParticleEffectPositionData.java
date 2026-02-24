package team.lodestar.lodestone.systems.network.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class NetworkedParticleEffectPositionData {

    public static final Codec<NetworkedParticleEffectPositionData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.DOUBLE.fieldOf("posX").forGetter(data -> data.posX),
            Codec.DOUBLE.fieldOf("posY").forGetter(data -> data.posY),
            Codec.DOUBLE.fieldOf("posZ").forGetter(data -> data.posZ)
    ).apply(instance, NetworkedParticleEffectPositionData::new));

    public static final StreamCodec<ByteBuf, NetworkedParticleEffectPositionData> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

    protected final double posX;
    protected final double posY;
    protected final double posZ;

    public NetworkedParticleEffectPositionData(BlockPos pos) {
        this(pos.getX(), pos.getY(), pos.getZ());
    }

    public NetworkedParticleEffectPositionData(Entity entity) {
        this(entity.getX(), entity.getY() + entity.getBbHeight() / 2f, entity.getZ());
    }

    public NetworkedParticleEffectPositionData(Vec3 pos) {
        this(pos.x, pos.y, pos.z);
    }

    public NetworkedParticleEffectPositionData(double posX, double posY, double posZ) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
    }

    public BlockPos getAsBlockPos() {
        return new BlockPos((int) posX, (int) posY, (int) posZ);
    }

    public Vec3 getAsVector() {
        return new Vec3(posX, posY, posZ);
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }

    public double getPosZ() {
        return posZ;
    }
}