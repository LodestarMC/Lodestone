package team.lodestar.lodestone.systems.particle.world.behaviors.components;

import net.minecraft.world.phys.*;
import team.lodestar.lodestone.systems.particle.data.*;
import team.lodestar.lodestone.systems.particle.data.spin.*;
import team.lodestar.lodestone.systems.particle.world.*;

public class BedrockDirectionalBehaviorComponent extends DirectionalBehaviorComponent {

    public final SpinParticleData pitchData;
    public final SpinParticleData yawData;

    public float pitch;
    public float yaw;

    public BedrockDirectionalBehaviorComponent(SpinParticleData pitchData, SpinParticleData yawData) {
        this.pitchData = pitchData;
        this.yawData = yawData;
        this.pitch = pitchData.spinOffset + pitchData.startingValue;
        this.yaw = yawData.spinOffset + yawData.startingValue;
    }

    public BedrockDirectionalBehaviorComponent(SpinParticleData data) {
        this(data, data);
    }

    @Override
    public Vec3 getDirection(LodestoneWorldParticle particle) {
        float x = (float) (Math.cos(pitch) * Math.cos(yaw));
        float y = (float) Math.sin(pitch);
        float z = (float) (Math.cos(pitch) * Math.sin(yaw));
        return new Vec3(x, y, z).normalize();
    }

    @Override
    public void tick(LodestoneWorldParticle particle) {
        pitch += pitchData.getValue(particle.getAge(), particle.getLifetime());
        yaw += yawData.getValue(particle.getAge(), particle.getLifetime());
    }
}