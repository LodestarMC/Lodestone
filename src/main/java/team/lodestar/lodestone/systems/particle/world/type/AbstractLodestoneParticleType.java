package team.lodestar.lodestone.systems.particle.world.type;

import com.mojang.brigadier.*;
import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.*;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import team.lodestar.lodestone.systems.particle.world.*;
import team.lodestar.lodestone.systems.particle.world.behaviors.*;
import team.lodestar.lodestone.systems.particle.world.options.*;

import javax.annotation.*;

public abstract class AbstractLodestoneParticleType<T extends WorldParticleOptions> extends ParticleType<T> {

    protected AbstractLodestoneParticleType() {
        super(false);
    }

    public AbstractLodestoneParticleType<T> getType() {
        return this;
    }
}