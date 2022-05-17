package com.sammy.ortus.mixin;

import com.sammy.ortus.config.ClientConfig;
import com.sammy.ortus.handlers.ScreenshakeHandler;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.sammy.ortus.OrtusLib.RANDOM;


@Mixin(Camera.class)
public class CameraMixin {

    @Inject(method = "setup", at = @At("RETURN"))
    private void ortusScreenshake(BlockGetter area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        if (ClientConfig.SCREENSHAKE_INTENSITY.getConfigValue() > 0) {
            ScreenshakeHandler.cameraTick((Camera) (Object) this, RANDOM);
        }
    }
}