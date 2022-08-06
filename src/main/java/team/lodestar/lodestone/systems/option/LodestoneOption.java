package team.lodestar.lodestone.systems.option;

import net.minecraftforge.client.event.ScreenEvent;

public interface LodestoneOption {

    boolean canAdd(ScreenEvent.InitScreenEvent.Post event);
}
