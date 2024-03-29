package team.lodestar.lodestone.capability;

import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.helpers.NBTHelper;
import team.lodestar.lodestone.network.capability.SyncLodestoneEntityCapabilityPacket;
import team.lodestar.lodestone.setup.LodestonePacketRegistry;
import team.lodestar.lodestone.systems.capability.LodestoneCapability;
import team.lodestar.lodestone.systems.capability.LodestoneCapabilityProvider;
import team.lodestar.lodestone.systems.fireeffect.FireEffectInstance;
import team.lodestar.lodestone.handlers.FireEffectHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.network.PacketDistributor;

public class LodestoneEntityDataCapability implements LodestoneCapability {

    public static Capability<LodestoneEntityDataCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public FireEffectInstance fireEffectInstance;

    public LodestoneEntityDataCapability() {

    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(LodestoneEntityDataCapability.class);
    }

    public static void attachEntityCapability(AttachCapabilitiesEvent<Entity> event) {
        final LodestoneEntityDataCapability capability = new LodestoneEntityDataCapability();
        event.addCapability(LodestoneLib.lodestonePath("entity_data"), new LodestoneCapabilityProvider<>(LodestoneEntityDataCapability.CAPABILITY, () -> capability));
    }

    public static void syncEntityCapability(PlayerEvent.StartTracking event) {
        if (event.getEntity().level instanceof ServerLevel) {
            LodestoneEntityDataCapability.syncTracking(event.getEntityLiving());
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
        getCapabilityOptional(entity).ifPresent(c -> LodestonePacketRegistry.ORTUS_CHANNEL.send(target, new SyncLodestoneEntityCapabilityPacket(entity.getId(), c.serializeNBT())));
    }

    public static void syncTrackingAndSelf(Entity entity, NBTHelper.TagFilter filter) {
        sync(entity, PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), filter);
    }

    public static void syncTracking(Entity entity, NBTHelper.TagFilter filter) {
        sync(entity, PacketDistributor.TRACKING_ENTITY.with(() -> entity), filter);
    }

    public static void sync(Entity entity, PacketDistributor.PacketTarget target, NBTHelper.TagFilter filter) {
        getCapabilityOptional(entity).ifPresent(c -> LodestonePacketRegistry.ORTUS_CHANNEL.send(target, new SyncLodestoneEntityCapabilityPacket(entity.getId(), NBTHelper.filterTag(c.serializeNBT(), filter))));
    }

    public static LazyOptional<LodestoneEntityDataCapability> getCapabilityOptional(Entity entity) {
        return entity.getCapability(CAPABILITY);
    }

    public static LodestoneEntityDataCapability getCapability(Entity entity) {
        return entity.getCapability(CAPABILITY).orElse(new LodestoneEntityDataCapability());
    }
}