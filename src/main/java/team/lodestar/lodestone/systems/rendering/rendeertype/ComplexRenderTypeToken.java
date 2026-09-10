package team.lodestar.lodestone.systems.rendering.rendeertype;

import team.lodestar.lodestone.registry.client.*;
import team.lodestar.lodestone.systems.rendering.uniform.UniformData;

import java.util.*;
import java.util.function.*;

public class ComplexRenderTypeToken extends RenderTypeToken {

    private UniformData uniformData;
    private Consumer<LodestoneRenderTypes.LodestoneCompositeStateBuilder> modifier;

    public ComplexRenderTypeToken(RenderTypeToken token) {
        super(token.getIdentifier(), token.getTexture());
    }

    @Override
    public ComplexRenderTypeToken addUniformData(UniformData data) {
        this.uniformData = data;
        return this;
    }

    @Override
    public ComplexRenderTypeToken addModifier(Consumer<LodestoneRenderTypes.LodestoneCompositeStateBuilder> modifier) {
        this.modifier = modifier;
        return this;
    }

    @Override
    protected RenderTypeToken unique() {
        return new ComplexRenderTypeToken(this).addUniformData(uniformData).addModifier(modifier);
    }

    public UniformData getUniformData() {
        return uniformData;
    }

    public Consumer<LodestoneRenderTypes.LodestoneCompositeStateBuilder> getModifier() {
        return modifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ComplexRenderTypeToken that = (ComplexRenderTypeToken) o;
        boolean equalUniform = Objects.equals(uniformData, that.uniformData);
        boolean equalModifier = Objects.equals(modifier, that.modifier);
        return equalUniform && equalModifier;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), uniformData, modifier);
    }
}