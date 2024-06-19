package team.lodestar.lodestone.systems.shader;

import com.google.gson.JsonElement;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.util.GsonHelper;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class LodestoneShaderProgram {
    private ResourceLocation programLocation;
    private int programID;
    private Map<GLShaderTypes, LodestoneShader> shaders;
    private boolean dirty;

    public LodestoneShaderProgram() {
        createProgram();
    }


    public void createProgram() {
        clearShaders();
        this.programID = glCreateProgram();
        if (this.programID == 0) throw new RuntimeException("Failed to create shader program");
    }


    public void attachShader(LodestoneShader shader) {
        glAttachShader(programID, shader.getShaderID());
        shaders.put(shader.getShaderType(), shader);
    }

    public void clearShaders() {
        if (this.programID != 0) {
            shaders.values().stream().filter(LodestoneShader::isNotNull).forEach(this::detachShader);
        }

        shaders.clear();
    }

    public void linkProgram() {
        glLinkProgram(programID);
        if (glGetProgrami(programID, GL_LINK_STATUS) == GL_FALSE) {
            throw new RuntimeException("Failed to link shader program: " + glGetProgramInfoLog(programID, 1024));
        }

        this.shaders.values().stream().filter(LodestoneShader::isNotNull).forEach(this::detachShader);
    }

    public void detachShader(LodestoneShader shader) {
        glDetachShader(programID, shader.getShaderID());
        //shaders.remove(shader.getShaderType());
    }

    public boolean hasGeometryShader() {
        return shaders.containsKey(GLShaderTypes.GEOMETRY);
    }

    public boolean hasTessControlShader() {
        return shaders.containsKey(GLShaderTypes.TESS_CONTROL);
    }

    public boolean hasTessEvaluationShader() {
        return shaders.containsKey(GLShaderTypes.TESS_EVALUATION);
    }

    public boolean isComputeShader() {
        return shaders.containsKey(GLShaderTypes.COMPUTE);
    }

    public int getProgramID() {
        return programID;
    }

    public void parseProgramConfig() {
        ResourceProvider provider = Minecraft.getInstance().getResourceManager();
        try {
            Reader reader = provider.openAsReader(programLocation);
            JsonElement jsonElement = GsonHelper.parse(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void markDirty() {
        this.dirty = true;
    }
}
