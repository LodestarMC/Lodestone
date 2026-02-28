package team.lodestar.lodestone.renderer.device;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

import java.util.function.Function;

public class DeviceRequirement {
    private final Component name;
    private final Component failMessage;
    private final Function<GLCapabilities, Boolean> requirement;

    public DeviceRequirement(Component name, Component failMessage, Function<GLCapabilities, Boolean> requirement) {
        this.name = name;
        this.failMessage = failMessage;
        this.requirement = requirement;
    }

    public Component getName() {
        return name;
    }

    public Component getFailMessage() {
        return failMessage;
    }

    public boolean test() {
        return requirement.apply(GL.getCapabilities());
    }

    public static final DeviceRequirement INSTANCED_DRAW = new DeviceRequirement(
            Component.translatable("device.requirement.lodestone.instanced_draw"),
            Component.translatable("device.requirement.lodestone.instanced_draw.fail").withStyle(ChatFormatting.RED),
            caps -> caps.OpenGL31 || caps.GL_ARB_draw_instanced
    );

    public static final DeviceRequirement COMPUTE_SHADERS = new DeviceRequirement(
            Component.translatable("device.requirement.lodestone.compute_shaders"),
            Component.translatable("device.requirement.lodestone.compute_shaders.fail").withStyle(ChatFormatting.RED),
            caps -> caps.OpenGL43 || caps.GL_ARB_compute_shader
    );

    public static final DeviceRequirement SSBO = new DeviceRequirement(
            Component.translatable("device.requirement.lodestone.ssbo"),
            Component.translatable("device.requirement.lodestone.ssbo.fail").withStyle(ChatFormatting.RED),
            caps -> caps.OpenGL43 || caps.GL_ARB_shader_storage_buffer_object
    );
}
