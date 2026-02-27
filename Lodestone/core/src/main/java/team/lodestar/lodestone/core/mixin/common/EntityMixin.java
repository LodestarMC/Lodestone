package team.lodestar.lodestone.core.mixin.common;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.lodestar.lodestone.core.sound.ExtendedSoundType;

@Mixin(Entity.class)
public class EntityMixin {

    @SuppressWarnings("UnreachableCode")
    @Inject(method = "playStepSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"))
    private void lodestone$CallExtendedStepSound(BlockPos pos, BlockState state, CallbackInfo ci, @Local SoundType soundType) {
        if (soundType instanceof ExtendedSoundType extendedSoundType) {
            Entity entity = ((Entity) (Object) this);
            extendedSoundType.onPlayStepSound(entity.level(), entity, pos, state,
                    ((event, volume, pitch) -> entity.playSound(
                            event,
                            volume * 0.15F,
                            pitch)
                    ));
        }
    }
}