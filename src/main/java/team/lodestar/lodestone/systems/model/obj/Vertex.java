package team.lodestar.lodestone.systems.model.obj;

import com.mojang.math.Vector3f;
import net.minecraft.world.phys.Vec2;

public record Vertex(Vector3f position, Vector3f normal, Vec2 uv) {
}