package team.lodestar.lodestone.systems.particle.screen;

import team.lodestar.lodestone.systems.particle.render_types.LodestoneScreenParticleRenderType;
import team.lodestar.lodestone.systems.particle.screen.base.ScreenParticle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ScreenParticleHolder {

    public final Map<LodestoneScreenParticleRenderType, ArrayList<ScreenParticle>> particles = new HashMap<>();

    public ScreenParticleHolder() {
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

    public void addFrom(ScreenParticleHolder otherHolder) {
        particles.putAll(otherHolder.particles);
    }

    public boolean isEmpty() {
        return particles.values().stream().allMatch(ArrayList::isEmpty);
    }
}
