package team.lodestar.lodestone.network.worldevent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import team.lodestar.lodestone.capability.LodestoneWorldDataCapability;
import team.lodestar.lodestone.systems.network.OneSidedPayloadData;
import team.lodestar.lodestone.systems.worldevent.WorldEventInstance;

import java.util.UUID;

public class UpdateWorldEventPayload extends OneSidedPayloadData {

    private UUID uuid;
    private CompoundTag eventData;

    public UpdateWorldEventPayload(FriendlyByteBuf byteBuf) {
        super(byteBuf);
        uuid = byteBuf.readUUID();
        eventData = byteBuf.readNbt();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle(IPayloadContext context) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null) {
            level.getCapability(LodestoneWorldDataCapability.CAPABILITY).ifPresent(capability -> {
                for (WorldEventInstance instance : capability.activeWorldEvents) {
                    if (instance.uuid.equals(uuid)) {
                        instance.deserializeNBT(eventData);
                        break;
                    }
                }
            });
        }
    }

    @Override
    public void serialize(FriendlyByteBuf byteBuf) {
        byteBuf.writeUUID(uuid);
        byteBuf.writeNbt(eventData);
    }
}
