package team.lodestar.lodestone.test;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.systems.worldevent.WorldEventInstance;

public class TestWorldEvent extends WorldEventInstance {
    int tickCount;
    int duration = 600;
    TestWorldEvent() {
        super(TestWorldEventTypes.TEST);
    }

    @Override
    public CompoundTag serializeNBT(CompoundTag tag) {
        tag.putInt("tickCount", tickCount);
        tag.putInt("duration", duration);
        return super.serializeNBT(tag);
    }

    @Override
    public WorldEventInstance deserializeNBT(CompoundTag tag) {
        this.tickCount = tag.getInt("tickCount");
        this.duration = tag.getInt("duration");
        return super.deserializeNBT(tag);
    }

    @Override
    public void tick(Level level) {
        if (level.isClientSide()) {
            LodestoneLib.LOGGER.info("Level: {}, UUID: {}, tickCount: {}", level, uuid, tickCount);
        } else {
            LodestoneLib.LOGGER.info("Level: {}, UUID: {}, tickCount: {}", level, uuid, tickCount);
            tickCount++;
        }

        boolean shouldEnd = false;
        if (!level.isClientSide() && tickCount >= duration) shouldEnd = true;

        if (tickCount >= duration) {
            end(level);
            LodestoneLib.LOGGER.info("Event ended");
        }
    }


    @Override
    public boolean isClientSynced() {
        return true;
    }
}
