package team.lodestar.lodestone.network.worldevent;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

import team.lodestar.lodestone.systems.network.OneSidedPayloadData;
import team.lodestar.lodestone.systems.worldevent.WorldEventInstance;

import java.util.UUID;

public class UpdateWorldEventPayload extends OneSidedPayloadData {

    private final UUID uuid;
    private final CompoundTag eventData;

    public UpdateWorldEventPayload(FriendlyByteBuf byteBuf) {
        this(byteBuf.readUUID(), byteBuf.readNbt());
    }

    public UpdateWorldEventPayload(WorldEventInstance instance) {
        this(instance.uuid, instance.serializeNBT());
    }

    public UpdateWorldEventPayload(UUID uuid, CompoundTag eventData) {
        this.uuid = uuid;
        this.eventData = eventData;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void handle(IPayloadContext context) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null) {
            var worldData = level.getData(LodestoneAttachmentTypes.WORLD_EVENT_DATA);
            for (WorldEventInstance instance : worldData.activeWorldEvents) {
                if (instance.uuid.equals(uuid)) {
                    instance.deserializeNBT(eventData);
                    break;
                }
            }
        }
    }

    @Override
    public void serialize(FriendlyByteBuf byteBuf) {
        byteBuf.writeUUID(uuid);
        byteBuf.writeNbt(eventData);
    }
}
