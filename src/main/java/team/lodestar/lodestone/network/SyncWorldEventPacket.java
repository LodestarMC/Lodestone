package team.lodestar.lodestone.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import team.lodestar.lodestone.handlers.WorldEventHandler;
import team.lodestar.lodestone.setup.worldevent.LodestoneWorldEventTypeRegistry;
import team.lodestar.lodestone.systems.network.LodestoneClientPacket;
import team.lodestar.lodestone.systems.worldevent.WorldEventType;

import java.util.function.Supplier;

public class SyncWorldEventPacket extends LodestoneClientPacket {
    String type;
    public boolean start;
    public CompoundTag eventData;

    public SyncWorldEventPacket(String type, boolean start, CompoundTag eventData) {
        this.type = type;
        this.start = start;
        this.eventData = eventData;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(type);
        buf.writeBoolean(start);
        buf.writeNbt(eventData);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void execute(Supplier<NetworkEvent.Context> context) {
        WorldEventType eventType = LodestoneWorldEventTypeRegistry.EVENT_TYPES.get(type);
        ClientLevel level = Minecraft.getInstance().level;
        WorldEventHandler.addWorldEvent(level, start, eventType.createInstance(eventData));
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, SyncWorldEventPacket.class, SyncWorldEventPacket::encode, SyncWorldEventPacket::decode, SyncWorldEventPacket::handle);
    }

    public static SyncWorldEventPacket decode(FriendlyByteBuf buf) {
        return new SyncWorldEventPacket(buf.readUtf(), buf.readBoolean(), buf.readNbt());
    }
}