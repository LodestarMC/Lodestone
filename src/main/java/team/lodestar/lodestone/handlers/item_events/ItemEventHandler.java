package team.lodestar.lodestone.handlers.item_events;

import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.event.entity.living.*;
import team.lodestar.lodestone.*;

import java.util.*;

/**
 * A handler for firing {@link IEventResponderItem} events
 */
public class ItemEventHandler {

    private static final HashSet<EventResponderSource> SOURCES = new HashSet<>();

    public static final EventResponderSource HELD_ITEM = registerSource(new EventResponderSource(LodestoneLib.lodestonePath("held_item"), e -> List.of(e.getMainHandItem())));

    public static final EventResponderSource ARMOR = registerSource(new EventResponderSource(LodestoneLib.lodestonePath("armor"), e -> {
        ArrayList<ItemStack> stacks = new ArrayList<>();
        for (ItemStack stack : e.getArmorSlots()) {
            stacks.add(stack);
        }
        return stacks;
    }));

    public static void triggerDeathResponses(LivingDeathEvent event) {
        if (event.isCanceled()) {
            return;
        }
        var source = event.getSource();
        var target = event.getEntity();
        var attacker = source.getEntity() instanceof LivingEntity livingAttacker ? livingAttacker : target.getLastAttacker();
        createEventProcessor(target).run((e, s) -> e.incomingDeathEvent(event, attacker, target, s));
        if (attacker != null) {
            createEventProcessor(attacker).run((e, s) -> e.outgoingDeathEvent(event, attacker, target, s));
        }
    }

    public static void triggerHurtResponses(LivingIncomingDamageEvent event) {
        var source = event.getSource();
        var target = event.getEntity();
        var attacker = source.getEntity() instanceof LivingEntity livingAttacker ? livingAttacker : target.getLastAttacker();
        createEventProcessor(target).run((e, s) -> e.incomingDamageEvent(event, attacker, target, s));
        if (attacker != null) {
            createEventProcessor(attacker).run((e, s) -> e.outgoingDamageEvent(event, attacker, target, s));
        }
    }

    public static void triggerHurtResponses(LivingDamageEvent.Pre event) {
        var source = event.getSource();
        var target = event.getEntity();
        var attacker = source.getEntity() instanceof LivingEntity livingAttacker ? livingAttacker : target.getLastAttacker();
        createEventProcessor(target).run((e, s) -> e.incomingDamageEvent(event, attacker, target, s));
        if (attacker != null) {
            createEventProcessor(attacker).run((e, s) -> e.outgoingDamageEvent(event, attacker, target, s));
        }
    }

    public static void triggerHurtResponses(LivingDamageEvent.Post event) {
        var source = event.getSource();
        var target = event.getEntity();
        var attacker = source.getEntity() instanceof LivingEntity livingAttacker ? livingAttacker : target.getLastAttacker();
        createEventProcessor(target).run((e, s) -> e.finalizedIncomingDamageEvent(event, attacker, target, s));
        if (attacker != null) {
            createEventProcessor(attacker).run((e, s) -> e.finalizedOutgoingDamageEvent(event, attacker, target, s));
        }
    }

    public static void addAttributeTooltips(AddAttributeTooltipsEvent event) {
        var stack = event.getStack();
        if (stack.getItem() instanceof IEventResponderItem eventResponderItem) {
            eventResponderItem.modifyAttributeTooltipEvent(event);
        }
    }

    public static EventResponderProcessor createEventProcessor(LivingEntity entity) {
        return new EventResponderProcessor(findEventResponders(entity));
    }

    public static List<EventResponderCollection> findEventResponders(LivingEntity entity) {
        return SOURCES.stream().map(s -> s.getEventResponders(entity)).toList();
    }


    public static EventResponderSource registerSource(EventResponderSource source) {
        SOURCES.add(source);
        return source;
    }
}