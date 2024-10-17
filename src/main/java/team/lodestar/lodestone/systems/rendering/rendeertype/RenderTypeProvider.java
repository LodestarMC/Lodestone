package team.lodestar.lodestone.systems.rendering.rendeertype;

import net.minecraft.Util;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry.*;
import team.lodestar.lodestone.systems.rendering.*;

import java.util.*;
import java.util.function.*;

public class RenderTypeProvider {
    private final Function<RenderTypeToken, LodestoneRenderType> function;
    private final Function<RenderTypeToken, LodestoneRenderType> memorizedFunction;
    private final HashMap<ShaderUniformHandler, Function<RenderTypeToken, LodestoneRenderType>> uniformHandlerCache;

    public RenderTypeProvider(Function<RenderTypeToken, LodestoneRenderType> function) {
        this.function = function;
        this.memorizedFunction = Util.memoize(function);
        this.uniformHandlerCache = new HashMap<>();
    }

    public LodestoneRenderType apply(RenderTypeToken token) {
        return function.apply(token);
    }

    public LodestoneRenderType apply(RenderTypeToken token, ShaderUniformHandler uniformHandler) {
        var renderType = apply(token);
        return LodestoneRenderTypeRegistry.applyUniformChanges(renderType, uniformHandler);
    }

    public LodestoneRenderType applyAndCache(RenderTypeToken token) {
        return this.memorizedFunction.apply(token);
    }

    public LodestoneRenderType applyAndCache(RenderTypeToken token, ShaderUniformHandler uniformHandler) {
        if (!uniformHandlerCache.containsKey(uniformHandler)) {
            uniformHandlerCache.put(uniformHandler, Util.memoize(function));
        }
        var renderType = uniformHandlerCache.get(uniformHandler).apply(token);
        return LodestoneRenderTypeRegistry.applyUniformChanges(renderType, uniformHandler);
    }

    public LodestoneRenderType applyWithModifier(RenderTypeToken token, Consumer<LodestoneCompositeStateBuilder> modifier) {
        LodestoneRenderTypeRegistry.addRenderTypeModifier(modifier);
        return apply(token);
    }

    public LodestoneRenderType applyWithModifier(RenderTypeToken token, ShaderUniformHandler uniformHandler, Consumer<LodestoneCompositeStateBuilder> modifier) {
        LodestoneRenderTypeRegistry.addRenderTypeModifier(modifier);
        return apply(token, uniformHandler);
    }

    public LodestoneRenderType applyWithModifierAndCache(RenderTypeToken token, Consumer<LodestoneCompositeStateBuilder> modifier) {
        LodestoneRenderTypeRegistry.addRenderTypeModifier(modifier);
        return applyAndCache(token);
    }

    public LodestoneRenderType applyWithModifierAndCache(RenderTypeToken token, ShaderUniformHandler uniformHandler, Consumer<LodestoneCompositeStateBuilder> modifier) {
        LodestoneRenderTypeRegistry.addRenderTypeModifier(modifier);
        return applyAndCache(token, uniformHandler);
    }
}
