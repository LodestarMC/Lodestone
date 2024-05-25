package team.lodestar.lodestone.systems.rendering.rendeertype;

import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry.*;
import team.lodestar.lodestone.systems.rendering.*;

import java.util.function.*;

public class RenderTypeProvider {
    private final Function<RenderTypeToken, LodestoneRenderType> function;
    private final Function<RenderTypeToken, LodestoneRenderType> memorizedFunction;

    public RenderTypeProvider(Function<RenderTypeToken, LodestoneRenderType> function) {
        this.function = function;
        this.memorizedFunction = Util.memoize(function);
    }

    public LodestoneRenderType apply(RenderTypeToken token) {
        return function.apply(token);
    }

    public LodestoneRenderType apply(RenderTypeToken token, ShaderUniformHandler uniformHandler) {
        return LodestoneRenderTypeRegistry.applyUniformChanges(apply(token), uniformHandler);
    }

    public LodestoneRenderType applyAndCache(RenderTypeToken token) {
        return this.memorizedFunction.apply(token);
    }

    public LodestoneRenderType applyAndCache(RenderTypeToken token, ShaderUniformHandler uniformHandler) {
        return LodestoneRenderTypeRegistry.applyUniformChanges(applyAndCache(token), uniformHandler);
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
        return LodestoneRenderTypeRegistry.applyUniformChanges(applyAndCache(token), uniformHandler);
    }
}
