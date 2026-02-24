package team.lodestar.lodestone.systems.rendering.buffer;

import team.lodestar.lodestone.handlers.rendering.LodestoneRenderHandler;

public class LodestoneRenderLayer {

    protected final LodestoneBufferSource target;
    protected final LodestoneBufferSource particleTarget;
    protected final LodestoneBufferSource trailTarget;

    public LodestoneRenderLayer() {
        this.target = new LodestoneBufferSource();
        this.particleTarget = new LodestoneBufferSource();
        this.trailTarget = new LodestoneBufferSource();
    }

    public void endBatches() {
        particleTarget.endBatches(false);
        target.endBatches(false);

        //Trails are already mapped to screen coordinates by lodestone, we clear the model view matrix here to prevent messing things up
        LodestoneRenderHandler.clearModelViewMatrix();

        trailTarget.endBatches(false);
        trailTarget.endBatches(true);

        LodestoneRenderHandler.restoreModelViewMatrix();

        target.endBatches(true);
        particleTarget.endBatches(true);
   }

    public LodestoneBufferSource getTarget() {
        return target;
    }

    public LodestoneBufferSource getParticleTarget() {
        return particleTarget;
    }

    public LodestoneBufferSource getTrailTarget() {
        return trailTarget;
    }
}