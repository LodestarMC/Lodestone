package team.lodestar.lodestone.systems.rendering.rendeertype;

import net.minecraft.Util;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypes;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypes.*;
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
        return LodestoneRenderTypes.applyUniformChanges(apply(token), uniformHandler);
    }

    public LodestoneRenderType applyAndCache(RenderTypeToken token) {
        return this.memorizedFunction.apply(token);
    }

    public LodestoneRenderType applyAndCache(RenderTypeToken token, ShaderUniformHandler uniformHandler) {
        return LodestoneRenderTypes.applyUniformChanges(applyAndCache(token), uniformHandler);
    }

    public LodestoneRenderType applyWithModifier(RenderTypeToken token, Consumer<LodestoneCompositeStateBuilder> modifier) {
        LodestoneRenderTypes.addRenderTypeModifier(modifier);
        return apply(token);
    }

    public LodestoneRenderType applyWithModifier(RenderTypeToken token, ShaderUniformHandler uniformHandler, Consumer<LodestoneCompositeStateBuilder> modifier) {
        LodestoneRenderTypes.addRenderTypeModifier(modifier);
        return apply(token, uniformHandler);
    }

    public LodestoneRenderType applyWithModifierAndCache(RenderTypeToken token, Consumer<LodestoneCompositeStateBuilder> modifier) {
        LodestoneRenderTypes.addRenderTypeModifier(modifier);
        return applyAndCache(token);
    }

    public LodestoneRenderType applyWithModifierAndCache(RenderTypeToken token, ShaderUniformHandler uniformHandler, Consumer<LodestoneCompositeStateBuilder> modifier) {
        LodestoneRenderTypes.addRenderTypeModifier(modifier);
        return LodestoneRenderTypes.applyUniformChanges(applyAndCache(token), uniformHandler);
    }
}
