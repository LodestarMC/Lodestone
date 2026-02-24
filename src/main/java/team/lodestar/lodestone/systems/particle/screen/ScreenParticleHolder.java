package team.lodestar.lodestone.systems.particle.screen;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import team.lodestar.lodestone.common.config.*;
import team.lodestar.lodestone.systems.particle.render_types.*;
import team.lodestar.lodestone.systems.particle.screen.base.*;

import javax.annotation.*;
import java.util.*;

public class ScreenParticleHolder {

    public static final Tesselator TESSELATOR = new Tesselator();

    protected final Map<LodestoneScreenParticleRenderType, ArrayList<ScreenParticle>> particles = new HashMap<>();

    public ScreenParticleHolder() {
    }

    public ScreenParticle addParticle(ScreenParticleOptions options, double x, double y, double xMotion, double yMotion) {
        var minecraft = Minecraft.getInstance();
        var type = options.type;
        var particle = type.provider.createParticle(minecraft.level, options, x, y, xMotion, yMotion);
        var list = getParticles(options.renderType);
        list.add(particle);
        return particle;
    }

    public ArrayList<ScreenParticle> getParticles(LodestoneScreenParticleRenderType renderType) {
        if (!particles.containsKey(renderType)) {
            particles.put(renderType, new ArrayList<>());
        }
        return particles.get(renderType);
    }

    public void clear() {
        particles.forEach((k, v) -> v.clear());
    }

    public void tick() {
        particles.forEach((pair, particles) -> {
            Iterator<ScreenParticle> iterator = particles.iterator();
            while (iterator.hasNext()) {
                ScreenParticle particle = iterator.next();
                particle.tick();
                if (!particle.isAlive()) {
                    iterator.remove();
                }
            }
        });
    }

    public void render() {
        render((PoseStack) null);
    }

    public void render(GuiGraphics graphics) {
        render(graphics.pose());
    }

    public void render(@Nullable PoseStack poseStack) {
        if (!ClientConfig.ENABLE_SCREEN_PARTICLES.getConfigValue()) {
            return;
        }
        var textureManager = Minecraft.getInstance().getTextureManager();
        particles.forEach((renderType, particles) -> {
            if (!particles.isEmpty()) {
                var builder = renderType.begin(TESSELATOR, textureManager);
                for (ScreenParticle next : particles) {
                    next.render(builder, poseStack);
                }
                renderType.end(builder);
            }
        });
    }

    public void addFrom(ScreenParticleHolder otherHolder) {
        particles.putAll(otherHolder.particles);
    }

    public boolean isEmpty() {
        return particles.values().stream().allMatch(ArrayList::isEmpty);
    }

}
