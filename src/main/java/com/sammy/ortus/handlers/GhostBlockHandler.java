package com.sammy.ortus.handlers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.sammy.ortus.systems.rendering.ghost.GhostBlockOptions;
import com.sammy.ortus.systems.rendering.ghost.GhostBlockRenderer;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class GhostBlockHandler {
    public static final Map<Object, GhostEntry> GHOSTS = new HashMap<>();

    public static GhostBlockOptions addGhost(Object slot, BlockState state) {
        return addGhost(slot, state, 1);
    }

    public static GhostBlockOptions addGhost(Object slot, BlockState state, int timeLeft) {
        GhostEntry ghostEntry = addGhost(slot, GhostBlockRenderer.TRANSPARENT, GhostBlockOptions.create(state), timeLeft);
        return ghostEntry.params;
    }

    public static GhostEntry addGhost(Object slot, GhostBlockRenderer ghost, GhostBlockOptions params, int timeLeft) {
        if (!GHOSTS.containsKey(slot)) {
            GHOSTS.put(slot, new GhostEntry(ghost, params, timeLeft));
        }
        GhostEntry ghostEntry = GHOSTS.get(slot);
        ghostEntry.timeLeft = timeLeft;
        ghostEntry.params = params;
        ghostEntry.ghost = ghost;
        return ghostEntry;
    }

    public static void renderGhosts(PoseStack ps) {
        GHOSTS.forEach((slot, ghostEntry) -> {
            GhostBlockRenderer ghost = ghostEntry.ghost;
            ghost.render(ps, ghostEntry.params);
        });
    }

    public static void tickGhosts() {
        Iterator<Map.Entry<Object, GhostEntry>> iterator = GHOSTS.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Object, GhostEntry> next = iterator.next();
            GhostEntry entry = next.getValue();
            if (entry.timeLeft <= 0) {
                iterator.remove();
            } else {
                entry.timeLeft--;
                iterator.next();
            }
        }
    }

    static class GhostEntry {
        private GhostBlockRenderer ghost;
        private GhostBlockOptions params;
        private int timeLeft;

        public GhostEntry(GhostBlockRenderer ghost, GhostBlockOptions params, int timeLeft) {
            this.ghost = ghost;
            this.params = params;
            this.timeLeft = timeLeft;
        }
    }
}