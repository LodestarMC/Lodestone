package team.lodestar.lodestone.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.shaders.Program;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EffectInstance.class)
public class EffectInstanceMixin {

    @WrapOperation(
            at = @At(
                    value = "NEW",
                    target = "(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;",
                    ordinal = 0
            ),
            method = "<init>"
    )
    private ResourceLocation lodestone$modifyNameSpace(String arg, Operation<ResourceLocation> original, @Local ResourceManager unused, @Local String id) {
        if (!id.contains(":")) {
            return original.call(arg);
        }
        ResourceLocation split = new ResourceLocation(id);
        return new ResourceLocation(split.getNamespace(), "shaders/program/" + split.getPath() + ".json");
    }

    @WrapOperation(
            at = @At(
                    value = "NEW",
                    target = "(Ljava/lang/String;)Lnet/minecraft/resources/ResourceLocation;",
                    ordinal = 0
            ),
            method = "getOrCreate"
    )
    private static ResourceLocation lodestone$modifyNameSpace2(String arg, Operation<ResourceLocation> original, ResourceManager unused, Program.Type shaderType, String id) {
        if (!arg.contains(":")) {
            return original.call(arg);
        }
        ResourceLocation split = new ResourceLocation(id);
        return new ResourceLocation(split.getNamespace(), "shaders/program/" + split.getPath() + shaderType.getExtension());
    }
}
