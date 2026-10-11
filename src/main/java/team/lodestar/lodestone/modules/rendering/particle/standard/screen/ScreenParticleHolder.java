package team.lodestar.lodestone.modules.rendering.particle.standard.screen;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import team.lodestar.lodestone.modules.rendering.particle.standard.render_types.LodestoneScreenParticleRenderType;
import team.lodestar.lodestone.modules.rendering.particle.standard.screen.base.ScreenParticle;

import javax.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ScreenParticleHolder {

    protected static final Tesselator TESSELATOR = new Tesselator();

    protected final Map<LodestoneScreenParticleRenderType, ArrayList<ScreenParticle>> particles = new HashMap<>();

    public ScreenParticleHolder() {
    }

    public void tick() {
        for (ArrayList<ScreenParticle> particles : particles.values()) {
            var iterator = particles.iterator();
            while (iterator.hasNext()) {
                var particle = iterator.next();
                particle.tick();
                if (!particle.isAlive()) {
                    iterator.remove();
                }
            }
        }
    }

    public void render() {
        render((PoseStack) null);
    }

    public void render(GuiGraphics graphics) {
        render(graphics.pose());
    }

    public void render(@Nullable PoseStack poseStack) {
        particles.forEach((renderType, particles) -> {
            if (particles.isEmpty()) {
                return;
            }
            var builder = renderType.begin(TESSELATOR, Minecraft.getInstance().getTextureManager());
            for (ScreenParticle next : particles) {
                next.render(builder, poseStack);
            }
            renderType.end(builder);
        });
    }

    public void addFrom(ScreenParticleHolder otherHolder) {
        particles.putAll(otherHolder.particles);
    }

    public boolean isEmpty() {
        return particles.values().stream().allMatch(ArrayList::isEmpty);
    }

    public void clear() {
        particles.values().forEach(ArrayList::clear);
    }
}