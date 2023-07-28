package team.lodestar.lodestone.systems.particle.screen;

import java.util.*;

public class ScreenParticleItemStackRetrievalKey {

    public final boolean isHotbarItem;
    public final boolean isRenderedAfterItem;
    public final int x;
    public final int y;

    public ScreenParticleItemStackRetrievalKey(boolean isHotbarItem, boolean isRenderedAfterItem, int x, int y) {
        this.isHotbarItem = isHotbarItem;
        this.isRenderedAfterItem = isRenderedAfterItem;
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ScreenParticleItemStackRetrievalKey key)) {
            return false;
        }
        return key.isHotbarItem == isHotbarItem && key.isRenderedAfterItem == isRenderedAfterItem && key.x == x && key.y == y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(isHotbarItem, isRenderedAfterItem, x, y);
    }
}