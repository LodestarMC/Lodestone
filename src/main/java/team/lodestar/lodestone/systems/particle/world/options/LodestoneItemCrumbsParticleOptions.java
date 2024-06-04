package team.lodestar.lodestone.systems.particle.world.options;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.item.ItemStack;
import team.lodestar.lodestone.systems.particle.world.behaviors.*;

import java.util.function.*;

public class LodestoneItemCrumbsParticleOptions extends WorldParticleOptions {

    public final ItemStack stack;

    public LodestoneItemCrumbsParticleOptions(ParticleType<LodestoneItemCrumbsParticleOptions> type, LodestoneParticleBehavior behavior, ItemStack stack) {
        super(type, behavior);
        this.stack = stack;
    }

    public LodestoneItemCrumbsParticleOptions(ParticleType<LodestoneItemCrumbsParticleOptions> type, ItemStack stack) {
        this(type, null, stack);
    }
}