package team.lodestar.lodestone.systems.rendering;

import com.mojang.blaze3d.systems.*;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;

import java.util.*;

import static net.minecraft.client.renderer.RenderType.CompositeRenderType.OUTLINE;

public class LodestoneRenderType extends RenderType {
    public final RenderType.CompositeState state;
    private final Optional<RenderType> outline;
    private final boolean isOutline;

    public static LodestoneRenderType createRenderType(String pName, VertexFormat pFormat, VertexFormat.Mode pMode, int pBufferSize, boolean pAffectsCrumbling, boolean pSortOnUpload, CompositeState pState) {
        return new LodestoneRenderType(pName, pFormat, pMode, pBufferSize, pAffectsCrumbling, pSortOnUpload, pState);
    }

    public LodestoneRenderType(String pName, VertexFormat pFormat, VertexFormat.Mode pMode, int pBufferSize, boolean pAffectsCrumbling, boolean pSortOnUpload, RenderType.CompositeState pState) {
        super(pName, pFormat, pMode, pBufferSize, pAffectsCrumbling, pSortOnUpload, () -> {
            pState.states.forEach(RenderStateShard::setupRenderState);
        }, () -> {
            pState.states.forEach(RenderStateShard::clearRenderState);
        });
        this.state = pState;
        this.outline = pState.outlineProperty == RenderType.OutlineProperty.AFFECTS_OUTLINE ? pState.textureState.cutoutTexture().map((p_173270_) -> OUTLINE.apply(p_173270_, pState.cullState)) : Optional.empty();
        this.isOutline = pState.outlineProperty == RenderType.OutlineProperty.IS_OUTLINE;
    }

    @Override
    public void end(BufferBuilder builder, VertexSorting sorting) {
        if (builder.building()) {
            if (this.sortOnUpload) {
                builder.setQuadSorting(sorting);
            }

            BufferBuilder.RenderedBuffer buffer = builder.end();
            if (buffer.isEmpty()) {
                buffer.release();
                return;
            }

            this.setupRenderState();
            RenderSystem.colorMask(true, true, true, true);
            RenderSystem.depthMask(false);
            BufferUploader.drawWithShader(buffer);
            if (Minecraft.getInstance().levelRenderer.transparencyChain != null) {
                // Set up depth state
                RenderSystem.colorMask(false, false, false, false);
                RenderSystem.depthMask(true);

                // Redraw buffer
                ShaderInstance shader = RenderSystem.getShader();
                shader.apply();
                BufferUploader.lastImmediateBuffer.draw();
                shader.clear();
            }
            this.clearRenderState();
            RenderSystem.colorMask(true, true, true, true);
        }
    }

    public Optional<RenderType> outline() {
        return this.outline;
    }

    public boolean isOutline() {
        return this.isOutline;
    }

    protected final RenderType.CompositeState state() {
        return this.state;
    }

    public String toString() {
        return "RenderType[" + this.name + ":" + this.state + "]";
    }
}
