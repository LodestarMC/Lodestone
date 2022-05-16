package com.sammy.ortus.capability;

import com.sammy.ortus.OrtusLib;
import com.sammy.ortus.handlers.WorldEventHandler;
import com.sammy.ortus.systems.capability.OrtusCapability;
import com.sammy.ortus.systems.capability.OrtusCapabilityProvider;
import com.sammy.ortus.systems.worldevent.WorldEventInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;

import java.util.ArrayList;

public class WorldDataCapability implements OrtusCapability {

    //shove all level data here, use WorldDataCapability.getCapability(level) to access data.
    //level refers to dimension, not world. Each dimension will have it's own capability instance.
    public static Capability<WorldDataCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });
    public final ArrayList<WorldEventInstance> activeWorldEvents = new ArrayList<>();
    public final ArrayList<WorldEventInstance> inboundWorldEvents = new ArrayList<>();

    public WorldDataCapability() {
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(WorldDataCapability.class);
    }

    public static void attachWorldCapability(AttachCapabilitiesEvent<Level> event) {
        final WorldDataCapability capability = new WorldDataCapability();
        event.addCapability(OrtusLib.ortusPrefix("world_data"), new OrtusCapabilityProvider<>(WorldDataCapability.CAPABILITY, () -> capability));
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        WorldEventHandler.serializeNBT(this, tag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        WorldEventHandler.deserializeNBT(this, nbt);
    }

    public static LazyOptional<WorldDataCapability> getCapability(Level level) {
        return level.getCapability(CAPABILITY);
    }
}