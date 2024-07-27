package team.lodestar.lodestone.systems.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.registry.common.LodestonePayloadRegistry;

import java.util.function.Function;

public abstract class LodestoneNetworkPayloadData implements CustomPacketPayload {

    public final CustomPacketPayload.Type<? extends LodestoneNetworkPayloadData> dataType;

    public LodestoneNetworkPayloadData(ResourceLocation type) {
        this.dataType = LodestonePayloadRegistry.CHANNEL_MAP.get(type.getNamespace()).getPayloadType(type.getPath());
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return dataType;
    }

    public final String name() {
        return type().id().getPath();
    }

    public abstract void deserialize(CompoundTag tag);

    public final CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        serialize(tag);
        return tag;
    }
    public abstract void serialize(CompoundTag tag);
|