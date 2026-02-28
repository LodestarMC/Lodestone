package team.lodestar.lodestone.core.command;

import com.mojang.datafixers.util.*;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class CommandGroups {
    
    public static final class Group0<O> {
        public CommandCodec<O> apply(CommandCodecBuilder.Instance<O> ignored, Supplier<O> constructor) {
            return new CompositeCodecs.CompositeCodec0<>(constructor);
        }
    }

    public static final class Group1<O, A> {
        private final CommandField<?, A> a;
        Group1(CommandField<?, A> a) { this.a = a; }

        public CommandCodec<O> apply(CommandCodecBuilder.Instance<O> ignored, Function<A, O> constructor) {
            return new CompositeCodecs.CompositeCodec1<>(a, constructor);
        }
    }

    public static final class Group2<O, A, B> {
        private final CommandField<?, A> a;
        private final CommandField<?, B> b;
        Group2(CommandField<?, A> a, CommandField<?, B> b) { this.a = a; this.b = b; }

        public CommandCodec<O> apply(CommandCodecBuilder.Instance<O> ignored, BiFunction<A, B, O> constructor) {
            return new CompositeCodecs.CompositeCodec2<>(a, b, constructor);
        }
    }

    public static final class Group3<O, A, B, C> {
        private final CommandField<?, A> a;
        private final CommandField<?, B> b;
        private final CommandField<?, C> c;
        Group3(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c) { this.a = a; this.b = b; this.c = c; }

        public CommandCodec<O> apply(CommandCodecBuilder.Instance<O> ignored, Function3<A, B, C, O> constructor) {
            return new CompositeCodecs.CompositeCodec3<>(a, b, c, constructor);
        }
    }

    public static final class Group4<O, A, B, C, D> {
        private final CommandField<?, A> a;
        private final CommandField<?, B> b;
        private final CommandField<?, C> c;
        private final CommandField<?, D> d;
        Group4(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d) { this.a = a; this.b = b; this.c = c; this.d = d; }

        public CommandCodec<O> apply(CommandCodecBuilder.Instance<O> ignored, Function4<A, B, C, D, O> constructor) {
            return new CompositeCodecs.CompositeCodec4<>(a, b, c, d, constructor);
        }
    }

    public static final class Group5<O, A, B, C, D, E> {
        private final CommandField<?, A> a;
        private final CommandField<?, B> b;
        private final CommandField<?, C> c;
        private final CommandField<?, D> d;
        private final CommandField<?, E> e;
        Group5(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e) { this.a = a; this.b = b; this.c = c; this.d = d; this.e = e; }

        public CommandCodec<O> apply(CommandCodecBuilder.Instance<O> ignored, Function5<A, B, C, D, E, O> constructor) {
            return new CompositeCodecs.CompositeCodec5<>(a, b, c, d, e, constructor);
        }
    }

    public static final class Group6<O, A, B, C, D, E, F> {
        private final CommandField<?, A> a;
        private final CommandField<?, B> b;
        private final CommandField<?, C> c;
        private final CommandField<?, D> d;
        private final CommandField<?, E> e;
        private final CommandField<?, F> f;
        Group6(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e, CommandField<?, F> f) { this.a = a; this.b = b; this.c = c; this.d = d; this.e = e; this.f = f; }

        public CommandCodec<O> apply(CommandCodecBuilder.Instance<O> ignored, Function6<A, B, C, D, E, F, O> constructor) {
            return new CompositeCodecs.CompositeCodec6<>(a, b, c, d, e, f, constructor);
        }
    }

    public static final class Group7<O, A, B, C, D, E, F, G> {
        private final CommandField<?, A> a;
        private final CommandField<?, B> b;
        private final CommandField<?, C> c;
        private final CommandField<?, D> d;
        private final CommandField<?, E> e;
        private final CommandField<?, F> f;
        private final CommandField<?, G> g;
        Group7(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e, CommandField<?, F> f, CommandField<?, G> g) { this.a = a; this.b = b; this.c = c; this.d = d; this.e = e; this.f = f; this.g = g; }

        public CommandCodec<O> apply(CommandCodecBuilder.Instance<O> ignored, Function7<A, B, C, D, E, F, G, O> constructor) {
            return new CompositeCodecs.CompositeCodec7<>(a, b, c, d, e, f, g, constructor);
        }
    }

    public static final class Group8<O, A, B, C, D, E, F, G, H> {
        private final CommandField<?, A> a;
        private final CommandField<?, B> b;
        private final CommandField<?, C> c;
        private final CommandField<?, D> d;
        private final CommandField<?, E> e;
        private final CommandField<?, F> f;
        private final CommandField<?, G> g;
        private final CommandField<?, H> h;
        Group8(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e, CommandField<?, F> f, CommandField<?, G> g, CommandField<?, H> h) { this.a = a; this.b = b; this.c = c; this.d = d; this.e = e; this.f = f; this.g = g; this.h = h; }

        public CommandCodec<O> apply(CommandCodecBuilder.Instance<O> ignored, Function8<A, B, C, D, E, F, G, H, O> constructor) {
            return new CompositeCodecs.CompositeCodec8<>(a, b, c, d, e, f, g, h, constructor);
        }
    }

    public static final class Group9<O, A, B, C, D, E, F, G, H, I> {
        private final CommandField<?, A> a;
        private final CommandField<?, B> b;
        private final CommandField<?, C> c;
        private final CommandField<?, D> d;
        private final CommandField<?, E> e;
        private final CommandField<?, F> f;
        private final CommandField<?, G> g;
        private final CommandField<?, H> h;
        private final CommandField<?, I> i;
        Group9(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e, CommandField<?, F> f, CommandField<?, G> g, CommandField<?, H> h, CommandField<?, I> i) { this.a = a; this.b = b; this.c = c; this.d = d; this.e = e; this.f = f; this.g = g; this.h = h; this.i = i; }

        public CommandCodec<O> apply(CommandCodecBuilder.Instance<O> ignored, Function9<A, B, C, D, E, F, G, H, I, O> constructor) {
            return new CompositeCodecs.CompositeCodec9<>(a, b, c, d, e, f, g, h, i, constructor);
        }
    }

    public static final class Group10<O, A, B, C, D, E, F, G, H, I, J> {
        private final CommandField<?, A> a;
        private final CommandField<?, B> b;
        private final CommandField<?, C> c;
        private final CommandField<?, D> d;
        private final CommandField<?, E> e;
        private final CommandField<?, F> f;
        private final CommandField<?, G> g;
        private final CommandField<?, H> h;
        private final CommandField<?, I> i;
        private final CommandField<?, J> j;
        Group10(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e, CommandField<?, F> f, CommandField<?, G> g, CommandField<?, H> h, CommandField<?, I> i, CommandField<?, J> j) { this.a = a; this.b = b; this.c = c; this.d = d; this.e = e; this.f = f; this.g = g; this.h = h; this.i = i; this.j = j; }

        public CommandCodec<O> apply(CommandCodecBuilder.Instance<O> ignored, Function10<A, B, C, D, E, F, G, H, I, J, O> constructor) {
            return new CompositeCodecs.CompositeCodec10<>(a, b, c, d, e, f, g, h, i, j, constructor);
        }
    }

    public static final class Group11<O, A, B, C, D, E, F, G, H, I, J, K> {
        private final CommandField<?, A> a;
        private final CommandField<?, B> b;
        private final CommandField<?, C> c;
        private final CommandField<?, D> d;
        private final CommandField<?, E> e;
        private final CommandField<?, F> f;
        private final CommandField<?, G> g;
        private final CommandField<?, H> h;
        private final CommandField<?, I> i;
        private final CommandField<?, J> j;
        private final CommandField<?, K> k;
        Group11(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e, CommandField<?, F> f, CommandField<?, G> g, CommandField<?, H> h, CommandField<?, I> i, CommandField<?, J> j, CommandField<?, K> k) { this.a = a; this.b = b; this.c = c; this.d = d; this.e = e; this.f = f; this.g = g; this.h = h; this.i = i; this.j = j; this.k = k; }

        public CommandCodec<O> apply(CommandCodecBuilder.Instance<O> ignored, Function11<A, B, C, D, E, F, G, H, I, J, K, O> constructor) {
            return new CompositeCodecs.CompositeCodec11<>(a, b, c, d, e, f, g, h, i, j, k, constructor);
        }
    }

    public static final class Group12<O, A, B, C, D, E, F, G, H, I, J, K, L> {
        private final CommandField<?, A> a;
        private final CommandField<?, B> b;
        private final CommandField<?, C> c;
        private final CommandField<?, D> d;
        private final CommandField<?, E> e;
        private final CommandField<?, F> f;
        private final CommandField<?, G> g;
        private final CommandField<?, H> h;
        private final CommandField<?, I> i;
        private final CommandField<?, J> j;
        private final CommandField<?, K> k;
        private final CommandField<?, L> l;
        Group12(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e, CommandField<?, F> f, CommandField<?, G> g, CommandField<?, H> h, CommandField<?, I> i, CommandField<?, J> j, CommandField<?, K> k, CommandField<?, L> l) { this.a = a; this.b = b; this.c = c; this.d = d; this.e = e; this.f = f; this.g = g; this.h = h; this.i = i; this.j = j; this.k = k; this.l = l; }

        public CommandCodec<O> apply(CommandCodecBuilder.Instance<O> ignored, Function12<A, B, C, D, E, F, G, H, I, J, K, L, O> constructor) {
            return new CompositeCodecs.CompositeCodec12<>(a, b, c, d, e, f, g, h, i, j, k, l, constructor);
        }
    }

    public static final class Group13<O, A, B, C, D, E, F, G, H, I, J, K, L, M> {
        private final CommandField<?, A> a;
        private final CommandField<?, B> b;
        private final CommandField<?, C> c;
        private final CommandField<?, D> d;
        private final CommandField<?, E> e;
        private final CommandField<?, F> f;
        private final CommandField<?, G> g;
        private final CommandField<?, H> h;
        private final CommandField<?, I> i;
        private final CommandField<?, J> j;
        private final CommandField<?, K> k;
        private final CommandField<?, L> l;
        private final CommandField<?, M> m;
        Group13(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e, CommandField<?, F> f, CommandField<?, G> g, CommandField<?, H> h, CommandField<?, I> i, CommandField<?, J> j, CommandField<?, K> k, CommandField<?, L> l, CommandField<?, M> m) { this.a = a; this.b = b; this.c = c; this.d = d; this.e = e; this.f = f; this.g = g; this.h = h; this.i = i; this.j = j; this.k = k; this.l = l; this.m = m; }

        public CommandCodec<O> apply(CommandCodecBuilder.Instance<O> ignored, Function13<A, B, C, D, E, F, G, H, I, J, K, L, M, O> constructor) {
            return new CompositeCodecs.CompositeCodec13<>(a, b, c, d, e, f, g, h, i, j, k, l, m, constructor);
        }
    }

    public static final class Group14<O, A, B, C, D, E, F, G, H, I, J, K, L, M, N> {
        private final CommandField<?, A> a;
        private final CommandField<?, B> b;
        private final CommandField<?, C> c;
        private final CommandField<?, D> d;
        private final CommandField<?, E> e;
        private final CommandField<?, F> f;
        private final CommandField<?, G> g;
        private final CommandField<?, H> h;
        private final CommandField<?, I> i;
        private final CommandField<?, J> j;
        private final CommandField<?, K> k;
        private final CommandField<?, L> l;
        private final CommandField<?, M> m;
        private final CommandField<?, N> n;
        Group14(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e, CommandField<?, F> f, CommandField<?, G> g, CommandField<?, H> h, CommandField<?, I> i, CommandField<?, J> j, CommandField<?, K> k, CommandField<?, L> l, CommandField<?, M> m, CommandField<?, N> n) { this.a = a; this.b = b; this.c = c; this.d = d; this.e = e; this.f = f; this.g = g; this.h = h; this.i = i; this.j = j; this.k = k; this.l = l; this.m = m; this.n = n; }

        public CommandCodec<O> apply(CommandCodecBuilder.Instance<O> ignored, Function14<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> constructor) {
            return new CompositeCodecs.CompositeCodec14<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, constructor);
        }
    }

    public static final class Group15<O, A, B, C, D, E, F, G, H, I, J, K, L, M, N, P> {
        private final CommandField<?, A> a;
        private final CommandField<?, B> b;
        private final CommandField<?, C> c;
        private final CommandField<?, D> d;
        private final CommandField<?, E> e;
        private final CommandField<?, F> f;
        private final CommandField<?, G> g;
        private final CommandField<?, H> h;
        private final CommandField<?, I> i;
        private final CommandField<?, J> j;
        private final CommandField<?, K> k;
        private final CommandField<?, L> l;
        private final CommandField<?, M> m;
        private final CommandField<?, N> n;
        private final CommandField<?, P> p;
        Group15(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e, CommandField<?, F> f, CommandField<?, G> g, CommandField<?, H> h, CommandField<?, I> i, CommandField<?, J> j, CommandField<?, K> k, CommandField<?, L> l, CommandField<?, M> m, CommandField<?, N> n, CommandField<?, P> p) { this.a = a; this.b = b; this.c = c; this.d = d; this.e = e; this.f = f; this.g = g; this.h = h; this.i = i; this.j = j; this.k = k; this.l = l; this.m = m; this.n = n; this.p = p; }

        public CommandCodec<O> apply(CommandCodecBuilder.Instance<O> ignored, Function15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, P, O> constructor) {
            return new CompositeCodecs.CompositeCodec15<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, p, constructor);
        }
    }

    public static final class Group16<O, A, B, C, D, E, F, G, H, I, J, K, L, M, N, P, Q> {
        private final CommandField<?, A> a;
        private final CommandField<?, B> b;
        private final CommandField<?, C> c;
        private final CommandField<?, D> d;
        private final CommandField<?, E> e;
        private final CommandField<?, F> f;
        private final CommandField<?, G> g;
        private final CommandField<?, H> h;
        private final CommandField<?, I> i;
        private final CommandField<?, J> j;
        private final CommandField<?, K> k;
        private final CommandField<?, L> l;
        private final CommandField<?, M> m;
        private final CommandField<?, N> n;
        private final CommandField<?, P> p;
        private final CommandField<?, Q> q;
        Group16(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e, CommandField<?, F> f, CommandField<?, G> g, CommandField<?, H> h, CommandField<?, I> i, CommandField<?, J> j, CommandField<?, K> k, CommandField<?, L> l, CommandField<?, M> m, CommandField<?, N> n, CommandField<?, P> p, CommandField<?, Q> q) { this.a = a; this.b = b; this.c = c; this.d = d; this.e = e; this.f = f; this.g = g; this.h = h; this.i = i; this.j = j; this.k = k; this.l = l; this.m = m; this.n = n; this.p = p; this.q = q; }

        public CommandCodec<O> apply(CommandCodecBuilder.Instance<O> ignored, Function16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, P, Q, O> constructor) {
            return new CompositeCodecs.CompositeCodec16<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, p, q, constructor);
        }
    }
}
