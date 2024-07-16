package team.lodestar.lodestone.systems.client;

import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.client.event.RenderFrameEvent;

public class ClientTickCounter {
    public static long ticksInGame = 0L;
    public static float partialTicks = 0.0F;

    public static float getTotal() {
        return (float) ticksInGame + partialTicks;
    }

    public static void renderTick(RenderFrameEvent event) {
        partialTicks = event.getPartialTick().getGameTimeDeltaPartialTick(false);
    }

    public static void clientTick() {
        if (!Minecraft.getInstance().isPaused()) {
            ++ticksInGame;
            partialTicks = 0.0F;
        }
    }
}