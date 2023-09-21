package team.lodestar.lodestone.mixin;

import team.lodestar.lodestone.systems.sound.ExtendedSoundType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.SoundType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {


    @Unique
    private SoundType type;

    @ModifyVariable(method = "playBlockFallSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"))
    private SoundType lodestoneGetStepSound(SoundType type) {
        return this.type = type;
    }

    @Inject(method = "playBlockFallSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"))
    private void lodestoneCallExtendedStepSound(CallbackInfo ci) {
        if (type instanceof ExtendedSoundType extendedSoundType) {
            Entity entity = ((Entity)(Object)this);
            extendedSoundType.onPlayFallSound(entity.level(), entity.getOnPos(), entity.getSoundSource());
        }
    }
}
