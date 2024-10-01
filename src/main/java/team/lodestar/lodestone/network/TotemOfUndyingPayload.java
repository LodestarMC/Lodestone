package team.lodestar.lodestone.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import team.lodestar.lodestone.systems.network.OneSidedPayloadData;

public class TotemOfUndyingPayload extends OneSidedPayloadData {

    private final int entityId;
    private ItemStack stack;

    public TotemOfUndyingPayload(FriendlyByteBuf byteBuf) {
        entityId = byteBuf.readInt();
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void handle(IPayloadContext context) {
        Minecraft minecraft = Minecraft.getInstance();
        Entity entity = minecraft.level.getEntity(entityId);
        if (entity instanceof LivingEntity livingEntity) {
            minecraft.particleEngine.createTrackingEmitter(livingEntity, ParticleTypes.TOTEM_OF_UNDYING, 30);
            minecraft.level.playLocalSound(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), SoundEvents.TOTEM_USE, livingEntity.getSoundSource(), 1.0F, 1.0F, false);
            if (livingEntity == minecraft.player) {
                minecraft.gameRenderer.displayItemActivation(stack);
            }
        }
    }

    @Override
    public void serialize(FriendlyByteBuf byteBuf) {
        byteBuf.writeInt(entityId);

        //TODO: saving the stack requires registry access, need to figure out the whole RegistryFriendlyByteBuf thing
//        stack.save()
    }

}
