package team.lodestar.lodestone.mixin.client;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.EffectInstance;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.systems.rendering.shader.SamplerType;

import java.util.List;
import java.util.Map;

@Mixin(EffectInstance.class)
public abstract class EffectInstanceMixin {

    @Unique
    private final Map<String, SamplerType> lodestone$samplerTypeMap = Maps.newHashMap();

    @Redirect(method = "parseSamplerNode", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    private boolean samplerDimension(List<String> samplerNames, Object s, @Local JsonElement json) {
        String name = s.toString();
        if (json.getAsJsonObject().has("type")) {
            String type1 = json.getAsJsonObject().get("type").getAsString();
            SamplerType type = SamplerType.fromString(type1);
            if (type == null) {
                LodestoneLib.LOGGER.warn("Unknown sampler type: " + type1);
            } else {
                lodestone$samplerTypeMap.put(name, type);
            }
        }
        return samplerNames.add(name);
    }

    @Redirect(method = "apply", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;bindTexture(I)V"))
    private void bindTex(int textureBinding, @Local String s) {
        if (lodestone$samplerTypeMap.containsKey(s)) {
            SamplerType type = lodestone$samplerTypeMap.get(s);
            lodestone$bindTexture(type.getGlType(), textureBinding);
        } else {
            RenderSystem.bindTexture(textureBinding);
        }
    }

    @Unique
    private void lodestone$bindTexture(int samplerType, int texture) {
        GlStateManager.TextureState activeTexture = GlStateManager.TEXTURES[GlStateManager.activeTexture];
        if (texture != activeTexture.binding) {
            activeTexture.binding = texture;
            GL11.glBindTexture(samplerType, texture);
        }
    }
}
