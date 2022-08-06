package team.lodestar.lodestone.helpers.util.transforms;

import com.mojang.math.Vector3f;

import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;

public interface Translate<Self> {
    Self translate(double x, double y, double z);

    default Self translate(Vec3 vec) {
        return translate(vec.x, vec.y, vec.z);
    }
}