package team.lodestar.lodestone.systems.particle.screen;

import net.minecraft.world.item.ItemStack;

public record ScreenParticleItemStackKey(boolean isHotbarItem, boolean isRenderedAfterItem, ItemStack itemStack) {

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ScreenParticleItemStackKey key)) {
            return false;
        }
        return key.isHotbarItem == isHotbarItem && key.isRenderedAfterItem == isRenderedAfterItem && key.itemStack == itemStack;
    }

}
