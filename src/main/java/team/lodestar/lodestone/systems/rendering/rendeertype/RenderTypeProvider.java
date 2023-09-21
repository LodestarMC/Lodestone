package team.lodestar.lodestone.systems.rendering.rendeertype;

import net.minecraft.Util;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.setup.LodestoneRenderTypeRegistry;

import java.util.function.Function;

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
