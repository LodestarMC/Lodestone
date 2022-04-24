package com.sammy.ortus.systems.option;

import net.minecraftforge.client.event.ScreenEvent;

public interface OrtusOption {

    boolean canAdd(ScreenEvent.InitScreenEvent.Post event);
}
