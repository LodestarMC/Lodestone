package team.lodestar.lodestone.systems.rendering.rendeertype;

import net.minecraft.client.renderer.ShaderInstance;

public interface ShaderUniformHandler {

    ShaderUniformHandler LUMITRANSPARENT = instance -> {
        instance.safeGetUniform("LumiTransparency").set(1f);
    };

    ShaderUniformHandler DEPTH_FADE = instance -> {
        instance.safeGetUniform("DepthFade").set(1f);
    };

    void updateShaderData(ShaderInstance instance);
}
