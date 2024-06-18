package team.lodestar.lodestone.systems.shader;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Arrays;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;
import static org.lwjgl.opengl.GL40.GL_TESS_CONTROL_SHADER;
import static org.lwjgl.opengl.GL40.GL_TESS_EVALUATION_SHADER;
import static org.lwjgl.opengl.GL43.GL_COMPUTE_SHADER;

public enum GLShaderTypes {
    VERTEX("vertex", "vsh", GL_VERTEX_SHADER, false),
    FRAGMENT("fragment", "fsh", GL_FRAGMENT_SHADER, false),
    GEOMETRY("geometry", "gsh", GL_GEOMETRY_SHADER, true),
    TESS_CONTROL("tess_control", "tcsh", GL_TESS_CONTROL_SHADER, true),
    TESS_EVALUATION("tess_evaluation", "tesh", GL_TESS_EVALUATION_SHADER, true),
    COMPUTE("compute", "csh", GL_COMPUTE_SHADER, true);

    private final String name;
    private final String fileExtension;
    private final int glConst;
    private final boolean optional;

    GLShaderTypes(String name, String fileExtension, int glConst, boolean optional) {
        this.name = name;
        this.fileExtension = fileExtension;
        this.glConst = glConst;
        this.optional = optional;
    }

    public String getName() {
        return name;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public int getGlConst() {
        return glConst;
    }

    public boolean isOptional() {
        return optional;
    }

    public static GLShaderTypes getFromName(String name) {
        for (GLShaderTypes shaderType : values()) {
            if (shaderType.getName().equals(name)) {
                return shaderType;
            }
        }
        return null;
    }

    public String appendExtension() {
        return "." + fileExtension;
    }

    private static final Codec<GLShaderTypes> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("type").forGetter(GLShaderTypes::name)
            ).apply(instance, GLShaderTypes::getFromName)
    );
}
