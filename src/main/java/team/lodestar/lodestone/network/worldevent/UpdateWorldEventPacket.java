package team.lodestar.lodestone.network.worldevent;

import me.pepperbell.simplenetworking.SimpleChannel;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import team.lodestar.lodestone.component.LodestoneComponents;
import team.lodestar.lodestone.systems.network.LodestoneClientPacket;
import team.lodestar.lodestone.systems.worldevent.WorldEventInstance;

import java.util.UUID;
import java.util.function.Supplier;

public class UpdateWorldEventPacket extends LodestoneClientPacket {
    private UUID uuid;
    private CompoundTag eventData;

    public UpdateWorldEventPacket(UUID uuid, CompoundTag eventData) {
        this.uuid = uuid;
        this.eventData = eventData;
    }

    public UpdateWorldEventPacket(FriendlyByteBuf buf) {
        uuid = buf.readUUID();
        eventData = buf.readNbt();
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeUUID(uuid);
        buf.writeNbt(eventData);
    }

    @Override
    public void executeClient(Minecraft client, ClientPacketListener listener, PacketSender responseSender, SimpleChannel channel) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null) {
            LodestoneComponents.LODESTONE_WORLD_COMPONENT.maybeGet(level).ifPresent(c -> {
                for (WorldEventInstance instance : c.activeWorldEvents) {
                    if (instance.uuid.equals(uuid)) {
                        instance.deserializeNBT(eventData);
                        break;
                    }
                }
            });
        }
    }
}