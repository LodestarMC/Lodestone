package team.lodestar.lodestone.systems.rendering.particle.screen;

import team.lodestar.lodestone.systems.rendering.particle.screen.base.ScreenParticle;
import team.lodestar.lodestone.systems.rendering.particle.SimpleParticleOptions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;

public class ScreenParticleOptions extends SimpleParticleOptions {

    public final ScreenParticleType<?> type;
    public ScreenParticle.RenderOrder renderOrder;
    public ItemStack stack;
    public float xOrigin;
    public float yOrigin;
    public float xOffset;
    public float yOffset;

    public Vec2 startingMotion = Vec2.ZERO, endingMotion = Vec2.ZERO;

    public ScreenParticleOptions(ScreenParticleType<?> type) {
        this.type = type;
    }
}