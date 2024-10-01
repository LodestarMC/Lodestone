package team.lodestar.lodestone.events.types;

import net.minecraft.world.entity.player.Player;

/**
 * Runs on the server when a player initially right-clicks an empty space. For prolonged checks use {@link LodestonePlayerDataAttachment#rightClickHeld}
 */
public class RightClickEmptyServer extends PlayerEvent {

    public RightClickEmptyServer(Player player) {
        super(player);
    }

    public static void onRightClickEmptyServer(Player player) {
        RightClickEmptyServer evt = new RightClickEmptyServer(player);
        NeoForge.EVENT_BUS.post(evt);
    }
}
