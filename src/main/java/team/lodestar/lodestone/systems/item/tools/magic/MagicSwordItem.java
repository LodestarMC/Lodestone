package team.lodestar.lodestone.systems.item.tools.magic;

import com.google.common.collect.ImmutableMultimap;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import team.lodestar.lodestone.registry.common.LodestoneAttributeRegistry;
import team.lodestar.lodestone.systems.item.tools.LodestoneSwordItem;

public class MagicSwordItem extends LodestoneSwordItem {
    public final float magicDamage;

    public MagicSwordItem(Tier material, int attackDamage, float attackSpeed, float magicDamage, Item.Properties properties) {
        super(material, attackDamage, attackSpeed, properties.durability(material.getUses()));
        this.magicDamage = magicDamage;
    }

    @Override
    public ImmutableMultimap.Builder<Attribute, AttributeModifier> createExtraAttributes() {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(LodestoneAttributeRegistry.MAGIC_DAMAGE.get(), new AttributeModifier(LodestoneAttributeRegistry.UUIDS.get(LodestoneAttributeRegistry.MAGIC_DAMAGE), "Weapon magic damage", magicDamage, AttributeModifier.Operation.ADDITION));
        return builder;
    }
}
