package team.lodestar.lodestone.core.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.datafixers.util.*;
import net.minecraft.commands.CommandSourceStack;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class CompositeCodecs {

    public static class CompositeCodec0<O> implements CommandCodec<O> {
        private final Supplier<O> constructor;

        public CompositeCodec0(Supplier<O> constructor) {
            this.constructor = constructor;
        }

        @Override
        public O decode(CommandContext<CommandSourceStack> ctx) {
            return constructor.get();
        }

        @Override
        public ArgumentBuilder<CommandSourceStack, ?> appendTo(ArgumentBuilder<CommandSourceStack, ?> parent, BiFunction<CommandSourceStack, O, Integer> onExecute) {
            parent.executes(ctx -> onExecute.apply(ctx.getSource(), decode(ctx)));
            return parent;
        }
    }

    public static class CompositeCodec1<O, A> implements CommandCodec<O> {
        protected final CommandField<?, A> a;
        private final Function<A, O> constructor;

        public CompositeCodec1(CommandField<?, A> a, Function<A, O> constructor) {
            this.a = a;
            this.constructor = constructor;
        }

        @Override
        public O decode(CommandContext<CommandSourceStack> ctx) {
            return constructor.apply(a.getValue(ctx));
        }

        @Override
        public ArgumentBuilder<CommandSourceStack, ?> appendTo(ArgumentBuilder<CommandSourceStack, ?> parent, BiFunction<CommandSourceStack, O, Integer> onExecute) {
            var argA = a.getArgument();

            argA.executes(ctx -> onExecute.apply(ctx.getSource(), decode(ctx)));
            parent.then(argA);
            return parent;
        }
    }

    public static class CompositeCodec2<O, A, B> implements CommandCodec<O> {
        protected final CommandField<?, A> a;
        protected final CommandField<?, B> b;
        private final BiFunction<A, B, O> constructor;

        public CompositeCodec2(CommandField<?, A> a, CommandField<?, B> b, BiFunction<A, B, O> constructor) {
            this.a = a;
            this.b = b;
            this.constructor = constructor;
        }

        @Override
        public O decode(CommandContext<net.minecraft.commands.CommandSourceStack> ctx) {
            return constructor.apply(a.getValue(ctx), b.getValue(ctx));
        }

        @Override
        public ArgumentBuilder<CommandSourceStack, ?> appendTo(ArgumentBuilder<CommandSourceStack, ?> parent, BiFunction<CommandSourceStack, O, Integer> onExecute) {
            var argA = a.getArgument();
            var argB = b.getArgument();

            argB.executes(ctx -> onExecute.apply(ctx.getSource(), decode(ctx)));
            argA.then(argB);
            parent.then(argA);
            return parent;
        }
    }

    public static class CompositeCodec3<O, A, B, C> implements CommandCodec<O> {
        protected final CommandField<?, A> a;
        protected final CommandField<?, B> b;
        protected final CommandField<?, C> c;
        private final Function3<A, B, C, O> constructor;

        public CompositeCodec3(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, Function3<A, B, C, O> constructor) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.constructor = constructor;
        }

        @Override
        public O decode(CommandContext<CommandSourceStack> ctx) {
            return constructor.apply(a.getValue(ctx), b.getValue(ctx), c.getValue(ctx));
        }

        @Override
        public ArgumentBuilder<CommandSourceStack, ?> appendTo(ArgumentBuilder<CommandSourceStack, ?> parent, BiFunction<CommandSourceStack, O, Integer> onExecute) {
            var argA =  a.getArgument();
            var argB =  b.getArgument();
            var argC =  c.getArgument();

            argC.executes(ctx -> onExecute.apply(ctx.getSource(), decode(ctx)));
            argB.then(argC);
            argA.then(argB);
            parent.then(argA);
            return parent;
        }
    }

    public static class CompositeCodec4<O, A, B, C, D> implements CommandCodec<O> {
        protected final CommandField<?, A> a;
        protected final CommandField<?, B> b;
        protected final CommandField<?, C> c;
        protected final CommandField<?, D> d;
        private final Function4<A, B, C, D, O> constructor;

        public CompositeCodec4(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, Function4<A, B, C, D, O> constructor) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.constructor = constructor;
        }

        @Override
        public O decode(CommandContext<CommandSourceStack> ctx) {
            return constructor.apply(a.getValue(ctx), b.getValue(ctx), c.getValue(ctx), d.getValue(ctx));
        }

        @Override
        public ArgumentBuilder<CommandSourceStack, ?> appendTo(ArgumentBuilder<CommandSourceStack, ?> parent, BiFunction<CommandSourceStack, O, Integer> onExecute) {
            var argA =  a.getArgument();
            var argB =  b.getArgument();
            var argC =  c.getArgument();
            var argD =  d.getArgument();

            argD.executes(ctx -> onExecute.apply(ctx.getSource(), decode(ctx)));
            argC.then(argD);
            argB.then(argC);
            argA.then(argB);
            parent.then(argA);
            return parent;
        }
    }

    public static class CompositeCodec5<O, A, B, C, D, E> implements CommandCodec<O> {
        protected final CommandField<?, A> a;
        protected final CommandField<?, B> b;
        protected final CommandField<?, C> c;
        protected final CommandField<?, D> d;
        protected final CommandField<?, E> e;
        private final Function5<A, B, C, D, E, O> constructor;

        public CompositeCodec5(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e, Function5<A, B, C, D, E, O> constructor) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
            this.constructor = constructor;
        }

        @Override
        public O decode(CommandContext<CommandSourceStack> ctx) {
            return constructor.apply(a.getValue(ctx), b.getValue(ctx), c.getValue(ctx), d.getValue(ctx), e.getValue(ctx));
        }

        @Override
        public ArgumentBuilder<CommandSourceStack, ?> appendTo(ArgumentBuilder<CommandSourceStack, ?> parent, BiFunction<CommandSourceStack, O, Integer> onExecute) {
            var argA =  a.getArgument();
            var argB =  b.getArgument();
            var argC =  c.getArgument();
            var argD =  d.getArgument();
            var argE =  e.getArgument();

            argE.executes(ctx -> onExecute.apply(ctx.getSource(), decode(ctx)));
            argD.then(argE);
            argC.then(argD);
            argB.then(argC);
            argA.then(argB);
            parent.then(argA);
            return parent;
        }
    }

    public static class CompositeCodec6<O, A, B, C, D, E, F> implements CommandCodec<O> {
        protected final CommandField<?, A> a;
        protected final CommandField<?, B> b;
        protected final CommandField<?, C> c;
        protected final CommandField<?, D> d;
        protected final CommandField<?, E> e;
        protected final CommandField<?, F> f;
        private final Function6<A, B, C, D, E, F, O> constructor;

        public CompositeCodec6(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e, CommandField<?, F> f, Function6<A, B, C, D, E, F, O> constructor) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
            this.f = f;
            this.constructor = constructor;
        }

        @Override
        public O decode(CommandContext<CommandSourceStack> ctx) {
            return constructor.apply(a.getValue(ctx), b.getValue(ctx), c.getValue(ctx), d.getValue(ctx), e.getValue(ctx), f.getValue(ctx));
        }

        @Override
        public ArgumentBuilder<CommandSourceStack, ?> appendTo(ArgumentBuilder<CommandSourceStack, ?> parent, BiFunction<CommandSourceStack, O, Integer> onExecute) {
            var argA =  a.getArgument();
            var argB =  b.getArgument();
            var argC =  c.getArgument();
            var argD =  d.getArgument();
            var argE =  e.getArgument();
            var argF =  f.getArgument();

            argF.executes(ctx -> onExecute.apply(ctx.getSource(), decode(ctx)));
            argE.then(argF);
            argD.then(argE);
            argC.then(argD);
            argB.then(argC);
            argA.then(argB);
            parent.then(argA);
            return parent;
        }
    }

    public static class CompositeCodec7<O, A, B, C, D, E, F, G> implements CommandCodec<O> {
        protected final CommandField<?, A> a;
        protected final CommandField<?, B> b;
        protected final CommandField<?, C> c;
        protected final CommandField<?, D> d;
        protected final CommandField<?, E> e;
        protected final CommandField<?, F> f;
        protected final CommandField<?, G> g;
        private final Function7<A, B, C, D, E, F, G, O> constructor;

        public CompositeCodec7(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e, CommandField<?, F> f, CommandField<?, G> g, Function7<A, B, C, D, E, F, G, O> constructor) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
            this.f = f;
            this.g = g;
            this.constructor = constructor;
        }

        @Override
        public O decode(CommandContext<CommandSourceStack> ctx) {
            return constructor.apply(a.getValue(ctx), b.getValue(ctx), c.getValue(ctx), d.getValue(ctx), e.getValue(ctx), f.getValue(ctx), g.getValue(ctx));
        }

        @Override
        public ArgumentBuilder<CommandSourceStack, ?> appendTo(ArgumentBuilder<CommandSourceStack, ?> parent, BiFunction<CommandSourceStack, O, Integer> onExecute) {
            var argA =  a.getArgument();
            var argB =  b.getArgument();
            var argC =  c.getArgument();
            var argD =  d.getArgument();
            var argE =  e.getArgument();
            var argF =  f.getArgument();
            var argG =  g.getArgument();

            argG.executes(ctx -> onExecute.apply(ctx.getSource(), decode(ctx)));
            argF.then(argG);
            argE.then(argF);
            argD.then(argE);
            argC.then(argD);
            argB.then(argC);
            argA.then(argB);
            parent.then(argA);
            return parent;
        }
    }

    public static class CompositeCodec8<O, A, B, C, D, E, F, G, H> implements CommandCodec<O> {
        protected final CommandField<?, A> a;
        protected final CommandField<?, B> b;
        protected final CommandField<?, C> c;
        protected final CommandField<?, D> d;
        protected final CommandField<?, E> e;
        protected final CommandField<?, F> f;
        protected final CommandField<?, G> g;
        protected final CommandField<?, H> h;
        private final Function8<A, B, C, D, E, F, G, H, O> constructor;

        public CompositeCodec8(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e, CommandField<?, F> f, CommandField<?, G> g, CommandField<?, H> h, Function8<A, B, C, D, E, F, G, H, O> constructor) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
            this.f = f;
            this.g = g;
            this.h = h;
            this.constructor = constructor;
        }

        @Override
        public O decode(CommandContext<CommandSourceStack> ctx) {
            return constructor.apply(a.getValue(ctx), b.getValue(ctx), c.getValue(ctx), d.getValue(ctx), e.getValue(ctx), f.getValue(ctx), g.getValue(ctx), h.getValue(ctx));
        }

        @Override
        public ArgumentBuilder<CommandSourceStack, ?> appendTo(ArgumentBuilder<CommandSourceStack, ?> parent, BiFunction<CommandSourceStack, O, Integer> onExecute) {
            var argA =  a.getArgument();
            var argB =  b.getArgument();
            var argC =  c.getArgument();
            var argD =  d.getArgument();
            var argE =  e.getArgument();
            var argF =  f.getArgument();
            var argG =  g.getArgument();
            var argH =  h.getArgument();

            argH.executes(ctx -> onExecute.apply(ctx.getSource(), decode(ctx)));
            argG.then(argH);
            argF.then(argG);
            argE.then(argF);
            argD.then(argE);
            argC.then(argD);
            argB.then(argC);
            argA.then(argB);
            parent.then(argA);
            return parent;
        }
    }

    public static class CompositeCodec9<O, A, B, C, D, E, F, G, H, I> implements CommandCodec<O> {
        protected final CommandField<?, A> a;
        protected final CommandField<?, B> b;
        protected final CommandField<?, C> c;
        protected final CommandField<?, D> d;
        protected final CommandField<?, E> e;
        protected final CommandField<?, F> f;
        protected final CommandField<?, G> g;
        protected final CommandField<?, H> h;
        protected final CommandField<?, I> i;
        private final Function9<A, B, C, D, E, F, G, H, I, O> constructor;

        public CompositeCodec9(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e, CommandField<?, F> f, CommandField<?, G> g, CommandField<?, H> h, CommandField<?, I> i, Function9<A, B, C, D, E, F, G, H, I, O> constructor) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
            this.f = f;
            this.g = g;
            this.h = h;
            this.i = i;
            this.constructor = constructor;
        }

        @Override
        public O decode(CommandContext<CommandSourceStack> ctx) {
            return constructor.apply(a.getValue(ctx), b.getValue(ctx), c.getValue(ctx), d.getValue(ctx), e.getValue(ctx), f.getValue(ctx), g.getValue(ctx), h.getValue(ctx), i.getValue(ctx));
        }

        @Override
        public ArgumentBuilder<CommandSourceStack, ?> appendTo(ArgumentBuilder<CommandSourceStack, ?> parent, BiFunction<CommandSourceStack, O, Integer> onExecute) {
            var argA =  a.getArgument();
            var argB =  b.getArgument();
            var argC =  c.getArgument();
            var argD =  d.getArgument();
            var argE =  e.getArgument();
            var argF =  f.getArgument();
            var argG =  g.getArgument();
            var argH =  h.getArgument();
            var argI =  i.getArgument();

            argI.executes(ctx -> onExecute.apply(ctx.getSource(), decode(ctx)));
            argH.then(argI);
            argG.then(argH);
            argF.then(argG);
            argE.then(argF);
            argD.then(argE);
            argC.then(argD);
            argB.then(argC);
            argA.then(argB);
            parent.then(argA);
            return parent;
        }
    }

    public static class CompositeCodec10<O, A, B, C, D, E, F, G, H, I, J> implements CommandCodec<O> {
        protected final CommandField<?, A> a;
        protected final CommandField<?, B> b;
        protected final CommandField<?, C> c;
        protected final CommandField<?, D> d;
        protected final CommandField<?, E> e;
        protected final CommandField<?, F> f;
        protected final CommandField<?, G> g;
        protected final CommandField<?, H> h;
        protected final CommandField<?, I> i;
        protected final CommandField<?, J> j;
        private final Function10<A, B, C, D, E, F, G, H, I, J, O> constructor;

        public CompositeCodec10(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e, CommandField<?, F> f, CommandField<?, G> g, CommandField<?, H> h, CommandField<?, I> i, CommandField<?, J> j, Function10<A, B, C, D, E, F, G, H, I, J, O> constructor) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
            this.f = f;
            this.g = g;
            this.h = h;
            this.i = i;
            this.j = j;
            this.constructor = constructor;
        }

        @Override
        public O decode(CommandContext<CommandSourceStack> ctx) {
            return constructor.apply(a.getValue(ctx), b.getValue(ctx), c.getValue(ctx), d.getValue(ctx), e.getValue(ctx), f.getValue(ctx), g.getValue(ctx), h.getValue(ctx), i.getValue(ctx), j.getValue(ctx));
        }

        @Override
        public ArgumentBuilder<CommandSourceStack, ?> appendTo(ArgumentBuilder<CommandSourceStack, ?> parent, BiFunction<CommandSourceStack, O, Integer> onExecute) {
            var argA =  a.getArgument();
            var argB =  b.getArgument();
            var argC =  c.getArgument();
            var argD =  d.getArgument();
            var argE =  e.getArgument();
            var argF =  f.getArgument();
            var argG =  g.getArgument();
            var argH =  h.getArgument();
            var argI =  i.getArgument();
            var argJ =  j.getArgument();

            argJ.executes(ctx -> onExecute.apply(ctx.getSource(), decode(ctx)));
            argI.then(argJ);
            argH.then(argI);
            argG.then(argH);
            argF.then(argG);
            argE.then(argF);
            argD.then(argE);
            argC.then(argD);
            argB.then(argC);
            argA.then(argB);
            parent.then(argA);
            return parent;
        }
    }

    public static class CompositeCodec11<O, A, B, C, D, E, F, G, H, I, J, K> implements CommandCodec<O> {
        protected final CommandField<?, A> a;
        protected final CommandField<?, B> b;
        protected final CommandField<?, C> c;
        protected final CommandField<?, D> d;
        protected final CommandField<?, E> e;
        protected final CommandField<?, F> f;
        protected final CommandField<?, G> g;
        protected final CommandField<?, H> h;
        protected final CommandField<?, I> i;
        protected final CommandField<?, J> j;
        protected final CommandField<?, K> k;
        private final Function11<A, B, C, D, E, F, G, H, I, J, K, O> constructor;

        public CompositeCodec11(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e, CommandField<?, F> f, CommandField<?, G> g, CommandField<?, H> h, CommandField<?, I> i, CommandField<?, J> j, CommandField<?, K> k, Function11<A, B, C, D, E, F, G, H, I, J, K, O> constructor) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
            this.f = f;
            this.g = g;
            this.h = h;
            this.i = i;
            this.j = j;
            this.k = k;
            this.constructor = constructor;
        }

        @Override
        public O decode(CommandContext<CommandSourceStack> ctx) {
            return constructor.apply(a.getValue(ctx), b.getValue(ctx), c.getValue(ctx), d.getValue(ctx), e.getValue(ctx), f.getValue(ctx), g.getValue(ctx), h.getValue(ctx), i.getValue(ctx), j.getValue(ctx), k.getValue(ctx));
        }

        @Override
        public ArgumentBuilder<CommandSourceStack, ?> appendTo(ArgumentBuilder<CommandSourceStack, ?> parent, BiFunction<CommandSourceStack, O, Integer> onExecute) {
            var argA =  a.getArgument();
            var argB =  b.getArgument();
            var argC =  c.getArgument();
            var argD =  d.getArgument();
            var argE =  e.getArgument();
            var argF =  f.getArgument();
            var argG =  g.getArgument();
            var argH =  h.getArgument();
            var argI =  i.getArgument();
            var argJ =  j.getArgument();
            var argK =  k.getArgument();

            argK.executes(ctx -> onExecute.apply(ctx.getSource(), decode(ctx)));
            argJ.then(argK);
            argI.then(argJ);
            argH.then(argI);
            argG.then(argH);
            argF.then(argG);
            argE.then(argF);
            argD.then(argE);
            argC.then(argD);
            argB.then(argC);
            argA.then(argB);
            parent.then(argA);
            return parent;
        }
    }

    public static class CompositeCodec12<O, A, B, C, D, E, F, G, H, I, J, K, L> implements CommandCodec<O> {
        protected final CommandField<?, A> a;
        protected final CommandField<?, B> b;
        protected final CommandField<?, C> c;
        protected final CommandField<?, D> d;
        protected final CommandField<?, E> e;
        protected final CommandField<?, F> f;
        protected final CommandField<?, G> g;
        protected final CommandField<?, H> h;
        protected final CommandField<?, I> i;
        protected final CommandField<?, J> j;
        protected final CommandField<?, K> k;
        protected final CommandField<?, L> l;
        private final Function12<A, B, C, D, E, F, G, H, I, J, K, L, O> constructor;

        public CompositeCodec12(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e, CommandField<?, F> f, CommandField<?, G> g, CommandField<?, H> h, CommandField<?, I> i, CommandField<?, J> j, CommandField<?, K> k, CommandField<?, L> l, Function12<A, B, C, D, E, F, G, H, I, J, K, L, O> constructor) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
            this.f = f;
            this.g = g;
            this.h = h;
            this.i = i;
            this.j = j;
            this.k = k;
            this.l = l;
            this.constructor = constructor;
        }

        @Override
        public O decode(CommandContext<CommandSourceStack> ctx) {
            return constructor.apply(a.getValue(ctx), b.getValue(ctx), c.getValue(ctx), d.getValue(ctx), e.getValue(ctx), f.getValue(ctx), g.getValue(ctx), h.getValue(ctx), i.getValue(ctx), j.getValue(ctx), k.getValue(ctx), l.getValue(ctx));
        }

        @Override
        public ArgumentBuilder<CommandSourceStack, ?> appendTo(ArgumentBuilder<CommandSourceStack, ?> parent, BiFunction<CommandSourceStack, O, Integer> onExecute) {
            var argA =  a.getArgument();
            var argB =  b.getArgument();
            var argC =  c.getArgument();
            var argD =  d.getArgument();
            var argE =  e.getArgument();
            var argF =  f.getArgument();
            var argG =  g.getArgument();
            var argH =  h.getArgument();
            var argI =  i.getArgument();
            var argJ =  j.getArgument();
            var argK =  k.getArgument();
            var argL =  l.getArgument();

            argL.executes(ctx -> onExecute.apply(ctx.getSource(), decode(ctx)));
            argK.then(argL);
            argJ.then(argK);
            argI.then(argJ);
            argH.then(argI);
            argG.then(argH);
            argF.then(argG);
            argE.then(argF);
            argD.then(argE);
            argC.then(argD);
            argB.then(argC);
            argA.then(argB);
            parent.then(argA);
            return parent;
        }
    }

    public static class CompositeCodec13<O, A, B, C, D, E, F, G, H, I, J, K, L, M> implements CommandCodec<O> {
        protected final CommandField<?, A> a;
        protected final CommandField<?, B> b;
        protected final CommandField<?, C> c;
        protected final CommandField<?, D> d;
        protected final CommandField<?, E> e;
        protected final CommandField<?, F> f;
        protected final CommandField<?, G> g;
        protected final CommandField<?, H> h;
        protected final CommandField<?, I> i;
        protected final CommandField<?, J> j;
        protected final CommandField<?, K> k;
        protected final CommandField<?, L> l;
        protected final CommandField<?, M> m;
        private final Function13<A, B, C, D, E, F, G, H, I, J, K, L, M, O> constructor;

        public CompositeCodec13(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e, CommandField<?, F> f, CommandField<?, G> g, CommandField<?, H> h, CommandField<?, I> i, CommandField<?, J> j, CommandField<?, K> k, CommandField<?, L> l, CommandField<?, M> m, Function13<A, B, C, D, E, F, G, H, I, J, K, L, M, O> constructor) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
            this.f = f;
            this.g = g;
            this.h = h;
            this.i = i;
            this.j = j;
            this.k = k;
            this.l = l;
            this.m = m;
            this.constructor = constructor;
        }

        @Override
        public O decode(CommandContext<CommandSourceStack> ctx) {
            return constructor.apply(a.getValue(ctx), b.getValue(ctx), c.getValue(ctx), d.getValue(ctx), e.getValue(ctx), f.getValue(ctx), g.getValue(ctx), h.getValue(ctx), i.getValue(ctx), j.getValue(ctx), k.getValue(ctx), l.getValue(ctx), m.getValue(ctx));
        }

        @Override
        public ArgumentBuilder<CommandSourceStack, ?> appendTo(ArgumentBuilder<CommandSourceStack, ?> parent, BiFunction<CommandSourceStack, O, Integer> onExecute) {
            var argA = a.getArgument();
            var argB = b.getArgument();
            var argC = c.getArgument();
            var argD = d.getArgument();
            var argE = e.getArgument();
            var argF = f.getArgument();
            var argG = g.getArgument();
            var argH = h.getArgument();
            var argI = i.getArgument();
            var argJ = j.getArgument();
            var argK = k.getArgument();
            var argL = l.getArgument();
            var argM = m.getArgument();

            argM.executes(ctx -> onExecute.apply(ctx.getSource(), decode(ctx)));
            argL.then(argM);
            argK.then(argL);
            argJ.then(argK);
            argI.then(argJ);
            argH.then(argI);
            argG.then(argH);
            argF.then(argG);
            argE.then(argF);
            argD.then(argE);
            argC.then(argD);
            argB.then(argC);
            argA.then(argB);
            parent.then(argA);
            return parent;
        }
    }

    public static class CompositeCodec14<O, A, B, C, D, E, F, G, H, I, J, K, L, M, N> implements CommandCodec<O> {
        protected final CommandField<?, A> a;
        protected final CommandField<?, B> b;
        protected final CommandField<?, C> c;
        protected final CommandField<?, D> d;
        protected final CommandField<?, E> e;
        protected final CommandField<?, F> f;
        protected final CommandField<?, G> g;
        protected final CommandField<?, H> h;
        protected final CommandField<?, I> i;
        protected final CommandField<?, J> j;
        protected final CommandField<?, K> k;
        protected final CommandField<?, L> l;
        protected final CommandField<?, M> m;
        protected final CommandField<?, N> n;
        private final Function14<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> constructor;

        public CompositeCodec14(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e, CommandField<?, F> f, CommandField<?, G> g, CommandField<?, H> h, CommandField<?, I> i, CommandField<?, J> j, CommandField<?, K> k, CommandField<?, L> l, CommandField<?, M> m, CommandField<?, N> n, Function14<A, B, C, D, E, F, G, H, I, J, K, L, M, N, O> constructor) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
            this.f = f;
            this.g = g;
            this.h = h;
            this.i = i;
            this.j = j;
            this.k = k;
            this.l = l;
            this.m = m;
            this.n = n;
            this.constructor = constructor;
        }

        @Override
        public O decode(CommandContext<CommandSourceStack> ctx) {
            return constructor.apply(a.getValue(ctx), b.getValue(ctx), c.getValue(ctx), d.getValue(ctx), e.getValue(ctx), f.getValue(ctx), g.getValue(ctx), h.getValue(ctx), i.getValue(ctx), j.getValue(ctx), k.getValue(ctx), l.getValue(ctx), m.getValue(ctx), n.getValue(ctx));
        }

        @Override
        public ArgumentBuilder<CommandSourceStack, ?> appendTo(ArgumentBuilder<CommandSourceStack, ?> parent, BiFunction<CommandSourceStack, O, Integer> onExecute) {
            var argA = a.getArgument();
            var argB = b.getArgument();
            var argC = c.getArgument();
            var argD = d.getArgument();
            var argE = e.getArgument();
            var argF = f.getArgument();
            var argG = g.getArgument();
            var argH = h.getArgument();
            var argI = i.getArgument();
            var argJ = j.getArgument();
            var argK = k.getArgument();
            var argL = l.getArgument();
            var argM = m.getArgument();
            var argN = n.getArgument();

            argN.executes(ctx -> onExecute.apply(ctx.getSource(), decode(ctx)));
            argM.then(argN);
            argL.then(argM);
            argK.then(argL);
            argJ.then(argK);
            argI.then(argJ);
            argH.then(argI);
            argG.then(argH);
            argF.then(argG);
            argE.then(argF);
            argD.then(argE);
            argC.then(argD);
            argB.then(argC);
            argA.then(argB);
            parent.then(argA);
            return parent;
        }
    }

    public static class CompositeCodec15<O, A, B, C, D, E, F, G, H, I, J, K, L, M, N, P> implements CommandCodec<O> {
        protected final CommandField<?, A> a;
        protected final CommandField<?, B> b;
        protected final CommandField<?, C> c;
        protected final CommandField<?, D> d;
        protected final CommandField<?, E> e;
        protected final CommandField<?, F> f;
        protected final CommandField<?, G> g;
        protected final CommandField<?, H> h;
        protected final CommandField<?, I> i;
        protected final CommandField<?, J> j;
        protected final CommandField<?, K> k;
        protected final CommandField<?, L> l;
        protected final CommandField<?, M> m;
        protected final CommandField<?, N> n;
        protected final CommandField<?, P> p;
        private final Function15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, P, O> constructor;

        public CompositeCodec15(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e, CommandField<?, F> f, CommandField<?, G> g, CommandField<?, H> h, CommandField<?, I> i, CommandField<?, J> j, CommandField<?, K> k, CommandField<?, L> l, CommandField<?, M> m, CommandField<?, N> n, CommandField<?, P> p, Function15<A, B, C, D, E, F, G, H, I, J, K, L, M, N, P, O> constructor) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
            this.f = f;
            this.g = g;
            this.h = h;
            this.i = i;
            this.j = j;
            this.k = k;
            this.l = l;
            this.m = m;
            this.n = n;
            this.p = p;
            this.constructor = constructor;
        }

        @Override
        public O decode(CommandContext<CommandSourceStack> ctx) {
            return constructor.apply(a.getValue(ctx), b.getValue(ctx), c.getValue(ctx), d.getValue(ctx), e.getValue(ctx), f.getValue(ctx), g.getValue(ctx), h.getValue(ctx), i.getValue(ctx), j.getValue(ctx), k.getValue(ctx), l.getValue(ctx), m.getValue(ctx), n.getValue(ctx), p.getValue(ctx));
        }

        @Override
        public ArgumentBuilder<CommandSourceStack, ?> appendTo(ArgumentBuilder<CommandSourceStack, ?> parent, BiFunction<CommandSourceStack, O, Integer> onExecute) {
            var argA = a.getArgument();
            var argB = b.getArgument();
            var argC = c.getArgument();
            var argD = d.getArgument();
            var argE = e.getArgument();
            var argF = f.getArgument();
            var argG = g.getArgument();
            var argH = h.getArgument();
            var argI = i.getArgument();
            var argJ = j.getArgument();
            var argK = k.getArgument();
            var argL = l.getArgument();
            var argM = m.getArgument();
            var argN = n.getArgument();
            var argP = p.getArgument();

            argP.executes(ctx -> onExecute.apply(ctx.getSource(), decode(ctx)));
            argN.then(argP);
            argM.then(argN);
            argL.then(argM);
            argK.then(argL);
            argJ.then(argK);
            argI.then(argJ);
            argH.then(argI);
            argG.then(argH);
            argF.then(argG);
            argE.then(argF);
            argD.then(argE);
            argC.then(argD);
            argB.then(argC);
            argA.then(argB);
            parent.then(argA);
            return parent;
        }
    }

    public static class CompositeCodec16<O, A, B, C, D, E, F, G, H, I, J, K, L, M, N, P, Q> implements CommandCodec<O> {
        protected final CommandField<?, A> a;
        protected final CommandField<?, B> b;
        protected final CommandField<?, C> c;
        protected final CommandField<?, D> d;
        protected final CommandField<?, E> e;
        protected final CommandField<?, F> f;
        protected final CommandField<?, G> g;
        protected final CommandField<?, H> h;
        protected final CommandField<?, I> i;
        protected final CommandField<?, J> j;
        protected final CommandField<?, K> k;
        protected final CommandField<?, L> l;
        protected final CommandField<?, M> m;
        protected final CommandField<?, N> n;
        protected final CommandField<?, P> p;
        protected final CommandField<?, Q> q;
        private final Function16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, P, Q, O> constructor;

        public CompositeCodec16(CommandField<?, A> a, CommandField<?, B> b, CommandField<?, C> c, CommandField<?, D> d, CommandField<?, E> e, CommandField<?, F> f, CommandField<?, G> g, CommandField<?, H> h, CommandField<?, I> i, CommandField<?, J> j, CommandField<?, K> k, CommandField<?, L> l, CommandField<?, M> m, CommandField<?, N> n, CommandField<?, P> p, CommandField<?, Q> q, Function16<A, B, C, D, E, F, G, H, I, J, K, L, M, N, P, Q, O> constructor) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
            this.f = f;
            this.g = g;
            this.h = h;
            this.i = i;
            this.j = j;
            this.k = k;
            this.l = l;
            this.m = m;
            this.n = n;
            this.p = p;
            this.q = q;
            this.constructor = constructor;
        }

        @Override
        public O decode(CommandContext<CommandSourceStack> ctx) {
            return constructor.apply(a.getValue(ctx), b.getValue(ctx), c.getValue(ctx), d.getValue(ctx), e.getValue(ctx), f.getValue(ctx), g.getValue(ctx), h.getValue(ctx), i.getValue(ctx), j.getValue(ctx), k.getValue(ctx), l.getValue(ctx), m.getValue(ctx), n.getValue(ctx), p.getValue(ctx), q.getValue(ctx));
        }

        @Override
        public ArgumentBuilder<CommandSourceStack, ?> appendTo(ArgumentBuilder<CommandSourceStack, ?> parent, BiFunction<CommandSourceStack, O, Integer> onExecute) {
            var argA = a.getArgument();
            var argB = b.getArgument();
            var argC = c.getArgument();
            var argD = d.getArgument();
            var argE = e.getArgument();
            var argF = f.getArgument();
            var argG = g.getArgument();
            var argH = h.getArgument();
            var argI = i.getArgument();
            var argJ = j.getArgument();
            var argK = k.getArgument();
            var argL = l.getArgument();
            var argM = m.getArgument();
            var argN = n.getArgument();
            var argP = p.getArgument();
            var argQ = q.getArgument();

            argQ.executes(ctx -> onExecute.apply(ctx.getSource(), decode(ctx)));
            argP.then(argQ);
            argN.then(argP);
            argM.then(argN);
            argL.then(argM);
            argK.then(argL);
            argJ.then(argK);
            argI.then(argJ);
            argH.then(argI);
            argG.then(argH);
            argF.then(argG);
            argE.then(argF);
            argD.then(argE);
            argC.then(argD);
            argB.then(argC);
            argA.then(argB);
            parent.then(argA);
            return parent;
        }
    }
}
