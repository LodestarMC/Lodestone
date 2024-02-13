package team.lodestar.lodestone.systems.rendering;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.renderer.*;
import net.minecraftforge.api.distmarker.*;

import java.util.*;

@OnlyIn(Dist.CLIENT)
   public final class CompositeRenderType extends RenderType {
      public final RenderType.CompositeState state;
      private final Optional<RenderType> outline;
      private final boolean isOutline;

      CompositeRenderType(String pName, VertexFormat pFormat, VertexFormat.Mode pMode, int pBufferSize, boolean pAffectsCrumbling, boolean pSortOnUpload, RenderType.CompositeState pState) {
         super(pName, pFormat, pMode, pBufferSize, pAffectsCrumbling, pSortOnUpload, () -> {
            pState.states.forEach(RenderStateShard::setupRenderState);
         }, () -> {
            pState.states.forEach(RenderStateShard::clearRenderState);
         });
         this.state = pState;
         this.outline = pState.outlineProperty == RenderType.OutlineProperty.AFFECTS_OUTLINE ? pState.textureState.cutoutTexture().map((p_173270_) -> {
            return OUTLINE.apply(p_173270_, pState.cullState);
         }) : Optional.empty();
         this.isOutline = pState.outlineProperty == RenderType.OutlineProperty.IS_OUTLINE;
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