package team.lodestar.lodestone.systems.shader;

import org.lwjgl.system.MemoryUtil;

import java.nio.Buffer;
import java.util.function.Function;

import static org.lwjgl.opengl.GL11.*;

public enum GlDataTypes {
    BYTE(1, "Byte", GL_BYTE, Range.of(-128, 127), MemoryUtil::memAlloc),
    UNSIGNED_BYTE(1, "Unsigned Byte", GL_UNSIGNED_BYTE, Range.of(0, 255), MemoryUtil::memAlloc),
    SHORT(2, "Short", GL_SHORT, Range.of(-32768, 32767), MemoryUtil::memAllocShort),
    UNSIGNED_SHORT(2, "Unsigned Short", GL_UNSIGNED_SHORT, Range.of(0, 65535), MemoryUtil::memAllocShort),
    TWO_BYTES(2, "2 Bytes", GL_2_BYTES,  Range.of(-32768, 32767), MemoryUtil::memAllocShort),
    THREE_BYTES(3, "3 Bytes", GL_3_BYTES, Range.of(-8388608, 8388607), MemoryUtil::memAlloc),
    INT(4, "Int", GL_INT, Range.of(-2147483648, 2147483647), MemoryUtil::memAllocInt),
    UNSIGNED_INT(4, "Unsigned Int", GL_UNSIGNED_INT, Range.of(0L, 4294967295L), MemoryUtil::memAllocInt),
    FLOAT(4, "Float", GL_FLOAT, Range.of(1.4E-45f, 3.4028235E38f), MemoryUtil::memAllocFloat),
    FOUR_BYTES(4, "4 Bytes", GL_4_BYTES, Range.of(-2147483648, 2147483647), MemoryUtil::memAllocInt),
    DOUBLE(8, "Double", GL_DOUBLE, Range.of(4.9E-324, 1.7976931348623157E308), MemoryUtil::memAllocDouble);

    private final int sizeInBytes;

    private final String name;

    private final int glConst;
    private final Range<?> range;
    private final Function<Integer, Buffer> bufferFunction;

    GlDataTypes(int sizeInBytes, String name, int glConst, Range<?> range, Function<Integer, Buffer> bufferFunction) {
        this.sizeInBytes = sizeInBytes;
        this.name = name;
        this.glConst = glConst;
        this.range = range;
        this.bufferFunction = bufferFunction;
    }

    public int getSizeInBytes() {
        return sizeInBytes;
    }

    public String getName() {
        return name;
    }

    public int getGlConst() {
        return glConst;
    }

    public boolean isInRange(Number value) {
        return range.isInRange(value);
    }

    public Buffer createBuffer(int size) {
        return bufferFunction.apply(size);
    }

    private static class Range<T extends Number> {
        private final T minimumn;
        private final T maximum;

        private Range(T min, T max) {
            this.minimumn = min;
            this.maximum = max;
        }

        public static <T extends Number> Range<T> of(T min, T max) {
            return new Range<>(min, max);
        }

        public T getMinimum() {
            return minimumn;
        }

        public T getMaximum() {
            return maximum;
        }

        public boolean isInRange(Number value) {
            return value.doubleValue() >= this.getMinimum().doubleValue() && value.doubleValue() <= this.getMaximum().doubleValue();
        }


    }
}
