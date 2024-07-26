package team.lodestar.lodestone.handlers;

import com.mojang.blaze3d.shaders.Shader;
import net.irisshaders.iris.Iris;
import net.irisshaders.iris.pipeline.WorldRenderingPipeline;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import com.mojang.blaze3d.systems.RenderSystem;
import team.lodestar.lodestone.mixin.client.integration.iris.IrisRenderingPipelineAccessor;

import java.util.Set;

public class OculusIntegration {

    public static void setupShaders() {
        // Retrieve the custom shader for Oculus
        Set<ShaderInstance> oculusShader = getOculusShader();
        if (oculusShader != null) {
            // Set the shader for the rendering system
            for (ShaderInstance instance : oculusShader){
                RenderSystem.setShader(() -> instance);
                // Setup shader uniforms
                setupShaderUniforms(instance);
            }
        }
    }

    public static void restoreShaders() {
        // Restore the default shader state after Oculus-specific rendering
        // Set to the default shader used by Minecraft
        var shaderSet = getOculusShader();
        if (shaderSet != null) {
            for (ShaderInstance instance : shaderSet){
                RenderSystem.setShader(() ->
                        Minecraft.getInstance().gameRenderer.getMinecraft().gameRenderer.getShader(
                                instance.getName())
                );
            }
        }
    }

    private static Set<ShaderInstance> getOculusShader() {
        // This method should retrieve or create the Oculus-specific shader
        // Example: replace with actual logic to get the Oculus shader
        // Note: Minecraft does not provide a direct method like getShader("oculus_shader")
        // You need to manage this based on how shaders are loaded in your setup
        WorldRenderingPipeline pipeline = Iris.getPipelineManager().getPipelineNullable();
        if (pipeline instanceof IrisRenderingPipelineAccessor) {
            return ((IrisRenderingPipelineAccessor) pipeline).getLoadedShaders();
        }

        return null;
    }

    private static void setupShaderUniforms(Shader shader) {
        // Use standard methods for setting shader uniforms if available
        if (shader instanceof ShaderInstance shaderProgram) {
            // Example: Set uniforms using ShaderProgram's method
            //Objects.requireNonNull(shaderProgram.getUniform("CustomUniform1")).set(1.0f);
            //Objects.requireNonNull(shaderProgram.getUniform("CustomUniform2")).set(0.5f);
        }
    }
}