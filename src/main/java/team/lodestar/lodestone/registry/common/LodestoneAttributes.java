package team.lodestar.lodestone.registry.common;

import io.github.fabricators_of_create.porting_lib.util.DeferredHolder;
import io.github.fabricators_of_create.porting_lib.util.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import static team.lodestar.lodestone.LodestoneLib.LODESTONE;

/**
 * The attribute registry, {@link LodestoneAttributes#UUIDS} contains randomly generated uuids for each attribute registered, allowing you to have a constant uuid you may apply to attribute values.
 * Attribute modifiers might not like this idea so much though.
 */

public class LodestoneAttributes {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, LODESTONE);
    public static final HashMap<Supplier<Attribute>, UUID> UUIDS = new HashMap<>();
    public static final DeferredHolder<Attribute, Attribute> MAGIC_RESISTANCE = registerAttribute(ATTRIBUTES, LODESTONE, "magic_resistance", (id) -> new RangedAttribute(id, 0.0D, 0.0D, 2048.0D).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> MAGIC_PROFICIENCY = registerAttribute(ATTRIBUTES, LODESTONE, "magic_proficiency", (id) -> new RangedAttribute(id, 0.0D, 0.0D, 2048.0D).setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> MAGIC_DAMAGE = registerAttribute(ATTRIBUTES, LODESTONE, "magic_damage", (id) -> new RangedAttribute(id, 0.0D, 0.0D, 2048.0D).setSyncable(true));

    /**
     * Registers an attribute with a given name,
     */
    public static DeferredHolder<Attribute, Attribute> registerAttribute(DeferredRegister<Attribute> registry, String modId, String name, Function<String, Attribute> attribute) {
        DeferredHolder<Attribute, Attribute> registryObject = registry.register(name, () -> attribute.apply("attribute.name." + modId + "." + name));
        UUIDS.put(registryObject, UUID.randomUUID());
        return registryObject;
    }

    public static void modifyEntityAttributes(EntityAttributeModificationEvent event) {
        event.getTypes().forEach(e -> {
            event.add(e, MAGIC_RESISTANCE);
            event.add(e, MAGIC_PROFICIENCY);
            event.add(e, MAGIC_DAMAGE);
        });
    }
}