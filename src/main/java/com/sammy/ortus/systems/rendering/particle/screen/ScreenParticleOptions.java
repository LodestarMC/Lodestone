package com.sammy.ortus.systems.rendering.particle.screen;

import com.sammy.ortus.systems.rendering.particle.SimpleParticleOptions;
import com.sammy.ortus.systems.rendering.particle.screen.base.ScreenParticle;
import net.minecraft.world.item.ItemStack;

public class ScreenParticleOptions extends SimpleParticleOptions {

    public final ScreenParticleType<?> type;
    public ScreenParticle.RenderOrder renderOrder;
    public ItemStack stack;
    public float xOrigin;
    public float yOrigin;
    public float xOffset;
    public float yOffset;
    public ScreenParticleOptions(ScreenParticleType<?> type) {
        this.type = type;
    }
}