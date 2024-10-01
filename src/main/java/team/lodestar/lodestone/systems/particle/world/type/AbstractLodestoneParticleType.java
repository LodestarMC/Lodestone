package team.lodestar.lodestone.systems.particle.world.type;

import net.minecraft.core.particles.*;
import team.lodestar.lodestone.systems.particle.world.options.*;

public abstract class AbstractLodestoneParticleType<T extends WorldParticleOptions> extends ParticleType<T> {

    protected AbstractLodestoneParticleType() {
        super(false);
    }

    public AbstractLodestoneParticleType<T> getType() {
        return this;
    }
}