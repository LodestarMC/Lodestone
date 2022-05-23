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

public class OrtusWorldDataCapability implements OrtusCapability {

    public static Capability<OrtusWorldDataCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });
    public final ArrayList<WorldEventInstance> activeWorldEvents = new ArrayList<>();
    public final ArrayList<WorldEventInstance> inboundWorldEvents = new ArrayList<>();

    public OrtusWorldDataCapability() {
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(OrtusWorldDataCapability.class);
    }

    public static void attachWorldCapability(AttachCapabilitiesEvent<Level> event) {
        final OrtusWorldDataCapability capability = new OrtusWorldDataCapability();
        event.addCapability(OrtusLib.ortusPrefix("world_data"), new OrtusCapabilityProvider<>(OrtusWorldDataCapability.CAPABILITY, () -> capability));
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

    public static LazyOptional<OrtusWorldDataCapability> getCapabilityOptional(Level level) {
        return level.getCapability(CAPABILITY);
    }

    public static OrtusWorldDataCapability getCapability(Level level) {
        return level.getCapability(CAPABILITY).orElse(new OrtusWorldDataCapability());
    }
}