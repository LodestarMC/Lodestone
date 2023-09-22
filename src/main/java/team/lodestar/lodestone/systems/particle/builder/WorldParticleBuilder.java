package team.lodestar.lodestone.systems.particle.builder;

import com.mojang.math.*;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.RegistryObject;
import team.lodestar.lodestone.helpers.BlockHelper;
import team.lodestar.lodestone.systems.particle.world.GenericParticle;
import team.lodestar.lodestone.systems.particle.world.WorldParticleOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class WorldParticleBuilder extends AbstractWorldParticleBuilder<WorldParticleBuilder, WorldParticleOptions> {

    final WorldParticleOptions options;

    public static WorldParticleBuilder create(Supplier<? extends ParticleType<WorldParticleOptions>> type) {
        return new WorldParticleBuilder(type.get());
    }

    protected WorldParticleBuilder(ParticleType<WorldParticleOptions> type) {
        super(type);
        this.options = new WorldParticleOptions(type);
    }

    @Override
    public WorldParticleOptions getParticleOptions() {
        return options;
    }
}