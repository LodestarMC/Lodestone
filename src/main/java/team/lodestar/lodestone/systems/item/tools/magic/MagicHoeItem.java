package team.lodestar.lodestone.systems.item.tools.magic;

import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import team.lodestar.lodestone.systems.item.*;
import team.lodestar.lodestone.systems.item.*;
import team.lodestar.lodestone.registry.common.LodestoneAttributes;
import team.lodestar.lodestone.systems.item.tools.LodestoneHoeItem;

import static team.lodestar.lodestone.registry.common.LodestoneAttributes.BASE_MAGIC_DAMAGE;

public class MagicHoeItem extends LodestoneHoeItem {

    public MagicHoeItem(Tier tier, float attackDamage, float attackSpeed, float magicDamage, LodestoneItemProperties properties) {
        super(tier, attackDamage, attackSpeed, properties.mergeAttributes(
                ItemAttributeModifiers.builder()
                        .add(LodestoneAttributes.MAGIC_DAMAGE, new AttributeModifier(BASE_MAGIC_DAMAGE, magicDamage, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                        .build()));
    }
}

