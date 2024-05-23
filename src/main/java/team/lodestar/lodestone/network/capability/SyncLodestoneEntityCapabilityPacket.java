package team.lodestar.lodestone.network.capability;

import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import team.lodestar.lodestone.capability.LodestoneEntityDataCapability;
import team.lodestar.lodestone.systems.network.LodestoneTwoWayNBTPacket;

import java.util.function.Supplier;

@SuppressWarnings("ConstantConditions")
public class SyncLodestoneEntityCapabilityPacket extends LodestoneTwoWayNBTPacket {

    public static final String ENTITY_ID = "entity_id";

    private final int entityID;

    public SyncLodestoneEntityCapabilityPacket(CompoundTag tag) {
        super(tag);
        this.entityID = tag.getInt(ENTITY_ID);
    }

    public SyncLodestoneEntityCapabilityPacket(int id, CompoundTag tag) {
        super(handleTag(id, tag));
        this.entityID = id;
    }

    public static CompoundTag handleTag(int id, CompoundTag tag) {
        tag.putInt(ENTITY_ID, id);
        return tag;
    }

    @Override
    public void serverExecute(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl listener, PacketSender responseSender, SimpleChannel channel, CompoundTag data) {
        Entity entity = context.get().getSender().level().getEntity(entityID);
        LodestoneEntityDataCapability.getCapabilityOptional(entity).ifPresent(c -> c.deserializeNBT(data));
    }

    @Override
    public void clientExecute(Minecraft client, ClientPacketListener listener, PacketSender responseSender, SimpleChannel channel, CompoundTag data) {
        Entity entity = Minecraft.getInstance().level.getEntity(entityID);
        LodestoneEntityDataCapability.getCapabilityOptional(entity).ifPresent(c -> c.deserializeNBT(data));
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, SyncLodestoneEntityCapabilityPacket.class, SyncLodestoneEntityCapabilityPacket::encode, SyncLodestoneEntityCapabilityPacket::decode, SyncLodestoneEntityCapabilityPacket::handle);
    }

    public static SyncLodestoneEntityCapabilityPacket decode(FriendlyByteBuf buf) {
        return new SyncLodestoneEntityCapabilityPacket(buf.readNbt());
    }
}