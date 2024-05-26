package team.lodestar.lodestone.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SoundType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.lodestar.lodestone.events.LodestoneInteractionEvent;
import team.lodestar.lodestone.events.LodestoneMobEffectEvents;
import team.lodestar.lodestone.systems.sound.ExtendedSoundType;

import static team.lodestar.lodestone.registry.common.LodestoneAttributeRegistry.*;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {


    @Shadow protected int useItemRemaining;
    @Unique
    private SoundType lodestone$type;

    @Unique
    private int newUseTime;

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
        return original.add(MAGIC_RESISTANCE.get()).add(MAGIC_PROFICIENCY.get()).add(MAGIC_DAMAGE.get());
    }

    @Inject(method = "startUsingItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getUseDuration()I"), cancellable = true)
    private void lodestone$injectUseEvent(InteractionHand hand, CallbackInfo ci, @Local ItemStack stack){

        int duration = LodestoneInteractionEvent.ON_ITEM_USE_START.invoker().onItemUseStart((LivingEntity) (Object)this, stack, stack.getUseDuration());
        if (duration <= 0) {
            ci.cancel();
        }
        this.newUseTime = duration;
    }

    @Inject(method = "startUsingItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getUseDuration()I", shift = At.Shift.AFTER))
    private void lodestone$modifyUseDuration(InteractionHand hand, CallbackInfo ci) {
        this.useItemRemaining = this.newUseTime;
    }

    @Inject(method = "canBeAffected", at = @At(value = "HEAD"), cancellable = true)
    private void lodestone$canBeAffected(MobEffectInstance effectInstance, CallbackInfoReturnable<Boolean> cir) {
        boolean bl = LodestoneMobEffectEvents.APPLICABLE.invoker().canBeAffected((LivingEntity) (Object)this, effectInstance);
        if (!bl) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;", shift = At.Shift.AFTER))
    private void lodestone$add(MobEffectInstance effectInstance, Entity entity, CallbackInfoReturnable<Boolean> cir, @Local(argsOnly = true) MobEffectInstance mobEffectInstance) {
        LodestoneMobEffectEvents.ADDED.invoker().canBeAffected((LivingEntity) (Object)this, effectInstance, mobEffectInstance, entity);
    }

}
