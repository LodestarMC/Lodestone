package com.sammy.ortus.capability;

import com.sammy.ortus.OrtusLib;
import com.sammy.ortus.handlers.FireEffectHandler;
import com.sammy.ortus.helpers.NBTHelper;
import com.sammy.ortus.network.packet.SyncOrtusEntityCapabilityPacket;
import com.sammy.ortus.systems.capability.OrtusCapability;
import com.sammy.ortus.systems.capability.OrtusCapabilityProvider;
import com.sammy.ortus.systems.fireeffect.FireEffectInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.network.PacketDistributor;

import static com.sammy.ortus.setup.OrtusPacketRegistry.INSTANCE;

public class OrtusEntityDataCapability implements OrtusCapability {

    public static Capability<OrtusEntityDataCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public FireEffectInstance fireEffectInstance;

    public OrtusEntityDataCapability() {

    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(OrtusEntityDataCapability.class);
    }

    public static void attachEntityCapability(AttachCapabilitiesEvent<Entity> event) {
        final OrtusEntityDataCapability capability = new OrtusEntityDataCapability();
        event.addCapability(OrtusLib.ortusPrefix("entity_data"), new OrtusCapabilityProvider<>(OrtusEntityDataCapability.CAPABILITY, () -> capability));
    }

    public static void syncEntityCapability(PlayerEvent.StartTracking event) {
        if (event.getEntity().level instanceof ServerLevel) {
            OrtusEntityDataCapability.syncTracking(event.getEntityLiving());
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        FireEffectHandler.serializeNBT(this, tag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        FireEffectHandler.deserializeNBT(this, tag);
    }

    public static void syncTrackingAndSelf(Entity entity) {
        sync(entity, PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity));
    }

    public static void syncTracking(Entity entity) {
        sync(entity, PacketDistributor.TRACKING_ENTITY.with(() -> entity));
    }

    public static void sync(Entity entity, PacketDistributor.PacketTarget target) {
        getCapabilityOptional(entity).ifPresent(c -> INSTANCE.send(target, new SyncOrtusEntityCapabilityPacket(entity.getId(), c.serializeNBT())));
    }

    public static void syncTrackingAndSelf(Entity entity, NBTHelper.TagFilter filter) {
        sync(entity, PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), filter);
    }

    public static void syncTracking(Entity entity, NBTHelper.TagFilter filter) {
        sync(entity, PacketDistributor.TRACKING_ENTITY.with(() -> entity), filter);
    }

    public static void sync(Entity entity, PacketDistributor.PacketTarget target, NBTHelper.TagFilter filter) {
        getCapabilityOptional(entity).ifPresent(c -> INSTANCE.send(target, new SyncOrtusEntityCapabilityPacket(entity.getId(), NBTHelper.filterTag(c.serializeNBT(), filter))));
    }

    public static LazyOptional<OrtusEntityDataCapability> getCapabilityOptional(Entity entity) {
        return entity.getCapability(CAPABILITY);
    }

    public static OrtusEntityDataCapability getCapability(Entity entity) {
        return entity.getCapability(CAPABILITY).orElse(new OrtusEntityDataCapability());
    }
}