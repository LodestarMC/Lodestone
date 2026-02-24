package team.lodestar.lodestone.registry.common;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import team.lodestar.lodestone.*;
import team.lodestar.lodestone.systems.attribute.*;

import static team.lodestar.lodestone.LodestoneLib.LODESTONE;

@EventBusSubscriber()
public class LodestoneAttributes {

    public static final ResourceLocation BASE_MAGIC_DAMAGE = LodestoneLib.lodestonePath("base_magic_damage");

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, LODESTONE);
    public static final DeferredHolder<Attribute, Attribute> MAGIC_RESISTANCE = registerAttribute(ATTRIBUTES,
            LodestoneRangedAttribute.create(LodestoneLib.lodestonePath("magic_resistance"), 1.0D, 0.0D, 2048.0D).forcePercentageDisplay().setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> MAGIC_PROFICIENCY = registerAttribute(ATTRIBUTES,
            LodestoneRangedAttribute.create(LodestoneLib.lodestonePath("magic_proficiency"), 1.0D, 0.0D, 2048.0D).forcePercentageDisplay().setSyncable(true));
    public static final DeferredHolder<Attribute, Attribute> MAGIC_DAMAGE = registerAttribute(ATTRIBUTES,
            LodestoneRangedAttribute.create(LodestoneLib.lodestonePath("magic_damage"), 0.0D, 0.0D, 2048.0D).setAsBaseAttribute(BASE_MAGIC_DAMAGE).setSyncable(true));

    public static DeferredHolder<Attribute, Attribute> registerAttribute(DeferredRegister<Attribute> registry, LodestoneAttributeBuilder builder) {
        return registry.register(builder.id.getPath(), builder::build);
    }


    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent event) {
        event.getTypes().forEach(e -> {
            for (DeferredHolder<Attribute, ? extends Attribute> entry : ATTRIBUTES.getEntries()) {
                if (!event.has(e, entry)) {
                    event.add(e, entry);
                }
            }
        });
    }
}