package team.lodestar.lodestone.mixin;

import net.fabricmc.fabric.impl.client.rendering.FabricShaderProgram;
import net.minecraft.client.renderer.ShaderInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import team.lodestar.lodestone.systems.rendering.shader.ExtendedShaderInstance;

@Mixin(ShaderInstance.class)
public class ShaderInstanceMixin {

    @Shadow @Final private String name;

    // Allow loading our ShaderPrograms from arbitrary namespaces.
    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/ResourceLocation;<init>(Ljava/lang/String;)V"), allow = 1)
    private String modifyProgramId(String id) {
        if ((Object) this instanceof ExtendedShaderInstance) {
            return FabricShaderProgram.rewriteAsId(id, name);
        }

        return id;
    }
}
