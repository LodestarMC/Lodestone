package team.lodestar.lodestone.systems.particle.screen;

public record ScreenParticleItemStackVaultKey(boolean isHotbarItem, boolean isRenderedAfterItem, int x, int y) {

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ScreenParticleItemStackVaultKey key)) {
            return false;
        }
        return key.isHotbarItem == isHotbarItem && key.isRenderedAfterItem == isRenderedAfterItem && key.x == x && key.y == y;
    }
}