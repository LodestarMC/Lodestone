package team.lodestar.lodestone.common.network;

import com.mojang.datafixers.util.*;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.function.Function;

@SuppressWarnings({"NullableProblems", "unchecked", "CastCanBeRemovedNarrowingVariableType", "unused"})
public class ExtraStreamCodecs {
  public static final StreamCodec<ByteBuf, Vec3> VEC3 = StreamCodec.composite(
          ByteBufCodecs.DOUBLE, Vec3::x,
          ByteBufCodecs.DOUBLE, Vec3::y,
          ByteBufCodecs.DOUBLE, Vec3::z,
          Vec3::new);

  public static <B, C, T1, T2, T3, T4, T5, T6, T7> StreamCodec<B, C> composite(
          final StreamCodec<? super B, T1> arg, final Function<C, T1> function,
          final StreamCodec<? super B, T2> arg2, final Function<C, T2> function2,
          final StreamCodec<? super B, T3> arg3, final Function<C, T3> function3,
          final StreamCodec<? super B, T4> arg4, final Function<C, T4> function4,
          final StreamCodec<? super B, T5> arg5, final Function<C, T5> function5,
          final StreamCodec<? super B, T6> arg6, final Function<C, T6> function6,
          final StreamCodec<? super B, T7> arg7, final Function<C, T7> function7,
          final Function7<T1, T2, T3, T4, T5, T6, T7, C> function72) {
    return new StreamCodec<>() {

        @Override
        public C decode(B object) {
            Object object2 = arg.decode(object);
            Object object3 = arg2.decode(object);
            Object object4 = arg3.decode(object);
            Object object5 = arg4.decode(object);
            Object object6 = arg5.decode(object);
            Object object7 = arg6.decode(object);
            Object object8 = arg7.decode(object);
            return function72.apply((T1) object2, (T2) object3, (T3) object4, (T4) object5, (T5) object6, (T6) object7, (T7) object8);
        }

        @Override
        public void encode(B object, C object2) {
            arg.encode(object, function.apply(object2));
            arg2.encode(object, function2.apply(object2));
            arg3.encode(object, function3.apply(object2));
            arg4.encode(object, function4.apply(object2));
            arg5.encode(object, function5.apply(object2));
            arg6.encode(object, function6.apply(object2));
            arg7.encode(object, function7.apply(object2));
        }
    };
  }

  public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8> StreamCodec<B, C> composite(
          final StreamCodec<? super B, T1> arg, final Function<C, T1> function,
          final StreamCodec<? super B, T2> arg2, final Function<C, T2> function2,
          final StreamCodec<? super B, T3> arg3, final Function<C, T3> function3,
          final StreamCodec<? super B, T4> arg4, final Function<C, T4> function4,
          final StreamCodec<? super B, T5> arg5, final Function<C, T5> function5,
          final StreamCodec<? super B, T6> arg6, final Function<C, T6> function6,
          final StreamCodec<? super B, T7> arg7, final Function<C, T7> function7,
          final StreamCodec<? super B, T8> arg8, final Function<C, T8> function8,
          final Function8<T1, T2, T3, T4, T5, T6, T7, T8, C> function72) {
    return new StreamCodec<>() {

        @Override
        public C decode(B object) {
            Object object2 = arg.decode(object);
            Object object3 = arg2.decode(object);
            Object object4 = arg3.decode(object);
            Object object5 = arg4.decode(object);
            Object object6 = arg5.decode(object);
            Object object7 = arg6.decode(object);
            Object object8 = arg7.decode(object);
            Object object9 = arg8.decode(object);
            return function72.apply((T1) object2, (T2) object3, (T3) object4, (T4) object5, (T5) object6, (T6) object7, (T7) object8, (T8) object9);
        }

        @Override
        public void encode(B object, C object2) {
            arg.encode(object, function.apply(object2));
            arg2.encode(object, function2.apply(object2));
            arg3.encode(object, function3.apply(object2));
            arg4.encode(object, function4.apply(object2));
            arg5.encode(object, function5.apply(object2));
            arg6.encode(object, function6.apply(object2));
            arg7.encode(object, function7.apply(object2));
            arg8.encode(object, function8.apply(object2));
        }
    };
  }

  public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9> StreamCodec<B, C> composite(
          final StreamCodec<? super B, T1> arg, final Function<C, T1> function,
          final StreamCodec<? super B, T2> arg2, final Function<C, T2> function2,
          final StreamCodec<? super B, T3> arg3, final Function<C, T3> function3,
          final StreamCodec<? super B, T4> arg4, final Function<C, T4> function4,
          final StreamCodec<? super B, T5> arg5, final Function<C, T5> function5,
          final StreamCodec<? super B, T6> arg6, final Function<C, T6> function6,
          final StreamCodec<? super B, T7> arg7, final Function<C, T7> function7,
          final StreamCodec<? super B, T8> arg8, final Function<C, T8> function8,
          final StreamCodec<? super B, T9> arg9, final Function<C, T9> function9,
          final Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, C> function72) {
    return new StreamCodec<>() {

        @Override
        public C decode(B object) {
            Object object2 = arg.decode(object);
            Object object3 = arg2.decode(object);
            Object object4 = arg3.decode(object);
            Object object5 = arg4.decode(object);
            Object object6 = arg5.decode(object);
            Object object7 = arg6.decode(object);
            Object object8 = arg7.decode(object);
            Object object9 = arg8.decode(object);
            Object object10 = arg9.decode(object);
            return function72.apply((T1) object2, (T2) object3, (T3) object4, (T4) object5, (T5) object6, (T6) object7, (T7) object8, (T8) object9, (T9) object10);
        }

        @Override
        public void encode(B object, C object2) {
            arg.encode(object, function.apply(object2));
            arg2.encode(object, function2.apply(object2));
            arg3.encode(object, function3.apply(object2));
            arg4.encode(object, function4.apply(object2));
            arg5.encode(object, function5.apply(object2));
            arg6.encode(object, function6.apply(object2));
            arg7.encode(object, function7.apply(object2));
            arg8.encode(object, function8.apply(object2));
            arg9.encode(object, function9.apply(object2));
        }
    };
  }

  public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> StreamCodec<B, C> composite(
          final StreamCodec<? super B, T1> arg, final Function<C, T1> function,
          final StreamCodec<? super B, T2> arg2, final Function<C, T2> function2,
          final StreamCodec<? super B, T3> arg3, final Function<C, T3> function3,
          final StreamCodec<? super B, T4> arg4, final Function<C, T4> function4,
          final StreamCodec<? super B, T5> arg5, final Function<C, T5> function5,
          final StreamCodec<? super B, T6> arg6, final Function<C, T6> function6,
          final StreamCodec<? super B, T7> arg7, final Function<C, T7> function7,
          final StreamCodec<? super B, T8> arg8, final Function<C, T8> function8,
          final StreamCodec<? super B, T9> arg9, final Function<C, T9> function9,
          final StreamCodec<? super B, T10> arg10, final Function<C, T10> function10,
          final Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, C> function72) {
    return new StreamCodec<>() {

        @Override
        public C decode(B object) {
            Object object2 = arg.decode(object);
            Object object3 = arg2.decode(object);
            Object object4 = arg3.decode(object);
            Object object5 = arg4.decode(object);
            Object object6 = arg5.decode(object);
            Object object7 = arg6.decode(object);
            Object object8 = arg7.decode(object);
            Object object9 = arg8.decode(object);
            Object object10 = arg9.decode(object);
            Object object11 = arg10.decode(object);
            return function72.apply((T1) object2, (T2) object3, (T3) object4, (T4) object5, (T5) object6, (T6) object7, (T7) object8, (T8) object9, (T9) object10, (T10) object11);
        }

        @Override
        public void encode(B object, C object2) {
            arg.encode(object, function.apply(object2));
            arg2.encode(object, function2.apply(object2));
            arg3.encode(object, function3.apply(object2));
            arg4.encode(object, function4.apply(object2));
            arg5.encode(object, function5.apply(object2));
            arg6.encode(object, function6.apply(object2));
            arg7.encode(object, function7.apply(object2));
            arg8.encode(object, function8.apply(object2));
            arg9.encode(object, function9.apply(object2));
            arg10.encode(object, function10.apply(object2));
        }
    };
  }

  public static final StreamCodec<ByteBuf, TagKey<Item>> ITEM_TAG_STREAM_CODEC = tagStreamCodec(Registries.ITEM);
  public static final StreamCodec<ByteBuf, TagKey<Block>> BLOCK_TAG_STREAM_CODEC = tagStreamCodec(Registries.BLOCK);
  public static final StreamCodec<ByteBuf, TagKey<EntityType<?>>> ENTITY_TAG_STREAM_CODEC = tagStreamCodec(Registries.ENTITY_TYPE);
  public static final StreamCodec<ByteBuf, TagKey<Fluid>> FLUID_TAG_STREAM_CODEC = tagStreamCodec(Registries.FLUID);

  public static final StreamCodec<ByteBuf, InteractionHand> INTERACTION_HAND_CODEC = ByteBufCodecs.VAR_INT.map(o -> o == 0 ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND, Enum::ordinal);

  public static <T> StreamCodec<ByteBuf, TagKey<T>> tagStreamCodec(ResourceKey<Registry<T>> registry) {
    return ResourceLocation.STREAM_CODEC.map(o -> TagKey.create(registry, o), TagKey::location);
  }

  public static <O> Codec<Optional<O>> optionalCodec(Codec<O> codec) {
    return Codec.either(
            codec,
            Codec.unit(Unit.INSTANCE)
    ).xmap(
            e -> e.map(Optional::of, u -> Optional.empty()),
            o -> o.map(Either::<O, Unit>left).orElse(Either.right(Unit.INSTANCE))
    );
  }
}