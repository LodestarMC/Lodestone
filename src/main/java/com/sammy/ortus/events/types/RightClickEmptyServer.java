package com.sammy.ortus.events.types;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class RightClickEmptyServer extends PlayerEvent {

    public RightClickEmptyServer(Player player) {
        super(player);
    }

    public static void onRightClickEmptyServer(Player player) {
        RightClickEmptyServer evt = new RightClickEmptyServer(player);
        MinecraftForge.EVENT_BUS.post(evt);
    }
}
