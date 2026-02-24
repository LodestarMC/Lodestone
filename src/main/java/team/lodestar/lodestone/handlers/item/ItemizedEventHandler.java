package team.lodestar.lodestone.handlers.item;

import com.mojang.datafixers.util.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.event.entity.living.*;
import team.lodestar.lodestone.*;

import java.util.*;
import java.util.function.*;

/**
 * A handler for firing {@link ItemizedEventResponder} events
 */
public class ItemizedEventHandler {

    private static final HashSet<ItemizedEventResponderLookup> LOOKUPS = new HashSet<>();

    public static final ItemizedEventResponderLookup HELD_ITEM = registerLookup(new ItemizedEventResponderLookup(LodestoneLib.lodestonePath("held_item"), e -> List.of(e.getMainHandItem())));

    public static final ItemizedEventResponderLookup ARMOR = registerLookup(new ItemizedEventResponderLookup(LodestoneLib.lodestonePath("armor"), e -> {
        ArrayList<ItemStack> stacks = new ArrayList<>();
        e.getArmorSlots().forEach(stacks::add);
        return stacks;
    }));

    public static ItemizedEventResponderLookup registerLookup(ItemizedEventResponderLookup lookup) {
        LOOKUPS.add(lookup);
        return lookup;
    }

    public static void triggerDeathResponses(LivingDeathEvent event) {
        if (event.isCanceled()) {
            return;
        }
        var source = event.getSource();
        var target = event.getEntity();
        var attacker = source.getEntity() instanceof LivingEntity livingAttacker ? livingAttacker : target.getLastAttacker();
        run(target, (responder, stack) -> responder.incomingDeathEvent(event, attacker, target, stack));
        if (attacker != null) {
            run(attacker, (responder, stack) -> responder.outgoingDeathEvent(event, attacker, target, stack));
        }
    }

    public static void triggerHurtResponses(LivingIncomingDamageEvent event) {
        var source = event.getSource();
        var target = event.getEntity();
        var attacker = source.getEntity() instanceof LivingEntity livingAttacker ? livingAttacker : target.getLastAttacker();
        run(target,(responder, stack) -> responder.incomingDamageEvent(event, attacker, target, stack));
        if (attacker != null) {
            run(attacker,(responder, stack) -> responder.outgoingDamageEvent(event, attacker, target, stack));
        }
    }

    public static void triggerHurtResponses(LivingDamageEvent.Pre event) {
        var source = event.getSource();
        var target = event.getEntity();
        var attacker = source.getEntity() instanceof LivingEntity livingAttacker ? livingAttacker : target.getLastAttacker();
        run(target,(responder, stack) -> responder.incomingDamageEvent(event, attacker, target, stack));
        if (attacker != null) {
            run(attacker,(responder, stack) -> responder.outgoingDamageEvent(event, attacker, target, stack));
        }
    }

    public static void triggerHurtResponses(LivingDamageEvent.Post event) {
        var source = event.getSource();
        var target = event.getEntity();
        var attacker = source.getEntity() instanceof LivingEntity livingAttacker ? livingAttacker : target.getLastAttacker();
        run(target,(responder, stack) -> responder.finalizedIncomingDamageEvent(event, attacker, target, stack));
        if (attacker != null) {
            run(attacker,(responder, stack) -> responder.finalizedOutgoingDamageEvent(event, attacker, target, stack));
        }
    }

    public static void addAttributeTooltips(AddAttributeTooltipsEvent event) {
        final ItemStack stack = event.getStack();
        if (stack.getItem() instanceof ItemizedEventResponder responder) {
            responder.modifyAttributeTooltipEvent(event);
        }
    }

    public static void run(LivingEntity entity, BiConsumer<ItemizedEventResponder, ItemStack> consumer) {
        for (ItemizedEventResponderLookup lookup : LOOKUPS) {
            lookup.run(entity, consumer);
        }
    }


    /**
     * An interface containing various methods which are triggered alongside various forge events.
     * Implement on your item for the methods to be called.
     * Does not necessarily have to be bound to an itemstack.
     */
    public interface ItemizedEventResponder {

        default void modifyAttributeTooltipEvent(AddAttributeTooltipsEvent event) {

        }

        //TODO: the naming here is confusing. We have the `Incoming` event which is ran before any damage is done
        // We also have Our Incoming and Outgoing wrappers for LivingDamageEvent Pre and Post
        // Need to figure out a nice naming consistency here
        default void incomingDamageEvent(LivingIncomingDamageEvent event, LivingEntity attacker, LivingEntity target, ItemStack stack) {
        }

        default void outgoingDamageEvent(LivingIncomingDamageEvent event, LivingEntity attacker, LivingEntity target, ItemStack stack) {
        }

        default void incomingDamageEvent(LivingDamageEvent.Pre event, LivingEntity attacker, LivingEntity target, ItemStack stack) {
        }

        default void outgoingDamageEvent(LivingDamageEvent.Pre event, LivingEntity attacker, LivingEntity target, ItemStack stack) {
        }

        default void finalizedIncomingDamageEvent(LivingDamageEvent.Post event, LivingEntity attacker, LivingEntity target, ItemStack stack) {
        }

        default void finalizedOutgoingDamageEvent(LivingDamageEvent.Post event, LivingEntity attacker, LivingEntity target, ItemStack stack) {
        }

        default void incomingDeathEvent(LivingDeathEvent event, LivingEntity attacker, LivingEntity target, ItemStack stack) {
        }

        default void outgoingDeathEvent(LivingDeathEvent event, LivingEntity attacker, LivingEntity target, ItemStack stack) {
        }
    }

    public static class ItemizedEventResponderLookup {

        public final ResourceLocation id;
        public final Function<LivingEntity, Collection<ItemStack>> itemsToCheck;
        public final MappingFunction mapper;

        public ItemizedEventResponderLookup(ResourceLocation id, Function<LivingEntity, Collection<ItemStack>> itemsToCheck) {
            this(id, itemsToCheck, null);
        }

        public ItemizedEventResponderLookup(ResourceLocation id, Function<LivingEntity, Collection<ItemStack>> itemsToCheck, MappingFunction mapper) {
            this.id = id;
            this.itemsToCheck = itemsToCheck;
            this.mapper = mapper;
        }

        public void run(LivingEntity entity, BiConsumer<ItemizedEventResponder, ItemStack> consumer) {
            for (Pair<ItemizedEventResponder, ItemStack> pair : findEventResponders(entity)) {
                consumer.accept(pair.getFirst(), pair.getSecond());
            }
        }

        public final Collection<Pair<ItemizedEventResponder, ItemStack>> findEventResponders(LivingEntity entity) {
            Collection<ItemStack> sourced = itemsToCheck.apply(entity);
            Collection<Pair<ItemizedEventResponder, ItemStack>> responders = new ArrayList<>();
            for (ItemStack stack : sourced) {
                getEventResponder(entity, stack).ifPresent(r -> responders.add(Pair.of(r, stack)));
            }
            return responders;
        }

        private Optional<ItemizedEventResponder> getEventResponder(LivingEntity entity, ItemStack stack) {
            if (mapper != null) {
                return Optional.ofNullable(mapper.apply(entity, stack));
            }
            if (stack.getItem() instanceof ItemizedEventResponder responder) {
                return Optional.of(responder);
            }
            return Optional.empty();
        }

        public interface MappingFunction {
            ItemizedEventResponder apply(LivingEntity entity, ItemStack stack);
        }
    }
}