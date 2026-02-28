package team.lodestar.lodestone.core.command;

import java.util.function.Function;

public final class CommandCodecBuilder {
    private CommandCodecBuilder() {}

    public static <O> CommandCodec<O> create(Function<Instance<O>, CommandCodec<O>> builder) {
        return builder.apply(new Instance<>());
    }

    public static final class Instance<O> {

        public CommandGroups.Group0<O> group() {
            return new CommandGroups.Group0<>();
        }

        public <A> CommandGroups.Group1<O, A> group(CommandField<?, A> a) {
            return new CommandGroups.Group1<>(a);
        }

        public <A, B> CommandGroups.Group2<O, A, B> group(CommandField<?, A> a, CommandField<?, B> b) {
            return new CommandGroups.Group2<>(a, b);
        }

        public <A, B, C> CommandGroups.Group3<O, A, B, C> group(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c) {
            return new CommandGroups.Group3<>(a, b, c);
        }

        public <A, B, C, D> CommandGroups.Group4<O, A, B, C, D> group(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d) {
            return new CommandGroups.Group4<>(a, b, c, d);
        }

        public <A, B, C, D, E> CommandGroups.Group5<O, A, B, C, D, E> group(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e) {
            return new CommandGroups.Group5<>(a, b, c, d, e);
        }

        public <A, B, C, D, E, F> CommandGroups.Group6<O, A, B, C, D, E, F> group(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e, CommandField<?, F> f) {
            return new CommandGroups.Group6<>(a, b, c, d, e, f);
        }

        public <A, B, C, D, E, F, G> CommandGroups.Group7<O, A, B, C, D, E, F, G> group(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e, CommandField<?, F> f, CommandField<?, G> g) {
            return new CommandGroups.Group7<>(a, b, c, d, e, f, g);
        }

        public <A, B, C, D, E, F, G, H> CommandGroups.Group8<O, A, B, C, D, E, F, G, H> group(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e, CommandField<?, F> f, CommandField<?, G> g, CommandField<?, H> h) {
            return new CommandGroups.Group8<>(a, b, c, d, e, f, g, h);
        }

        public <A, B, C, D, E, F, G, H, I> CommandGroups.Group9<O, A, B, C, D, E, F, G, H, I> group(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e, CommandField<?, F> f, CommandField<?, G> g, CommandField<?, H> h, CommandField<?, I> i) {
            return new CommandGroups.Group9<>(a, b, c, d, e, f, g, h, i);
        }

        public <A, B, C, D, E, F, G, H, I, J> CommandGroups.Group10<O, A, B, C, D, E, F, G, H, I, J> group(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e, CommandField<?, F> f, CommandField<?, G> g, CommandField<?, H> h, CommandField<?, I> i, CommandField<?, J> j) {
            return new CommandGroups.Group10<>(a, b, c, d, e, f, g, h, i, j);
        }

        public <A, B, C, D, E, F, G, H, I, J, K> CommandGroups.Group11<O, A, B, C, D, E, F, G, H, I, J, K> group(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e, CommandField<?, F> f, CommandField<?, G> g, CommandField<?, H> h, CommandField<?, I> i, CommandField<?, J> j, CommandField<?, K> k) {
            return new CommandGroups.Group11<>(a, b, c, d, e, f, g, h, i, j, k);
        }

        public <A, B, C, D, E, F, G, H, I, J, K, L> CommandGroups.Group12<O, A, B, C, D, E, F, G, H, I, J, K, L> group(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e, CommandField<?, F> f, CommandField<?, G> g, CommandField<?, H> h, CommandField<?, I> i, CommandField<?, J> j, CommandField<?, K> k, CommandField<?, L> l) {
            return new CommandGroups.Group12<>(a, b, c, d, e, f, g, h, i, j, k, l);
        }

        public <A, B, C, D, E, F, G, H, I, J, K, L, M> CommandGroups.Group13<O, A, B, C, D, E, F, G, H, I, J, K, L, M> group(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e, CommandField<?, F> f, CommandField<?, G> g, CommandField<?, H> h, CommandField<?, I> i, CommandField<?, J> j, CommandField<?, K> k, CommandField<?, L> l, CommandField<?, M> m) {
            return new CommandGroups.Group13<>(a, b, c, d, e, f, g, h, i, j, k, l, m);
        }

        public <A, B, C, D, E, F, G, H, I, J, K, L, M, N> CommandGroups.Group14<O, A, B, C, D, E, F, G, H, I, J, K, L, M, N> group(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e, CommandField<?, F> f, CommandField<?, G> g, CommandField<?, H> h, CommandField<?, I> i, CommandField<?, J> j, CommandField<?, K> k, CommandField<?, L> l, CommandField<?, M> m, CommandField<?, N> n) {
            return new CommandGroups.Group14<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n);
        }

        public <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O1> CommandGroups.Group15<O, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O1> group(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e, CommandField<?, F> f, CommandField<?, G> g, CommandField<?, H> h, CommandField<?, I> i, CommandField<?, J> j, CommandField<?, K> k, CommandField<?, L> l, CommandField<?, M> m, CommandField<?, N> n, CommandField<?, O1> o1) {
            return new CommandGroups.Group15<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o1);
        }

        public <A, B, C, D, E, F, G, H, I, J, K, L, M, N, O1, P> CommandGroups.Group16<O, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O1, P> group(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e, CommandField<?, F> f, CommandField<?, G> g, CommandField<?, H> h, CommandField<?, I> i, CommandField<?, J> j, CommandField<?, K> k, CommandField<?, L> l, CommandField<?, M> m, CommandField<?, N> n, CommandField<?, O1> o1, CommandField<?, P> p) {
            return new CommandGroups.Group16<>(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o1, p);
        }
    }
}

