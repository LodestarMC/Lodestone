package com.sammy.ortus.helpers.math;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.sammy.ortus.mixin.GameRendererMixin;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

//TODO: re-implement all NECESSARY functions into the new vecHelper method and remove ones that are not required.
public class VecHelper {
    public static final Vec3 CENTER_OF_ORIGIN = new Vec3(.5, .5, .5);

    public static Vec3 getCenterOf(Vec3i pos) {
        if (pos.equals(Vec3i.ZERO))
            return CENTER_OF_ORIGIN;
        return Vec3.atLowerCornerOf(pos)
                .add(.5f, .5f, .5f);
    }

    public static Vec3 axisAlignedPlaneOf(Vec3 vec) {
        vec = vec.normalize();
        return new Vec3(1, 1, 1).subtract(Math.abs(vec.x), Math.abs(vec.y), Math.abs(vec.z));
    }

    public static Vec3 rotate(Vec3 vec, double deg, Direction.Axis axis) {
        if (deg == 0)
            return vec;
        if (vec == Vec3.ZERO)
            return vec;

        float angle = (float) (deg / 180f * Math.PI);
        double sin = Mth.sin(angle);
        double cos = Mth.cos(angle);
        double x = vec.x;
        double y = vec.y;
        double z = vec.z;

        if (axis == Direction.Axis.X)
            return new Vec3(x, y * cos - z * sin, z * cos + y * sin);
        if (axis == Direction.Axis.Y)
            return new Vec3(x * cos + z * sin, y, z * cos - x * sin);
        if (axis == Direction.Axis.Z)
            return new Vec3(x * cos - y * sin, y * cos + x * sin, z);
        return vec;
    }

    // https://forums.minecraftforge.net/topic/88562-116solved-3d-to-2d-conversion/?do=findComment&comment=413573 slightly modified
    public static Vec3 projectToPlayerView(Vec3 target, float partialTicks) {
        /*
         * The (centered) location on the screen of the given 3d point in the world.
         * Result is (dist right of center screen, dist up from center screen, if < 0, then in front of view plane)
         */
        Camera ari = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 camera_pos = ari.getPosition();
        Quaternion camera_rotation_conj = ari.rotation()
                .copy();
        camera_rotation_conj.conj();

        Vector3f result3f = new Vector3f((float) (camera_pos.x - target.x), (float) (camera_pos.y - target.y),
                (float) (camera_pos.z - target.z));
        result3f.transform(camera_rotation_conj);

        // ----- compensate for view bobbing (if active) -----
        // the following code adapted from GameRenderer::applyBobbing (to invert it)
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.bobView) {
            Entity renderViewEntity = mc.getCameraEntity();
            if (renderViewEntity instanceof Player) {
                Player playerentity = (Player) renderViewEntity;
                float distwalked_modified = playerentity.walkDist;

                float f = distwalked_modified - playerentity.walkDistO;
                float f1 = -(distwalked_modified + f * partialTicks);
                float f2 = Mth.lerp(partialTicks, playerentity.oBob, playerentity.bob);
                Quaternion q2 = new Quaternion(Vector3f.XP,
                        Math.abs(Mth.cos(f1 * (float) Math.PI - 0.2F) * f2) * 5.0F, true);
                q2.conj();
                result3f.transform(q2);

                Quaternion q1 =
                        new Quaternion(Vector3f.ZP, Mth.sin(f1 * (float) Math.PI) * f2 * 3.0F, true);
                q1.conj();
                result3f.transform(q1);

                Vector3f bob_translation = new Vector3f((Mth.sin(f1 * (float) Math.PI) * f2 * 0.5F),
                        (-Math.abs(Mth.cos(f1 * (float) Math.PI) * f2)), 0.0f);
                bob_translation.setY(-bob_translation.y()); // this is weird but hey, if it works
                result3f.add(bob_translation);
            }
        }

        // ----- adjust for fov -----
        float fov = (float) ((GameRendererMixin) mc.gameRenderer).ortus$callGetFov(ari, partialTicks, true);

        float half_height = (float) mc.getWindow()
                .getGuiScaledHeight() / 2;
        float scale_factor = half_height / (result3f.z() * (float) Math.tan(Math.toRadians(fov / 2)));
        return new Vec3(-result3f.x() * scale_factor, result3f.y() * scale_factor, result3f.z());
    }
}
