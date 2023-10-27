package team.lodestar.lodestone.systems.particle.builder;

import net.minecraft.core.*;
import net.minecraft.core.particles.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.*;
import team.lodestar.lodestone.systems.particle.options.*;

import java.util.function.*;

public class ItemCrumbParticleBuilder extends AbstractWorldParticleBuilder<ItemCrumbParticleBuilder, LodestoneItemCrumbsParticleOptions> {

    final LodestoneItemCrumbsParticleOptions options;

    public static ItemCrumbParticleBuilder create(Supplier<? extends ParticleType<LodestoneItemCrumbsParticleOptions>> type, ItemStack stack) {
        return new ItemCrumbParticleBuilder(type.get(), stack);
    }

    protected ItemCrumbParticleBuilder(ParticleType<LodestoneItemCrumbsParticleOptions> type, ItemStack stack) {
        super(type);
        this.options = new LodestoneItemCrumbsParticleOptions(type, stack);
    }

    @Override
    public LodestoneItemCrumbsParticleOptions getParticleOptions() {
        return options;
    }
}