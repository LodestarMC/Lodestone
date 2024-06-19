package team.lodestar.lodestone.systems.shader;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import team.lodestar.lodestone.LodestoneLib;

import java.io.Reader;

import static org.lwjgl.opengl.GL20.*;

public class LodestoneShader {
    private ResourceLocation sourceLocation;
    private int shaderID;
    private final LodestoneShaderProgram program;
    private final GLShaderTypes shaderType;
    private CompiledSource compiledSource;

    public LodestoneShader(LodestoneShaderProgram program, GLShaderTypes type, ResourceLocation shaderLocation) {
        this.program = program;
        this.shaderType = type;
        sourceLocation = new ResourceLocation(shaderLocation.getNamespace(), "shaders/core/" + shaderLocation.getPath() + type.appendExtension());
    }

    public void createShader(GLShaderTypes type, CompiledSource source) throws RuntimeException {
        int id = glCreateShader(type.getGlConst());
        if (id == 0) throw new RuntimeException("Failed to create shader of type: " + type.getName());

        glShaderSource(id, source.getSource());
        glCompileShader(id);

        if (glGetShaderi(id, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new RuntimeException("Failed to compile shader of type: " + type.getName() + "\n" + glGetShaderInfoLog(id, 1024));
        }

        shaderID = id;
        program.attachShader(this);
    }

    private void compileShader(ResourceProvider provider) {
        if (compiledSource != null) throw new RuntimeException("Shader already compiled");
        try (Reader reader = provider.openAsReader(sourceLocation)) {

        } catch (Exception e) {
            throw new RuntimeException("Failed to compile shader: " + sourceLocation, e);
        }




        createShader(shaderType, compiledSource);
    }

    public int getShaderID() {
        return shaderID;
    }

    public GLShaderTypes getShaderType() {
        return shaderType;
    }

    public boolean isNotNull() {
        return this.shaderID == GL_NONE;
    }

    public static class CompiledSource {
        private boolean preProcessed;
        private String source;

        public boolean isPreProcessed() {
            return preProcessed;
        }

        public String getSource() {
            return source;
        }


    }
}
