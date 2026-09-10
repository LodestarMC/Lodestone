package team.lodestar.lodestone.systems.rendering.rendeertype;

import com.mojang.blaze3d.systems.*;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.*;
import team.lodestar.lodestone.registry.client.*;
import team.lodestar.lodestone.registry.client.LodestoneRenderTypes.*;
import team.lodestar.lodestone.systems.rendering.StateShards;
import team.lodestar.lodestone.systems.rendering.uniform.UniformData;

import javax.annotation.*;
import java.util.*;
import java.util.function.*;

import static net.minecraft.client.renderer.RenderType.CompositeRenderType.OUTLINE;

public class LodestoneRenderType extends RenderType {

    private final HashMap<Object, LodestoneRenderType> copies = new HashMap<>();

    public final RenderType.CompositeState state;
    private final RenderType outline;
    private final boolean isOutline;

    private final boolean isAdditive;

    private final UniformData uniformData;

    public LodestoneRenderType(String pName, VertexFormat pFormat, VertexFormat.Mode pMode, int pBufferSize, boolean pAffectsCrumbling, boolean pSortOnUpload, RenderType.CompositeState pState, @Nullable UniformData uniformData) {
        super(pName, pFormat, pMode, pBufferSize, pAffectsCrumbling, pSortOnUpload,
                () -> pState.states.forEach(RenderStateShard::setupRenderState),
                () -> pState.states.forEach(RenderStateShard::clearRenderState));
        this.state = pState;
        this.outline = pState.outlineProperty == RenderType.OutlineProperty.AFFECTS_OUTLINE ? pState.textureState.cutoutTexture().map((p_173270_) -> OUTLINE.apply(p_173270_, pState.cullState)).orElse(null) : null;
        this.isOutline = pState.outlineProperty == RenderType.OutlineProperty.IS_OUTLINE;
        this.isAdditive = isAdditive(this);
        this.uniformData = uniformData;
    }

    public LodestoneRenderType copy(Object key) {
        return copy(key, null);
    }

    public LodestoneRenderType copy(Object key, UniformData addedData) {
        return copy(key, addedData, null);
    }

    public LodestoneRenderType copy(Object key, UniformData addedData, Consumer<LodestoneCompositeStateBuilder> modifier) {
        if (!copies.containsKey(key)) {
            var builder = LodestoneRenderTypes.builder(state).accept(modifier);
            var data = addedData.fuse(uniformData);
            var copy = new LodestoneRenderType(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, builder.createCompositeState(), data);
            copies.put(key, copy);
        }
        return copies.get(key);
    }

    public static boolean isAdditive(LodestoneRenderType renderType) {
        return renderType.state.transparencyState.equals(StateShards.ADDITIVE_TRANSPARENCY) || renderType.state.transparencyState.equals(ADDITIVE_TRANSPARENCY);
    }

    public @Nullable UniformData getUniformData() {
        return uniformData;
    }

    @Override
    public Optional<RenderType> outline() {
        return Optional.ofNullable(outline);
    }

    @Override
    public boolean isOutline() {
        return isOutline;
    }

    @Override
    public String toString() {
        return "RenderType[" + this.name + ":" + this.state + "]";
    }

    @Override
    public void draw(MeshData meshData) {
        //I think there's a better way to do this, but this makes sure our depth writing is correct
        RenderSystem.depthMask(state.writeMaskState.writeDepth);
        this.setupRenderState();
        BufferUploader.drawWithShader(meshData);
        this.clearRenderState();
    }

    public boolean isAdditive() {
        return isAdditive;
    }
}