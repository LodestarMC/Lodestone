package team.lodestar.lodestone.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.block.SoundType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.lodestar.lodestone.events.EntityAttributeModificationEvent;
import team.lodestar.lodestone.systems.sound.ExtendedSoundType;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {


    @Unique
    private SoundType lodestone$type;

    @ModifyVariable(method = "playBlockFallSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"))
    private SoundType lodestone$GetStepSound(SoundType type) {
        return this.lodestone$type = type;
    }

    @Inject(method = "playBlockFallSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"))
    private void lodestone$CallExtendedStepSound(CallbackInfo ci) {
        if (lodestone$type instanceof ExtendedSoundType extendedSoundType) {
            Entity entity = ((Entity) (Object) this);
            extendedSoundType.onPlayFallSound(entity.level(), entity.getOnPos(), entity.getSoundSource());
        }
    }

    @ModifyReturnValue(method = "createLivingAttributes", at = @At("RETURN"))
    private static AttributeSupplier.Builder lodestone$CreateLivingAttributes(AttributeSupplier.Builder original) {
        AttributeSupplier.Builder list = original;
        list = EntityAttributeModificationEvent.ADD.invoker().add(list);;
        return list;
    }
}
