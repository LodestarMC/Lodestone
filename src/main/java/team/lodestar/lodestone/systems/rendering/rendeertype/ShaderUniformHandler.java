package team.lodestar.lodestone.systems.rendering.rendeertype;

import net.minecraft.client.renderer.ShaderInstance;

public interface ShaderUniformHandler {

    ShaderUniformHandler LUMITRANSPARENT = instance -> {
        instance.safeGetUniform("LumiTransparency").set(1);
    };

    void updateShaderData(ShaderInstance instance);
}
