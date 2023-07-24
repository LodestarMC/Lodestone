package team.lodestar.lodestone.systems.particle.screen;

import com.ibm.icu.impl.*;
import net.minecraft.nbt.*;
import net.minecraft.world.item.*;

import java.util.*;

public class ScreenParticleItemStackKey {

    public final boolean isHotbarItem;
    public final boolean isRenderedAfterItem;
    public final ItemStack itemStack;

    public ScreenParticleItemStackKey(boolean isHotbarItem, boolean isRenderedAfterItem, ItemStack itemStack) {
        this.isHotbarItem = isHotbarItem;
        this.isRenderedAfterItem = isRenderedAfterItem;
        this.itemStack = itemStack;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ScreenParticleItemStackKey key)) {
            return false;
        }
        return key.isHotbarItem == isHotbarItem && key.isRenderedAfterItem == isRenderedAfterItem && key.itemStack == itemStack;
    }

    @Override
    public int hashCode() {
        return Objects.hash(isHotbarItem, isRenderedAfterItem, itemStack);
    }
}
