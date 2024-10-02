package team.lodestar.lodestone.mixin.common.attributeslib;


import dev.shadowsoffire.attributeslib.client.*;
import net.minecraft.world.entity.ai.attributes.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import team.lodestar.lodestone.registry.common.*;

import java.util.*;

@Pseudo
@Mixin(AttributesLibClient.class)
public class AttributeLibClientMixin {

    @Unique
    private static AttributeModifier lodestone$modifier;

    @Inject(method = "lambda$applyTextFor$6", at = @At(value = "HEAD"), remap = false)
    private static void lodestone$cacheModifier(Map<?, ?> baseModifs, Attribute attr, AttributeModifier modif, CallbackInfo ci) {
        lodestone$modifier = modif;
    }
    @ModifyArg(method = "lambda$applyTextFor$6", at = @At(value = "INVOKE", target = "Ljava/util/UUID;equals(Ljava/lang/Object;)Z"), remap = false)
    private static Object lodestone$getBaseUUID(Object obj) {
        if (lodestone$modifier.getId().equals(LodestoneAttributeRegistry.UUIDS.get(LodestoneAttributeRegistry.MAGIC_DAMAGE))) {
            final UUID id = lodestone$modifier.getId();
            lodestone$modifier = null;
            return id;
        }
        return obj;
    }
}
