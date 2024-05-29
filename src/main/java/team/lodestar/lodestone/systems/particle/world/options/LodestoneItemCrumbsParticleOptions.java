package team.lodestar.lodestone.systems.particle.world.options;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.item.ItemStack;
import team.lodestar.lodestone.systems.particle.world.behaviors.*;

public class LodestoneItemCrumbsParticleOptions<T extends LodestoneParticleBehavior<T>> extends WorldParticleOptions<T> {

    public final ItemStack stack;

    public LodestoneItemCrumbsParticleOptions(ParticleType<?> type, T behavior, ItemStack stack) {
        super(type, behavior);
        this.stack = stack;
    }

    public LodestoneItemCrumbsParticleOptions(ParticleType<?> type, ItemStack stack) {
        this(type, null, stack);
    }
}