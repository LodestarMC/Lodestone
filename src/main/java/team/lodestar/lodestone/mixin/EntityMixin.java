package team.lodestar.lodestone.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.lodestar.lodestone.handlers.FireEffectHandler;
import team.lodestar.lodestone.systems.sound.ExtendedSoundType;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;updateSwimming()V"))
    private void lodestoneFireEffectTicking(CallbackInfo ci) {
        FireEffectHandler.entityUpdate(((Entity) (Object) this));
    }

    @Inject(method = "setSecondsOnFire", at = @At(value = "RETURN"))
    private void lodestoneFireEffectOverride(int pSeconds, CallbackInfo ci) {
        FireEffectHandler.onVanillaFireTimeUpdate((Entity) (Object) this);
    }

    @Unique
    private SoundType type;

    @ModifyVariable(method = "playStepSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/SoundType;getStepSound()Lnet/minecraft/sounds/SoundEvent;"))
    private SoundType lodestoneGetStepSound(SoundType type) {
        return this.type = type;
    }

    @Inject(method = "playStepSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"))
    private void lodestoneCallExtendedStepSound(BlockPos pos, BlockState state, CallbackInfo ci) {
        if (type instanceof ExtendedSoundType extendedSoundType) {
            Entity entity = ((Entity) (Object) this);
            extendedSoundType.onPlayStepSound(entity.level(), pos, state, entity.getSoundSource());
        }
    }
}