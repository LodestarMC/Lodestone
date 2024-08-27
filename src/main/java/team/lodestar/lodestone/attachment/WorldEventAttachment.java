package team.lodestar.lodestone.attachment;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.UnknownNullability;
import team.lodestar.lodestone.registry.common.LodestoneWorldEventTypeRegistry;
import team.lodestar.lodestone.systems.worldevent.WorldEventInstance;
import team.lodestar.lodestone.systems.worldevent.WorldEventType;

import java.util.ArrayList;

public class WorldEventAttachment implements INBTSerializable<CompoundTag> {

    public final ArrayList<WorldEventInstance> activeWorldEvents = new ArrayList<>();
    public final ArrayList<WorldEventInstance> inboundWorldEvents = new ArrayList<>();


    @Override
    public @UnknownNullability CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        CompoundTag worldTag = new CompoundTag();
        worldTag.putInt("worldEventCount", activeWorldEvents.size());
        for (int i = 0; i < activeWorldEvents.size(); i++) {
            WorldEventInstance instance = activeWorldEvents.get(i);
            CompoundTag instanceTag = new CompoundTag();
            instance.serializeNBT(instanceTag);
            worldTag.put("worldEvent_" + i, instanceTag);
        }
        tag.put("worldEventData", worldTag);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        activeWorldEvents.clear();
        CompoundTag worldTag = tag.getCompound("worldEventData");
        int worldEventCount = worldTag.getInt("worldEventCount");
        for (int i = 0; i < worldEventCount; i++) {
            CompoundTag instanceTag = worldTag.getCompound("worldEvent_" + i);
            WorldEventType reader = LodestoneWorldEventTypeRegistry.WORLD_EVENT_TYPE_REGISTRY.get(ResourceLocation.parse(instanceTag.getString("type")));
            WorldEventInstance eventInstance = reader.createInstance(instanceTag);
            activeWorldEvents.add(eventInstance);
        }
    }
}
