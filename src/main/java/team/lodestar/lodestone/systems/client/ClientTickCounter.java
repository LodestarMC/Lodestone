package team.lodestar.lodestone.systems.client;

import net.minecraft.client.Minecraft;

public class ClientTickCounter {
    public static long ticksInGame = 0L;
    public static float partialTicks = 0.0F;

    public static float getTotal() {
        return (float) ticksInGame + partialTicks;
    }

    public static void renderTick(TickEvent.RenderTickEvent event) {
        if (event.phase.equals(TickEvent.Phase.START)) {
            partialTicks = event.renderTickTime;
        }
    }

    public static void clientTick() {
        if (!Minecraft.getInstance().isPaused()) {
            ++ticksInGame;
            partialTicks = 0.0F;
        }
    }
}