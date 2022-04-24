package com.sammy.ortus.events.types;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class RightClickEmptyServer extends PlayerEvent {

    public RightClickEmptyServer(Player player) {
        super(player);
    }

    public static void onRightClickEmptyServer(Player player) {
        RightClickEmptyServer evt = new RightClickEmptyServer(player);
        MinecraftForge.EVENT_BUS.post(evt);
    }
}
