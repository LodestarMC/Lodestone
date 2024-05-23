package team.lodestar.lodestone.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
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

    public static void playerTick(Player player) {
        LodestoneComponents.LODESTONE_PLAYER_COMPONENT.maybeGet(player).ifPresent(c -> {
            c.rightClickTime = c.rightClickHeld ? c.rightClickTime + 1 : 0;
            c.leftClickTime = c.leftClickHeld ? c.leftClickTime + 1 : 0;
        });
    }

    public static boolean playerJoin(Entity entity, Level level, boolean b) {
        if (entity instanceof ServerPlayer serverPlayer) {
            LodestoneComponents.LODESTONE_PLAYER_COMPONENT.maybeGet(serverPlayer).ifPresent(capability -> capability.hasJoinedBefore = true);
            LodestoneComponents.LODESTONE_PLAYER_COMPONENT.sync(serverPlayer);
        }
        return true;
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
