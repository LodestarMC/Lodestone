package team.lodestar.lodestone.handlers;

import com.mojang.blaze3d.vertex.PoseStack;
import team.lodestar.lodestone.systems.rendering.ghost.GhostBlockOptions;
import team.lodestar.lodestone.systems.rendering.ghost.GhostBlockRenderer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Ghost blocks are a means of displaying holograms or previews of blockstates in the world.
 * To add a ghostblock, simply call {@link GhostBlockHandler#addGhost(Object, GhostBlockRenderer, GhostBlockOptions, int)}
 * Ghost blocks naturally decay after the given time limit
 */
public class GhostBlockHandler {
    public static final Map<Object, GhostBlockEntry> GHOSTS = new HashMap<>();

    /**
     * Places a ghost block into the world, only one ghost can exist in a given slot
     *
     * @param slot     A key for the ghost to occupy
     * @param renderer A renderer that handles how the ghost block is rendered into the world
     * @param options  A modular config parameter for the ghost block, allows you to define things like scale, transparency, and even render type
     * @param timeLeft Determines How long will the ghost block remain in the world
     * @return The ghost block placed into the world
     */
    public static GhostBlockEntry addGhost(Object slot, GhostBlockRenderer renderer, GhostBlockOptions options, int timeLeft) {
        if (!GHOSTS.containsKey(slot)) {
            GHOSTS.put(slot, new GhostBlockEntry(renderer, options, timeLeft));
        }
        GhostBlockEntry ghostBlockEntry = GHOSTS.get(slot);
        ghostBlockEntry.timeLeft = timeLeft;
        ghostBlockEntry.options = options;
        ghostBlockEntry.ghost = renderer;
        return ghostBlockEntry;
    }


    public static void renderGhosts(PoseStack poseStack) {
        GHOSTS.forEach((slot, ghostBlockEntry) -> {
            GhostBlockRenderer ghost = ghostBlockEntry.ghost;
            ghost.render(poseStack, ghostBlockEntry.options);
        });
    }

    public static void tickGhosts() {
        Iterator<Map.Entry<Object, GhostBlockEntry>> iterator = GHOSTS.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Object, GhostBlockEntry> next = iterator.next();
            GhostBlockEntry entry = next.getValue();
            if (entry.timeLeft <= 0) {
                iterator.remove();
            } else {
                entry.timeLeft--;
            }
        }
    }

    static class GhostBlockEntry {
        private GhostBlockRenderer ghost;
        private GhostBlockOptions options;
        private int timeLeft;

        public GhostBlockEntry(GhostBlockRenderer ghost, GhostBlockOptions options, int timeLeft) {
            this.ghost = ghost;
            this.options = options;
            this.timeLeft = timeLeft;
        }
    }
}