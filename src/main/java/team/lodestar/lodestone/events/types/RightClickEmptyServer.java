package team.lodestar.lodestone.events.types;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import team.lodestar.lodestone.capability.LodestonePlayerDataCapability;

/**
 * Runs on the server when a player initially right-clicks an empty space. For prolonged checks use {@link LodestonePlayerDataCapability#rightClickHeld}
 */
public class RightClickEmptyServer extends PlayerEvent {

    public RightClickEmptyServer(Player player) {
        super(player);
    }

    public static void onRightClickEmptyServer(Player player) {
        RightClickEmptyServer evt = new RightClickEmptyServer(player);
        MinecraftForge.EVENT_BUS.post(evt);
    }
}
