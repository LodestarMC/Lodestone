package team.lodestar.lodestone.component;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.UnknownNullability;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.CommonTickingComponent;
import team.lodestar.lodestone.handlers.WorldEventHandler;
import team.lodestar.lodestone.registry.common.LodestoneWorldEventTypes;
import team.lodestar.lodestone.systems.worldevent.WorldEventInstance;
import team.lodestar.lodestone.systems.worldevent.WorldEventType;

import java.util.ArrayList;

public class LodestoneWorldComponent implements AutoSyncedComponent, CommonTickingComponent {
    public Level level;
    public final ArrayList<WorldEventInstance> activeWorldEvents = new ArrayList<>();
    public final ArrayList<WorldEventInstance> inboundWorldEvents = new ArrayList<>();

    public LodestoneWorldComponent(Level level) {
        this.level = level;
    }


    @Override
    public void tick() {
        WorldEventHandler.worldTick(level);
        if (level.isClientSide()) {
            WorldEventHandler.tick(level);
        }
    }

    @Override
    public void readFromNbt(CompoundTag tag, HolderLookup.Provider registryLookup) {
        activeWorldEvents.clear();
        CompoundTag worldTag = tag.getCompound("worldEventData");
        int worldEventCount = worldTag.getInt("worldEventCount");
        for (int i = 0; i < worldEventCount; i++) {
            CompoundTag instanceTag = worldTag.getCompound("worldEvent_" + i);
            WorldEventType type = LodestoneWorldEventTypes.WORLD_EVENT_TYPE_REGISTRY.get(ResourceLocation.parse(instanceTag.getString("type")));
            WorldEventInstance eventInstance = type.createInstance(instanceTag);
            activeWorldEvents.add(eventInstance);
        }
    }

    @Override
    public void writeToNbt(CompoundTag tag, HolderLookup.Provider registryLookup) {
        CompoundTag worldTag = new CompoundTag();
        worldTag.putInt("worldEventCount", activeWorldEvents.size());
        for (int i = 0; i < activeWorldEvents.size(); i++) {
            WorldEventInstance instance = activeWorldEvents.get(i);
            CompoundTag instanceTag = instance.serializeNBT();
            worldTag.put("worldEvent_" + i, instanceTag);
        }
        tag.put("worldEventData", worldTag);
    }
}
