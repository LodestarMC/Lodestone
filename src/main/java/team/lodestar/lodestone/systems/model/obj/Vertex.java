package team.lodestar.lodestone.systems.model.obj;

import net.minecraft.world.phys.Vec2;
import org.joml.Vector3f;

public record Vertex(Vector3f position, Vector3f normal, Vec2 uv) {
}
