package team.lodestar.lodestone.systems.item.tools.magic;

import com.google.common.collect.ImmutableMultimap;
import team.lodestar.lodestone.systems.item.tools.LodestoneHoeItem;
import team.lodestar.lodestone.setup.LodestoneAttributeRegistry;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;

public class MagicHoeItem extends LodestoneHoeItem {

    public final float magicDamage;
    public MagicHoeItem(Tier material, int damage, float speed, float magicDamage, Item.Properties properties) {
        super(material, damage, speed, properties.durability(material.getUses()));
        this.magicDamage = magicDamage;
    }

    @Override
    public ImmutableMultimap.Builder<Attribute, AttributeModifier> createExtraAttributes() {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(LodestoneAttributeRegistry.MAGIC_DAMAGE.get(), new AttributeModifier(LodestoneAttributeRegistry.UUIDS.get(LodestoneAttributeRegistry.MAGIC_DAMAGE), "Weapon magic damage", magicDamage, AttributeModifier.Operation.ADDITION));
        return builder;
    }
}

