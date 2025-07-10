package team.lodestar.lodestone.systems.rendering;

import com.mojang.blaze3d.systems.RenderSystem;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import static org.lwjgl.opengl.GL43.*;

public class LodestoneRenderSystem extends RenderSystem {
    private static final List<IBufferObject> bufferObjects = new ArrayList<>();
    public static void wrap(Runnable renderCall) {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.queueFencedTask(renderCall);
        } else {
            renderCall.run();
        }
    }

    public static void registerBufferObject(IBufferObject bufferObject) {
        bufferObjects.add(bufferObject);
    }

    public static void unregisterBufferObject(IBufferObject bufferObject) {
        bufferObjects.remove(bufferObject);
    }

    public static void destroyBufferObjects() {
        Iterator<IBufferObject> objects = bufferObjects.iterator();
        while (objects.hasNext()) {
            IBufferObject object = objects.next();
            object.destroy();
            objects.remove();
        }
    }

    public static void dispatchCompute(int num_groups_x, int num_groups_y, int num_groups_z) {
        wrap(() -> glDispatchCompute(num_groups_x, num_groups_y, num_groups_z));
    }

    public static void memoryBarrier(int barriers) {
        wrap(() -> glMemoryBarrier(barriers));
    }

    public static void bindBufferBase(int target, int index, int buffer) {
        wrap(() -> glBindBufferBase(target, index, buffer));
    }

    public static void mapBuffer(int target, int access, Consumer<ByteBuffer> byteBufferConsumer) {
        wrap(() -> byteBufferConsumer.accept(glMapBuffer(target, access)));
    }

    public static void unmapBuffer(int target) {
        wrap(() -> glUnmapBuffer(target));
    }
}
