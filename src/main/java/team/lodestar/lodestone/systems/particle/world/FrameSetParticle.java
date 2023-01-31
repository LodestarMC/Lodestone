package team.lodestar.lodestone.systems.particle.world;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import team.lodestar.lodestone.systems.particle.SimpleParticleOptions;

import java.util.ArrayList;

import static team.lodestar.lodestone.systems.particle.SimpleParticleOptions.ParticleSpritePicker.FIRST_INDEX;

public class FrameSetParticle extends GenericParticle {
    public ArrayList<Integer> frameSet = new ArrayList<>();

    public FrameSetParticle(ClientLevel world, WorldParticleOptions data, ParticleEngine.MutableSpriteSet spriteSet, double x, double y, double z, double xd, double yd, double zd) {
        super(world, data, spriteSet, x, y, z, xd, yd, zd);
    }

    @Override
    public void tick() {
        if (age < frameSet.size()) {
            pickSprite(frameSet.get(age));
        }
        super.tick();
    }

    @Override
    public SimpleParticleOptions.ParticleSpritePicker getSpritePicker() {
        return FIRST_INDEX;
    }

    protected void addLoop(int min, int max, int times) {
        for (int i = 0; i < times; i++) {
            addFrames(min, max);
        }
    }

    protected void addFrames(int min, int max) {
        for (int i = min; i <= max; i++) {
            frameSet.add(i);
        }
    }

    protected void insertFrames(int insertIndex, int min, int max) {
        for (int i = min; i <= max; i++) {
            frameSet.add(insertIndex, i);
        }
    }
}