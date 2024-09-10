package team.lodestar.lodestone.systems.model.obj.data;

import java.util.Objects;

public record IndexedVertex(int positionIndex, int normalIndex, int uvIndex) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndexedVertex that = (IndexedVertex) o;
        return positionIndex == that.positionIndex && normalIndex == that.normalIndex && uvIndex == that.uvIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(positionIndex, normalIndex, uvIndex);
    }
}
