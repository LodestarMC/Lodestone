package team.lodestar.lodestone.systems.model.geo;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.Direction;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.GsonHelper;
import org.joml.Vector2f;
import org.joml.Vector3f;
import team.lodestar.lodestone.systems.model.geo.data.*;
import team.lodestar.lodestone.systems.model.geo.data.*;
import team.lodestar.lodestone.helpers.JsonHelper;
import team.lodestar.lodestone.systems.model.LodestoneParser;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.*;

public class BedrockGeometryParser extends LodestoneParser<BedrockGeometryModel> {

    private final Map<String, GeoBone> bones = new HashMap<>();
    private String version;
    private GeoDescription geoDescription;

    @Override
    public void parse(Resource resource, BedrockGeometryModel model) throws IOException {
        JsonObject json = GsonHelper.parse(resource.openAsReader());
        this.version = GsonHelper.getAsString(json, "format_version");
        JsonArray geometries = json.getAsJsonArray("minecraft:geometry");
        for (int i = 0; i < geometries.size(); i++) {
            parseGeometry(geometries.get(i).getAsJsonObject());
        }

        for (String boneName : bones.keySet()) {
            GeoBone bone = bones.get(boneName);
            if (bone.getParent() != null) {
                GeoBone parentBone = bones.get(bone.getParent());
                if (parentBone != null) {
                    parentBone.addChild(boneName, bone);
                } else {
                    throw new IOException("Parent bone '" + bone.getParent() + "' not found for bone '" + boneName + "'");
                }
            } else {
                model.root = bone;
            }
        }
    }

    private void parseGeometry(JsonObject geometry) {
        this.geoDescription = parseDescription(geometry.getAsJsonObject("description"));
        JsonArray bones = geometry.getAsJsonArray("bones");
        for (int i = 0; i < bones.size(); i++) {
            parseBone(bones.get(i).getAsJsonObject());
        }

    }

    private GeoDescription parseDescription(JsonObject description) {
        String identifier = GsonHelper.getAsString(description, "identifier");
        int textureWidth = GsonHelper.getAsInt(description, "texture_width");
        int textureHeight = GsonHelper.getAsInt(description, "texture_height");
        float visibleBoundsWidth = GsonHelper.getAsFloat(description, "visible_bounds_width");
        float visibleBoundsHeight = GsonHelper.getAsFloat(description, "visible_bounds_height");
        Vector3f visibleBoundsOffset = JsonHelper.getAsVec3f(description, "visible_bounds_offset");
        return new GeoDescription(identifier, textureWidth, textureHeight, visibleBoundsWidth, visibleBoundsHeight, visibleBoundsOffset);
    }

    private GeoBone parseBone(JsonObject bone) {
        String name = GsonHelper.getAsString(bone, "name");
        String parent = GsonHelper.getAsString(bone, "parent", null); // optional
        Vector3f pivot = JsonHelper.getAsVec3f(bone, "pivot");
        Vector3f rotation = JsonHelper.getAsVec3f(bone, "rotation"); // optional
        JsonArray cubes = bone.getAsJsonArray("cubes"); // optional
        List<GeoCube> geoCubes = new ArrayList<>();
        if (cubes != null) {
            for (int i = 0; i < cubes.size(); i++) {
                geoCubes.add(parseCube(cubes.get(i).getAsJsonObject()));
            }
        }
        GeoBone geoBone = new GeoBone(geoCubes, new HashMap<>(), parent);
        geoBone.setPosition(pivot);
        if (rotation != null) {
            geoBone.setRotation(rotation.x, rotation.y, rotation.z);
        }
        bones.put(name, geoBone);
        return geoBone;
    }

    private GeoCube parseCube(JsonObject cube) {
        Vector3f origin = JsonHelper.getAsVec3f(cube, "origin");
        Vector3f size = JsonHelper.getAsVec3f(cube, "size");

        origin = new Vector3f(
                -(origin.x + size.x) / 16.0f,
                origin.y / 16.0f,
                origin.z/ 16.0f
        );
        Vector3f vertexSize = new Vector3f(size).div(16.0f);

        VertexSet vertexSet = VertexSet.fromCubeData(origin, vertexSize);

        JsonObject uv = cube.getAsJsonObject("uv");
        UVSet uvSet = new UVSet(
                parseUV(uv, "north"),
                parseUV(uv, "south"),
                parseUV(uv, "east"),
                parseUV(uv, "west"),
                parseUV(uv, "up"),
                parseUV(uv, "down")
        );
        List<GeoQuad> quads = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            UVData uvData = uvSet.getFace(direction);
            if (uvData != null) {
                GeoQuad quad = GeoQuad.build(vertexSet.verticesForQuad(direction), uvData.uv,
                        uvData.uvSize, uvData.uvRotation,
                        new Vector2f(this.geoDescription.textureWidth(), this.geoDescription.textureHeight()));
                quads.add(quad);
            }
        }

        return new GeoCube(quads.toArray(new GeoQuad[0]), origin, size, new Vector3f());
    }

    private @Nullable UVData parseUV(JsonObject uvSet, String face) {
        if (!uvSet.has(face)) return null;
        JsonObject uv = uvSet.getAsJsonObject(face);

        Vector2f uvVec  = JsonHelper.getAsVec2f(uv, "uv");
        Vector2f uvSize = JsonHelper.getAsVec2f(uv, "uv_size");
        int rot = GsonHelper.getAsInt(uv, "uv_rotation", 0);

        return new UVData(uvVec, uvSize, rot);
    }

    private record VertexSet(GeoVertex bottomLeftBack, GeoVertex bottomRightBack,
                             GeoVertex topLeftBack, GeoVertex topRightBack,
                             GeoVertex topLeftFront, GeoVertex topRightFront,
                             GeoVertex bottomLeftFront, GeoVertex bottomRightFront) {

        public static VertexSet fromCubeData(Vector3f origin, Vector3f size) {
            GeoVertex blb = new GeoVertex(origin.x, origin.y, origin.z);
            GeoVertex brb = new GeoVertex(origin.x, origin.y, origin.z + size.z);
            GeoVertex tlb = new GeoVertex(origin.x, origin.y + size.y, origin.z);
            GeoVertex trb = new GeoVertex(origin.x, origin.y + size.y, origin.z + size.z);
            GeoVertex tlf = new GeoVertex(origin.x + size.x, origin.y + size.y, origin.z);
            GeoVertex trf = new GeoVertex(origin.x + size.x, origin.y + size.y, origin.z + size.z);
            GeoVertex blf = new GeoVertex(origin.x + size.x, origin.y, origin.z);
            GeoVertex brf = new GeoVertex(origin.x + size.x, origin.y, origin.z + size.z);

            return new VertexSet(blb, brb, tlb, trb, tlf, trf, blf, brf);
        }

        public GeoVertex[] verticesForQuad(Direction direction) {
            return switch (direction) {
                case WEST -> new GeoVertex[] {this.topRightBack, this.topLeftBack, this.bottomLeftBack, this.bottomRightBack};
                case EAST -> new GeoVertex[] {this.topLeftFront, this.topRightFront, this.bottomRightFront, this.bottomLeftFront};
                case NORTH -> new GeoVertex[] {this.topLeftBack, this.topLeftFront, this.bottomLeftFront, this.bottomLeftBack};
                case SOUTH -> new GeoVertex[] {this.topRightFront, this.topRightBack, this.bottomRightBack, this.bottomRightFront};
                case UP -> new GeoVertex[] {this.topRightBack, this.topRightFront, this.topLeftFront, this.topLeftBack};
                case DOWN -> new GeoVertex[] {this.bottomLeftBack, this.bottomLeftFront, this.bottomRightFront, this.bottomRightBack};
            };
        }
    }

    private record UVData(Vector2f uv, Vector2f uvSize, int uvRotation) {}

    private record UVSet(@Nullable UVData north,
                         @Nullable UVData south,
                         @Nullable UVData east,
                         @Nullable UVData west,
                         @Nullable UVData up,
                         @Nullable UVData down) {

        public @Nullable UVData getFace(Direction direction) {
            return switch (direction) {
                case NORTH -> north;
                case SOUTH -> south;
                case EAST  -> east;
                case WEST  -> west;
                case UP    -> up;
                case DOWN  -> down;
            };
        }
    }


}
