package team.lodestar.lodestone.modules.rendering.particle.standard.screen;

public record ItemStackScreenParticleCacheKey(boolean isHotbarItem, boolean isLateRender, int x, int y) {

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ItemStackScreenParticleCacheKey key)) {
            return false;
        }
        return key.isHotbarItem == isHotbarItem && key.isLateRender == isLateRender && key.x == x && key.y == y;
    }

}