package team.lodestar.lodestone.systems.model.obj.data;

import java.util.Objects;

/**
 * Represents a vertex with indices for position, normal and uv.
 * <p>Used exclusively for parsing OBJ files and generating a list of unique vertices</p>
 * <p>See {@link team.lodestar.lodestone.systems.model.obj.data.Vertex} for a vertex that stores the actual data</p>
 * @param positionIndex
 * @param normalIndex
 * @param uvIndex
 */
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
