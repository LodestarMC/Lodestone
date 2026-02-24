package team.lodestar.lodestone.systems.enchanting;

import com.mojang.datafixers.util.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.item.enchantment.effects.*;
import org.apache.commons.lang3.mutable.*;

import javax.annotation.*;
import java.util.*;

public class LodestoneEnchantmentValueEffectHelper {

    //TODO: replace this with a random source that always outputs the average
    private static final RandomSource FAKE_ASS_RANDOM_SOURCE = RandomSource.create();

    public static float getAccurateAttackDamage(LivingEntity entity, ItemStack stack) {
        return getComponentValue(entity, stack, DataComponents.DAMAGE, Attributes.ATTACK_DAMAGE);
    }

    public static float getComponentValue(LivingEntity entity, ItemStack stack, DataComponentType<?> component, Holder<Attribute> base) {
        float baseValue = (float) entity.getAttributeValue(base);
        return getComponentValue(stack, component, baseValue);
    }

    public static float getComponentValue(ItemStack item, DataComponentType<?> component, float baseValue) {
        return getComponentValue(item, null, component, baseValue);
    }

    public static float getComponentValue(ItemStack item, @Nullable Holder<Enchantment> filter, DataComponentType<?> componentType, float baseValue) {
        return getComponentValue(item, item, filter, componentType, baseValue);
    }

    public static float getComponentValue(ItemStack enchantmentHolder, ItemStack effectHolder, @Nullable Holder<Enchantment> filter, DataComponentType<?> componentType, float baseValue) {
        var mutable = new MutableFloat(baseValue);
        try {
            LodestoneEnchantmentDataHelper.runIterationOnItem(enchantmentHolder, filter, (enchantment, enchantmentLevel) -> {
                var componentMap = enchantment.value().effects();
                List<EnchantmentValueEffect> effects = getValueEffects(effectHolder, componentMap, componentType);
                for (EnchantmentValueEffect effect : effects) {
                    processValueEffect(mutable, effect, enchantmentLevel);
                }
            });
        } catch (Exception ignored) {
        }
        return mutable.getValue();
    }

    public static List<EnchantmentValueEffect> getValueEffects(ItemStack item, DataComponentMap map, DataComponentType<?> type) {
        return LodestoneEnchantmentDataHelper.getMatchingEffects(item, map, EnchantmentValueEffect.class, Either.left(e -> e.type().equals(type)));
    }

    public static void processValueEffect(MutableFloat mutable, EnchantmentValueEffect effect, int enchantmentLevel) {
        if (effect instanceof RemoveBinomial removeBinomial) {
            //Compute the average when dealing with random components.
            float f = removeBinomial.chance().calculate(enchantmentLevel);
            if (f < 1) {
                mutable.setValue(mutable.getValue() / (1 - f));
                return;
            }
        }
        mutable.setValue(effect.process(enchantmentLevel, FAKE_ASS_RANDOM_SOURCE, mutable.getValue()));
    }
}