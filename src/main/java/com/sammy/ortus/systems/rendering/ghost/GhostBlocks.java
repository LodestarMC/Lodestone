package com.sammy.ortus.systems.rendering.ghost;

import com.mojang.blaze3d.vertex.PoseStack;
import com.sammy.ortus.systems.rendering.SuperRenderTypeBuffer;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class GhostBlocks {

    public GhostBlocks() {
        ghosts = new HashMap<>();
    }

    final Map<Object, Entry> ghosts;

    public GhostBlockParams showGhostState(Object slot, BlockState state) {
        return showGhostState(slot, state, 1);
    }
    public GhostBlockParams showGhostState(Object slot, BlockState state, int ttl) {
        Entry entry = refresh(slot, GhostBlockRenderer.transparent(), GhostBlockParams.create(state), ttl);
        return entry.params;
    }

    private Entry refresh(Object slot, GhostBlockRenderer ghost, GhostBlockParams params, int ttl) {
        if (!ghosts.containsKey(slot)) ghosts.put(slot, new Entry(ghost, params, ttl));

        Entry entry = ghosts.get(slot);
        entry.ticksToLive = ttl;
        entry.params = params;
        entry.ghost = ghost;
        return entry;
    }

    public void renderAll(PoseStack ps, SuperRenderTypeBuffer buffer) {
        ghosts.forEach((slot, entry) -> {
            GhostBlockRenderer ghost = entry.ghost;
            ghost.render(ps, buffer, entry.params);
        });
    }

    static class Entry {
        private GhostBlockRenderer ghost;
        private GhostBlockParams params;
        private int ticksToLive;


        public Entry(GhostBlockRenderer ghost, GhostBlockParams params, int ticksToLive){
            this.ghost = ghost;
            this.params = params;
            this.ticksToLive = ticksToLive;
        }

    }

}
