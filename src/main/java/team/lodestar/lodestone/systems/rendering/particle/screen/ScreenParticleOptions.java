package team.lodestar.lodestone.systems.rendering.particle.screen;

import team.lodestar.lodestone.handlers.screenparticle.ScreenParticleHandler;
import team.lodestar.lodestone.systems.rendering.particle.SimpleParticleOptions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import team.lodestar.lodestone.systems.rendering.particle.screen.base.ScreenParticle;

import java.util.ArrayList;
import java.util.HashMap;

public class ScreenParticleOptions extends SimpleParticleOptions {

    public final ScreenParticleType<?> type;
    public ItemStack stack;
    public float xOrigin;
    public float yOrigin;
    public float xOffset;
    public float yOffset;
    public ScreenParticleRenderType renderType = ScreenParticleRenderType.ADDITIVE;

    public Vec2 startingMotion = Vec2.ZERO, endingMotion = Vec2.ZERO;

    public ScreenParticleOptions(ScreenParticleType<?> type) {
        this.type = type;
    }
}