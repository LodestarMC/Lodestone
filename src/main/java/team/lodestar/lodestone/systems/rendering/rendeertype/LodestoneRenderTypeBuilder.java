package team.lodestar.lodestone.systems.rendering.rendeertype;

import team.lodestar.lodestone.registry.client.*;
import team.lodestar.lodestone.systems.rendering.uniform.UniformData;

import java.util.*;
import java.util.function.*;

public class LodestoneRenderTypeBuilder {
    private final RenderTypeProvider provider;
    private RenderTypeToken token;

    public LodestoneRenderTypeBuilder(RenderTypeProvider provider, RenderTypeToken token) {
        this.provider = provider;
        this.token = token;
    }

    public LodestoneRenderTypeBuilder addUniformData(UniformData uniformData) {
        token = token.addUniformData(uniformData);
        return this;
    }

    public LodestoneRenderTypeBuilder addModifier(Consumer<LodestoneRenderTypes.LodestoneCompositeStateBuilder> modifier) {
        token = token.addModifier(modifier);
        return this;
    }

    public LodestoneRenderType getRenderType() {
        return provider.createRenderType(token);
    }

    public RenderTypeProvider getProvider() {
        return provider;
    }

    public RenderTypeToken getToken() {
        return token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LodestoneRenderTypeBuilder that = (LodestoneRenderTypeBuilder) o;
        return Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }
}
