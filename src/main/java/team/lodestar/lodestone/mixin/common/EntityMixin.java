package team.lodestar.lodestone.mixin.common;

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
    private void lodestone$FireEffectTicking(CallbackInfo ci) {
        FireEffectHandler.entityUpdate(((Entity) (Object) this));
    }

    @Inject(method = "setRemainingFireTicks", at = @At(value = "RETURN"))
    private void lodestone$FireEffectOverride(int pSeconds, CallbackInfo ci) {
        FireEffectHandler.onVanillaFireTimeUpdate((Entity) (Object) this);
    }

    @Unique
    private SoundType lodestone$type;

    @ModifyVariable(method = "playStepSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/SoundType;getStepSound()Lnet/minecraft/sounds/SoundEvent;"))
    private SoundType lodestone$GetStepSound(SoundType type) {
        return this.lodestone$type = type;
    }

    @Inject(method = "playStepSound", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"))
    private void lodestone$CallExtendedStepSound(BlockPos pos, BlockState state, CallbackInfo ci) {
        if (lodestone$type instanceof ExtendedSoundType extendedSoundType) {
            Entity entity = ((Entity) (Object) this);
            extendedSoundType.onPlayStepSound(entity.level(), pos, state, entity.getSoundSource());
        }
    }
}