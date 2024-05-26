package team.lodestar.lodestone.registry.common;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Function;

import static team.lodestar.lodestone.LodestoneLib.LODESTONE;

/**
 * The attribute registry, {@link LodestoneAttributeRegistry#UUIDS} contains randomly generated uuids for each attribute registered, allowing you to have a constant uuid you may apply to attribute values.
 * Attribute modifiers might not like this idea so much though.
 */

public class LodestoneAttributeRegistry {
    public static final LazyRegistrar<Attribute> ATTRIBUTES = LazyRegistrar.create(BuiltInRegistries.ATTRIBUTE, LODESTONE);
    public static final HashMap<RegistryObject<Attribute>, UUID> UUIDS = new HashMap<>();
    public static final RegistryObject<Attribute> MAGIC_RESISTANCE = registerAttribute(ATTRIBUTES, LODESTONE, "magic_resistance", (id) -> new RangedAttribute(id, 0.0D, 0.0D, 2048.0D).setSyncable(true));
    public static final RegistryObject<Attribute> MAGIC_PROFICIENCY = registerAttribute(ATTRIBUTES, LODESTONE, "magic_proficiency", (id) -> new RangedAttribute(id, 0.0D, 0.0D, 2048.0D).setSyncable(true));
    public static final RegistryObject<Attribute> MAGIC_DAMAGE = registerAttribute(ATTRIBUTES, LODESTONE, "magic_damage", (id) -> new RangedAttribute(id, 0.0D, 0.0D, 2048.0D).setSyncable(true));

    /**
     * Registers an attribute with a given name,
     */
    public static RegistryObject<Attribute> registerAttribute(LazyRegistrar<Attribute> registry, String modId, String name, Function<String, Attribute> attribute) {
        RegistryObject<Attribute> registryObject = registry.register(name, () -> attribute.apply("attribute.name." + modId + "." + name));
        UUIDS.put(registryObject, UUID.randomUUID());
        return registryObject;
    }
}