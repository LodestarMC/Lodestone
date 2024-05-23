package team.lodestar.lodestone.network.capability;

import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.player.Player;
import team.lodestar.lodestone.component.LodestonePlayerDataCapability;
import team.lodestar.lodestone.systems.network.LodestoneTwoWayNBTPacket;

import java.util.UUID;

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

    @Override
    public void serverExecute(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl listener, PacketSender responseSender, SimpleChannel channel, CompoundTag data) {
        Player player = context.get().getSender().level().getPlayerByUUID(uuid);
        LodestonePlayerDataCapability.getCapabilityOptional(player).ifPresent(c -> c.deserializeNBT(data));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void clientExecute(Minecraft client, ClientPacketListener listener, PacketSender responseSender, SimpleChannel channel, CompoundTag data) {
        Player player = Minecraft.getInstance().level.getPlayerByUUID(uuid);
        LodestonePlayerDataCapability.getCapabilityOptional(player).ifPresent(c -> c.deserializeNBT(data));
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, SyncLodestonePlayerCapabilityPacket.class, SyncLodestonePlayerCapabilityPacket::encode, SyncLodestonePlayerCapabilityPacket::decode, SyncLodestonePlayerCapabilityPacket::handle);
    }

    public static SyncLodestonePlayerCapabilityPacket decode(FriendlyByteBuf buf) {
        return new SyncLodestonePlayerCapabilityPacket(buf.readNbt());
    }
}