package team.lodestar.lodestone.systems.shader;

import org.joml.*;
import org.lwjgl.system.MemoryUtil;

import java.nio.*;
import java.util.function.Consumer;

import static org.lwjgl.opengl.GL20.*;

public abstract class Uniform<T> implements AutoCloseable {
    protected final String name;
    protected Buffer buffer;
    protected LodestoneShaderProgram program;
    protected boolean dirty;

    public Uniform(String name, LodestoneShaderProgram program) {
        this.name = name;
        this.program = program;
        this.buffer = this.getDataType().createBuffer(this.getBufferSize());
    }
    public abstract void set(T value);
    public int getUniformLocation() {
        return glGetUniformLocation(program.getProgramID(), name);
    }
    public abstract GlDataTypes getDataType();
    public abstract int getBufferSize();
    public abstract void upload();
    public void markDirty() {
        dirty = true;
        if (program != null) program.markDirty();
    }

    @Override
    public void close() {
        if (buffer != null) MemoryUtil.memFree(buffer);
    }

    public static class IntUniform extends Uniform<Integer> {
        public IntUniform(String name, int bufferSize, LodestoneShaderProgram program) {
            super(name, program);
        }

        @Override
        public void set(Integer value) {
            if (this.buffer instanceof IntBuffer intBuffer) {
                intBuffer.position(0);
                intBuffer.put(0, value);
                this.markDirty();
            }
        }

        @Override
        public GlDataTypes getDataType() {
            return GlDataTypes.INT;
        }

        @Override
        public int getBufferSize() {
            return 1;
        }

        @Override
        public void upload() {
            if (!dirty) return;

            int location = getUniformLocation();
            if (location < 0) return;

            withIntBuffer(buffer -> glUniform1iv(location, buffer));

            dirty = false;
        }
    }


    public static class FloatUniform extends Uniform<Float> {
        public FloatUniform(String name, int bufferSize, LodestoneShaderProgram program) {
            super(name, program);
        }

        @Override
        public void set(Float value) {
            if (this.buffer instanceof FloatBuffer floatBuffer) {
                floatBuffer.position(0);
                floatBuffer.put(0, value);
                this.markDirty();
            }
        }

        @Override
        public GlDataTypes getDataType() {
            return GlDataTypes.FLOAT;
        }

        @Override
        public int getBufferSize() {
            return 1;
        }

        @Override
        public void upload() {
            if (!dirty) return;

            int location = getUniformLocation();
            if (location < 0) return;

            withFloatBuffer(buffer -> glUniform1fv(location, buffer));

            dirty = false;
        }
    }

    public static class Vector3fUniform extends Uniform<Vector3f> {
        public Vector3fUniform(String name, LodestoneShaderProgram program) {
            super(name, program);
        }
        @Override
        public void set(Vector3f value) {
            if (this.buffer instanceof FloatBuffer floatBuffer) {
                floatBuffer.position(0);
                value.get(floatBuffer);
                this.markDirty();
            }
        }

        @Override
        public GlDataTypes getDataType() {
            return GlDataTypes.FLOAT;
        }

        @Override
        public int getBufferSize() {
            return 3;
        }

        @Override
        public void upload() {
            if (!dirty) return;

            int location = getUniformLocation();
            if (location < 0) return;

            withFloatBuffer(buffer -> glUniform3fv(location, buffer));

            dirty = false;
        }
    }

    public static class Matrix2fUniform extends Uniform<Matrix2f> {
        public Matrix2fUniform(String name, LodestoneShaderProgram program) {
            super(name, program);
        }
        @Override
        public void set(Matrix2f value) {
            if (buffer instanceof FloatBuffer floatBuffer) {
                floatBuffer.position(0);
                value.get(floatBuffer);
                this.markDirty();
            }
        }

        @Override
        public GlDataTypes getDataType() {
            return GlDataTypes.FLOAT;
        }

        @Override
        public int getBufferSize() {
            return 4;
        }

        @Override
        public void upload() {
            if (!dirty) return;

            int location = getUniformLocation();
            if (location < 0) return;

            withFloatBuffer(buffer -> glUniformMatrix2fv(location, false, buffer));

            dirty = false;
        }
    }

    public static class Matrix3fUniform extends Uniform<Matrix3f> {
        public Matrix3fUniform(String name, LodestoneShaderProgram program) {
            super(name, program);
        }
        @Override
        public void set(Matrix3f value) {
            if (buffer instanceof FloatBuffer floatBuffer) {
                floatBuffer.position(0);
                value.get(floatBuffer);
                this.markDirty();
            }
        }

        @Override
        public GlDataTypes getDataType() {
            return GlDataTypes.FLOAT;
        }

        @Override
        public int getBufferSize() {
            return 9;
        }

        public void upload() {
            if (!dirty) return;

            int location = getUniformLocation();
            if (location < 0) return;

            withFloatBuffer(buffer -> glUniformMatrix3fv(location, false, buffer));

            dirty = false;
        }
    }

    public static class Matrix4fUniform extends Uniform<Matrix4f> {
        public Matrix4fUniform(String name, LodestoneShaderProgram program) {
            super(name, program);
        }
        @Override
        public void set(Matrix4f value) {
            if (buffer instanceof FloatBuffer floatBuffer) {
                floatBuffer.position(0);
                value.get(floatBuffer);
                this.markDirty();
            }
        }

        @Override
        public GlDataTypes getDataType() {
            return GlDataTypes.FLOAT;
        }

        @Override
        public int getBufferSize() {
            return 16;
        }

        @Override
        public void upload() {
            if (!dirty) return;

            int location = getUniformLocation();
            if (location < 0) return;

            withFloatBuffer(buffer -> glUniformMatrix4fv(location, false, buffer));

            dirty = false;
        }
    }

    protected void withFloatBuffer(Consumer<FloatBuffer> buffer) {
        if (this.buffer instanceof FloatBuffer floatBuffer) {
            buffer.accept(floatBuffer);
        }
    }

    protected void withIntBuffer(Consumer<IntBuffer> buffer) {
        if (this.buffer instanceof IntBuffer intBuffer) {
            buffer.accept(intBuffer);
        }
    }
}
