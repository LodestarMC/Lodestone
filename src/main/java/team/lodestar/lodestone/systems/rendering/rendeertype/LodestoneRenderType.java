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

    // Constructors for copying and modifying render types
    // They are a bit ugly but alas
    protected LodestoneRenderType(String name, LodestoneRenderType original) {
        this(name, original.format, original.mode, original.bufferSize, original.affectsCrumbling, original.sortOnUpload, original.state, original.uniformData);
    }

    protected LodestoneRenderType(String name, LodestoneRenderType original, UniformData uniformData) {
        this(name, original.format, original.mode, original.bufferSize, original.affectsCrumbling, original.sortOnUpload, original.state, uniformData);
    }

    protected LodestoneRenderType(String name, LodestoneRenderType original, UniformData uniformData, Consumer<LodestoneCompositeStateBuilder> modifier) {
        this(name, original.format, original.mode, original.bufferSize, original.affectsCrumbling, original.sortOnUpload, LodestoneRenderTypes.builder(original.state).accepts(modifier).createCompositeState(), uniformData);
    }

    public LodestoneRenderType copy(Object key) {
        if (!copies.containsKey(key)) {
            copies.put(key, new LodestoneRenderType(name, this));
        }
        return copies.get(key);
    }

    public LodestoneRenderType copyAndModify(Object key, UniformData uniformHandler) {
        if (!copies.containsKey(key)) {
            copies.put(key, new LodestoneRenderType(name, this, uniformHandler));
        }
        return copies.get(key);
    }

    public LodestoneRenderType copyAndModify(Object key, UniformData uniformHandler, Consumer<LodestoneCompositeStateBuilder> modifier) {
        if (!copies.containsKey(key)) {
            copies.put(key, new LodestoneRenderType(name, this, uniformHandler, modifier));
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