package com.sammy.ortus.mixin;

import com.sammy.ortus.handlers.FireEffectHandler;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;updateSwimming()V"))
    private void fundamentalForcesCustomFireTicking(CallbackInfo ci)
    {
        FireEffectHandler.entityUpdate(((Entity)(Object)this));
    }

    @Inject(method = "setSecondsOnFire", at = @At(value = "RETURN"))
    private void fundamentalForcesCustomFireOverride(int pSeconds, CallbackInfo ci)
    {
        FireEffectHandler.onVanillaFireTimeUpdate((Entity)(Object)this);
    }
}
