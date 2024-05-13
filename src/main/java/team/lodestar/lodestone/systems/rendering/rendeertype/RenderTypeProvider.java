package team.lodestar.lodestone.systems.rendering.rendeertype;

import net.minecraft.Util;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypeRegistry.*;
import team.lodestar.lodestone.systems.rendering.*;

import java.util.function.*;

public class RenderTypeProvider {
    private final Function<ResourceLocation, LodestoneRenderType> function;
    private final Function<ResourceLocation, LodestoneRenderType> memorizedFunction;

    public RenderTypeProvider(Function<ResourceLocation, LodestoneRenderType> function) {
        this.function = function;
        this.memorizedFunction = Util.memoize(function);
    }

    public LodestoneRenderType apply(ResourceLocation texture) {
        return function.apply(texture);
    }

    public LodestoneRenderType apply(ResourceLocation texture, ShaderUniformHandler uniformHandler) {
        return LodestoneRenderTypeRegistry.applyUniformChanges(function.apply(texture), uniformHandler);
    }

    public LodestoneRenderType applyAndCache(ResourceLocation texture) {
        return this.memorizedFunction.apply(texture);
    }

    public LodestoneRenderType applyAndCache(ResourceLocation texture, ShaderUniformHandler uniformHandler) {
        return LodestoneRenderTypeRegistry.applyUniformChanges(this.memorizedFunction.apply(texture), uniformHandler);
    }

    public LodestoneRenderType applyWithModifier(ResourceLocation texture, Consumer<LodestoneCompositeStateBuilder> modifier) {
        LodestoneRenderTypeRegistry.addRenderTypeModifier(modifier);
        return apply(texture);
    }

    public LodestoneRenderType applyWithModifier(ResourceLocation texture, ShaderUniformHandler uniformHandler, Consumer<LodestoneCompositeStateBuilder> modifier) {
        LodestoneRenderTypeRegistry.addRenderTypeModifier(modifier);
        return apply(texture, uniformHandler);
    }

    public LodestoneRenderType applyWithModifierAndCache(ResourceLocation texture, Consumer<LodestoneCompositeStateBuilder> modifier) {
        LodestoneRenderTypeRegistry.addRenderTypeModifier(modifier);
        return applyAndCache(texture);
    }
}
