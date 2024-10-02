package team.lodestar.lodestone.mixin.common.attributeslib;


import dev.shadowsoffire.attributeslib.api.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import team.lodestar.lodestone.registry.common.*;

import javax.management.*;
import java.util.*;

@SuppressWarnings({"RedundantCast", "DataFlowIssue"})
@Pseudo
@Mixin(IFormattableAttribute.class)
public class IFormattableAttributeMixin {

    @Inject(method = "getBaseUUID", at = @At("HEAD"), cancellable = true, remap = false)
    private void lodestone$getBaseUUID(CallbackInfoReturnable<UUID> cir) {
        if ((Attribute)((Object)this) == LodestoneAttributeRegistry.MAGIC_DAMAGE.get()) {
            cir.setReturnValue(LodestoneAttributeRegistry.UUIDS.get(LodestoneAttributeRegistry.MAGIC_DAMAGE));
        }
    }
}
