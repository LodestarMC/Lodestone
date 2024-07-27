package team.lodestar.lodestone.networkold.capability;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import team.lodestar.lodestone.capability.LodestonePlayerDataCapability;
import team.lodestar.lodestone.systems.network.LodestoneTwoWayNBTPacket;

import java.util.UUID;
import java.util.function.Supplier;

@SuppressWarnings("ConstantConditions")
public class SyncLodestonePlayerCapabilityPacket extends LodestoneTwoWayNBTPacket {

    public static final String PLAYER_UUID = "player_uuid";

    private final UUID uuid;

    public SyncLodestonePlayerCapabilityPacket(CompoundTag tag) {
        super(tag);
        this.uuid = tag.getUUID(PLAYER_UUID);
    }

    public SyncLodestonePlayerCapabilityPacket(UUID uuid, CompoundTag tag) {
        super(handleTag(uuid, tag));
        this.uuid = uuid;
    }

    public static CompoundTag handleTag(UUID id, CompoundTag tag) {
        tag.putUUID(PLAYER_UUID, id);
        return tag;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void clientExecute(Supplier<NetworkEvent.Context> context, CompoundTag data) {
        Player player = Minecraft.getInstance().level.getPlayerByUUID(uuid);
        LodestonePlayerDataCapability.getCapabilityOptional(player).ifPresent(c -> c.deserializeNBT(data));
    }

    @Override
    public void serverExecute(Supplier<NetworkEvent.Context> context, CompoundTag data) {
        Player player = context.get().getSender().level().getPlayerByUUID(uuid);
        LodestonePlayerDataCapability.getCapabilityOptional(player).ifPresent(c -> c.deserializeNBT(data));
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, SyncLodestonePlayerCapabilityPacket.class, SyncLodestonePlayerCapabilityPacket::encode, SyncLodestonePlayerCapabilityPacket::decode, SyncLodestonePlayerCapabilityPacket::handle);
    }

    public static SyncLodestonePlayerCapabilityPacket decode(FriendlyByteBuf buf) {
        return new SyncLodestonePlayerCapabilityPacket(buf.readNbt());
    }
}