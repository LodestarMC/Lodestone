package team.lodestar.lodestone.core.mixin.common;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.SoundType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.lodestar.lodestone.core.sound.ExtendedSoundType;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @SuppressWarnings("UnreachableCode")
    @Inject(method = "playBlockFallSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"))
    private void lodestone$CallExtendedStepSound(CallbackInfo ci, @Local SoundType soundType) {
        if (soundType instanceof ExtendedSoundType extendedSoundType) {
            var entity = ((LivingEntity) (Object) this);
            extendedSoundType.onPlayFallSound(entity.level(), entity, entity.getOnPos(),
                    ((event, volume, pitch) -> entity.playSound(
                            event,
                            volume * 0.5F,
                            pitch * 0.75F)
                    ));
        }
    }
}
