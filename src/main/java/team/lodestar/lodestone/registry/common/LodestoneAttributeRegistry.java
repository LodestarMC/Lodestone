package team.lodestar.lodestone.registry.common;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Function;

import static team.lodestar.lodestone.LodestoneLib.LODESTONE;

/**
 * The attribute registry, {@link LodestoneAttributeRegistry#UUIDS} contains randomly generated uuids for each attribute registered, allowing you to have a constant uuid you may apply to attribute values.
 * Attribute modifiers might not like this idea so much though.
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class LodestoneAttributeRegistry {
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, LODESTONE);
    public static final HashMap<RegistryObject<Attribute>, UUID> UUIDS = new HashMap<>();
    public static final RegistryObject<Attribute> MAGIC_RESISTANCE = registerAttribute(ATTRIBUTES, LODESTONE, "magic_resistance", (id) -> new RangedAttribute(id, 1f, 0.0D, 2048.0D).setSyncable(true));
    public static final RegistryObject<Attribute> MAGIC_PROFICIENCY = registerAttribute(ATTRIBUTES, LODESTONE, "magic_proficiency", (id) -> new RangedAttribute(id, 1f, 0.0D, 2048.0D).setSyncable(true));
    public static final RegistryObject<Attribute> MAGIC_DAMAGE = registerAttribute(ATTRIBUTES, LODESTONE, "magic_damage", (id) -> new RangedAttribute(id, 0.0D, 0.0D, 2048.0D).setSyncable(true));

    /**
     * Registers an attribute with a given name,
     */
    public static RegistryObject<Attribute> registerAttribute(DeferredRegister<Attribute> registry, String modId, String name, Function<String, Attribute> attribute) {
        RegistryObject<Attribute> registryObject = registry.register(name, () -> attribute.apply("attribute.name." + modId + "." + name));
        UUIDS.put(registryObject, UUID.randomUUID());
        return registryObject;
    }

    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent event) {
        event.getTypes().forEach(e -> {
            event.add(e, MAGIC_RESISTANCE.get());
            event.add(e, MAGIC_PROFICIENCY.get());
            event.add(e, MAGIC_DAMAGE.get());
        });
    }
}