package team.lodestar.lodestone.helpers.util.transforms;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import net.minecraft.core.Direction;

public interface Rotate<Self> {

    Self multiply(Quaternion quaternion);

    default Self rotate(Direction axis, float radians) {
        if (radians == 0)
            return (Self) this;
        return multiply(axis.step()
                .rotation(radians));
    }

    default Self rotate(double angle, Direction.Axis axis) {
        Vector3f vec =
                axis == Direction.Axis.X ? Vector3f.XP : axis == Direction.Axis.Y ? Vector3f.YP : Vector3f.ZP;
        return multiply(vec, angle);
    }

    default Self rotateX(double angle) {
        return multiply(Vector3f.XP, angle);
    }

    default Self rotateY(double angle) {
        return multiply(Vector3f.YP, angle);
    }

    default Self multiply(Vector3f axis, double angle) {
        if (angle == 0)
            return (Self) this;
        return multiply(axis.rotationDegrees((float) angle));
    }

    default Self multiplyRadians(Vector3f axis, double angle) {
        if (angle == 0)
            return (Self) this;
        return multiply(axis.rotation((float) angle));
    }
}