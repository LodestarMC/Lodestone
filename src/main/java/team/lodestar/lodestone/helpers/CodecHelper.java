package team.lodestar.lodestone.helpers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;

import java.util.function.Supplier;

public class CodecHelper {
    public static <T, B> B encodeOrThrow(DynamicOps<B> target, Codec<T> codec, T value) {
        return codec.encodeStart(target, value).result().orElseThrow(
                () -> new IllegalStateException("Failed to encode " + value + " as " + target.toString())
        );
    }

    public static <T, B> T decodeOrThrow(DynamicOps<B> source, Codec<T> codec, B value) {
        return codec.parse(source, value).result().orElseThrow(
                () -> new IllegalStateException("Failed to decode " + value + " from " + source.toString())
        );
    }

    /**
     * Overwrites the fields of the target object with the fields of the source object.
     * If the target contains fields that the source does not, they will be left unchanged.
     */
    public static <T, B> void overwiteExisting(DynamicOps<B> source, Codec<T> codec, B value, T target) {
        T obj = decodeOrThrow(source, codec, value);
        ReflectionHelper.copyFields(obj, target);
    }
}
