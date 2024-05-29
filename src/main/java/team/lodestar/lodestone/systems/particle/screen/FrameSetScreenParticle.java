package team.lodestar.lodestone.systems.particle.screen;

import net.fabricmc.fabric.impl.client.particle.FabricSpriteProviderImpl;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;

import java.util.ArrayList;

public class FrameSetScreenParticle extends GenericScreenParticle {
    public ArrayList<Integer> frameSet = new ArrayList<>();

    public FrameSetScreenParticle(ClientLevel world, ScreenParticleOptions data, FabricSpriteProviderImpl spriteSet, double x, double y, double xMotion, double yMotion) {
        super(world, data, spriteSet, x, y, xMotion, yMotion);
    }

    @Override
    public void tick() {
        setSprite(frameSet.get(age));
        super.tick();
    }

    public void setSprite(int spriteIndex) {
        if (spriteIndex < spriteSet.getSprites().size() && spriteIndex >= 0) {
            setSprite(spriteSet.getSprites().get(spriteIndex));
        }
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