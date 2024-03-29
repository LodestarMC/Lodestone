package team.lodestar.lodestone.handlers;

import team.lodestar.lodestone.systems.rendering.ghost.GhostBlockOptions;
import team.lodestar.lodestone.systems.rendering.ghost.GhostBlockRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class GhostBlockHandler {
    public static final Map<Object, GhostEntry> GHOSTS = new HashMap<>();

    public static GhostBlockOptions addGhost(Object slot, BlockState state) {
        return addGhost(slot, state, 0);
    }

    public static GhostBlockOptions addGhost(Object slot, BlockState state, int timeLeft) {
        GhostEntry ghostEntry = addGhost(slot, GhostBlockRenderer.TRANSPARENT, GhostBlockOptions.create(state), timeLeft);
        return ghostEntry.options;
    }

    public static GhostEntry addGhost(Object slot, GhostBlockRenderer ghost, GhostBlockOptions options, int timeLeft) {
        if (!GHOSTS.containsKey(slot)) {
            GHOSTS.put(slot, new GhostEntry(ghost, options, timeLeft));
        }
        GhostEntry ghostEntry = GHOSTS.get(slot);
        ghostEntry.timeLeft = timeLeft;
        ghostEntry.options = options;
        ghostEntry.ghost = ghost;
        return ghostEntry;
    }

    public static void renderGhosts(PoseStack ps) {
        GHOSTS.forEach((slot, ghostEntry) -> {
            GhostBlockRenderer ghost = ghostEntry.ghost;
            ghost.render(ps, ghostEntry.options);
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
            }
        }
    }

    static class GhostEntry {
        private GhostBlockRenderer ghost;
        private GhostBlockOptions options;
        private int timeLeft;

        public GhostEntry(GhostBlockRenderer ghost, GhostBlockOptions options, int timeLeft) {
            this.ghost = ghost;
            this.options = options;
            this.timeLeft = timeLeft;
        }
    }
}