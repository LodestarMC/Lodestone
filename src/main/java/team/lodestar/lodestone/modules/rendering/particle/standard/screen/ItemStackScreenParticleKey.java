package team.lodestar.lodestone.modules.rendering.particle.standard.screen;

import net.minecraft.world.item.ItemStack;

public record ItemStackScreenParticleKey(boolean isHotbarItem, boolean isLateRender, ItemStack itemStack) {

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ItemStackScreenParticleKey key)) {
            return false;
        }
        return key.isHotbarItem == isHotbarItem && key.isLateRender == isLateRender && key.itemStack == itemStack;
    }

}
