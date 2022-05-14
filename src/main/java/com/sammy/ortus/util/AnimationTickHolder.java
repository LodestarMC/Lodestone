package com.sammy.ortus.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.LevelAccessor;

public class AnimationTickHolder {

    private static int ticks;
    private static int pausedTicks;


    public static void tick() {
        if (!Minecraft.getInstance()
                .isPaused()) {
            ticks = (ticks + 1) % 1_728_000; // wrap around every 24 hours so we maintain enough floating point precision
        } else {
            pausedTicks = (pausedTicks + 1) % 1_728_000;
        }
    }


    public static float getPartialTicks() {
        Minecraft mc = Minecraft.getInstance();
        return (mc.isPaused() ? 1 : mc.getFrameTime());
    }
}