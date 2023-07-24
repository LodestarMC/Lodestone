package team.lodestar.lodestone.systems.particle.screen;

import net.minecraft.nbt.*;
import net.minecraft.world.item.*;

public class ScreenParticleItemStackKey {

    public final boolean isHotbarItem;
    public final boolean isRenderedAfterItem;
    public final ItemStack itemStack;

    public ScreenParticleItemStackKey(boolean isHotbarItem, boolean isRenderedAfterItem, ItemStack itemStack) {
        this.isHotbarItem = isHotbarItem;
        this.isRenderedAfterItem = isRenderedAfterItem;
        this.itemStack = itemStack;
    }
}
