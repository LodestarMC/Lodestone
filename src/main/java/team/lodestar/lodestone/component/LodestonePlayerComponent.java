package team.lodestar.lodestone.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import team.lodestar.lodestone.network.interaction.UpdateLeftClickPacket;
import team.lodestar.lodestone.network.interaction.UpdateRightClickPacket;
import team.lodestar.lodestone.registry.common.LodestonePacketRegistry;

public class LodestonePlayerComponent implements AutoSyncedComponent {
    private final Player player;

    public boolean hasJoinedBefore;
    public boolean rightClickHeld;
    public int rightClickTime;
    public boolean leftClickHeld;
    public int leftClickTime;

    public LodestonePlayerComponent(Player player) {
        this.player = player;
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        hasJoinedBefore = tag.getBoolean("firstTimeJoin");

    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        tag.putBoolean("firstTimeJoin", hasJoinedBefore);
    }

    @Environment(EnvType.CLIENT)
    public static void clientTick(Minecraft minecraft) {
        Player player = minecraft.player;
        LodestoneComponents.LODESTONE_PLAYER_COMPONENT.maybeGet(player).ifPresent(c -> {
            boolean left = minecraft.options.keyAttack.isDown();
            boolean right = minecraft.options.keyUse.isDown();
            if (left != c.leftClickHeld) {
                c.leftClickHeld = left;
                LodestonePacketRegistry.LODESTONE_CHANNEL.sendToServer(new UpdateLeftClickPacket(c.leftClickHeld));
            }
            if (right != c.rightClickHeld) {
                c.rightClickHeld = right;
                LodestonePacketRegistry.LODESTONE_CHANNEL.sendToServer(new UpdateRightClickPacket(c.rightClickHeld));
            }
        });
    }
}
