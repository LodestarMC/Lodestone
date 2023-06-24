package team.lodestar.lodestone.systems.rendering.rendeertype;

import net.minecraft.*;
import net.minecraft.client.renderer.*;
import net.minecraft.resources.*;
import team.lodestar.lodestone.registry.client.*;

import java.util.function.*;

public class RenderTypeProvider {
    private final Function<ResourceLocation, RenderType> function;
    private final Function<ResourceLocation, RenderType> memorizedFunction;

    public RenderTypeProvider(Function<ResourceLocation, RenderType> function) {
        this.function = function;
        this.memorizedFunction = Util.memoize(function);
    }

    public RenderType apply(ResourceLocation texture) {
        return function.apply(texture);
    }

    public RenderType apply(ResourceLocation texture, ShaderUniformHandler uniformHandler) {
        return LodestoneRenderTypeRegistry.applyUniformChanges(function.apply(texture), uniformHandler);
    }

    public RenderType applyAndCache(ResourceLocation texture) {
        return this.memorizedFunction.apply(texture);
    }
}
