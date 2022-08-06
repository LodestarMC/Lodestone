package team.lodestar.lodestone.systems.rendering.outline;

import java.util.*;

import com.mojang.blaze3d.vertex.PoseStack;
import team.lodestar.lodestone.systems.rendering.outline.Outline.OutlineParams;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class Outliner {

    private final Map<Object, OutlineEntry> outlines = Collections.synchronizedMap(new HashMap<>());

    // Facade


    public OutlineParams showLine(Object slot, Vec3 start, Vec3 end) {
        if (!outlines.containsKey(slot)) {
            LineOutline outline = new LineOutline();
            outlines.put(slot, new OutlineEntry(outline));
        }
        OutlineEntry entry = outlines.get(slot);
        entry.ticksTillRemoval = 1;
        ((LineOutline) entry.outline).set(start, end);
        return entry.outline.getParams();
    }


    public void renderOutlines(PoseStack ms, float pt) {
        outlines.forEach((key, entry) -> {
            Outline outline = entry.getOutline();
            OutlineParams params = outline.getParams();
            params.alpha = 1;
            if (entry.isFading()) {
                int prevTicks = entry.ticksTillRemoval + 1;
                float fadeticks = OutlineEntry.fadeTicks;
                float lastAlpha = prevTicks >= 0 ? 1 : 1 + (prevTicks / fadeticks);
                float currentAlpha = 1 + (entry.ticksTillRemoval / fadeticks);
                float alpha = Mth.lerp(pt, lastAlpha, currentAlpha);

                params.alpha = alpha * alpha * alpha;
                if (params.alpha < 1 / 8f)
                    return;
            }
            outline.render(ms, pt);
        });
    }

    public static class OutlineEntry {

        static final int fadeTicks = 8;
        private Outline outline;
        private int ticksTillRemoval;

        public OutlineEntry(Outline outline) {
            this.outline = outline;
            ticksTillRemoval = 1;
        }

        public void tick() {
            ticksTillRemoval--;
            outline.tick();
        }


        public boolean isFading() {
            return ticksTillRemoval < 0;
        }

        public Outline getOutline() {
            return outline;
        }

    }

}
