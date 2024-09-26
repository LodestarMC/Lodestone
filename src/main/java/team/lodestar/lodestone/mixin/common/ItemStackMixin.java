package team.lodestar.lodestone.mixin.common;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import team.lodestar.lodestone.systems.item.IComponentResponderItem;

import javax.annotation.Nullable;
import java.util.Optional;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract Item getItem();

    @Shadow public abstract int getCount();

    @Shadow public abstract DataComponentMap getComponents();

    @Shadow @Final private PatchedDataComponentMap components;

    @WrapMethod(method = "getComponents")
    private DataComponentMap lodestone$getComponents(Operation<DataComponentMap> method) {
        DataComponentMap original = method.call();
        DataComponentMap.Builder builder = DataComponentMap.builder().addAll(original);
        if (this.getItem() instanceof IComponentResponderItem componentItem) {
            componentItem.readComponent(this.getCount(), builder, IComponentResponderItem.ComponentGetter.create(original));
        }
        return builder.build();
    }

    @WrapMethod(method = "remove")
    private <T> T lodestone$remove(DataComponentType<T> type, Operation<T> operation) {
        DataComponentMap original = this.getComponents();
        DataComponentMap.Builder builder = DataComponentMap.builder().addAll(original);
        if (this.getItem() instanceof IComponentResponderItem componentItem) {
            IComponentResponderItem.RemoveComponentOperation<T> removeOp = new IComponentResponderItem.RemoveComponentOperation<>(
                    original.get(type), type
            );
            componentItem.removeComponent(this.getCount(), builder, removeOp, IComponentResponderItem.ComponentGetter.create(original));
            if (removeOp.isCancelled) return original.get(type);
        }
       return operation.call(type);
    }

    @WrapMethod(method = "set")
    private <T> T lodestone$set(DataComponentType<T> type, T value, Operation<T> operation) {
        DataComponentMap original = this.getComponents();
        DataComponentMap.Builder builder = DataComponentMap.builder().addAll(original);
        if (this.getItem() instanceof IComponentResponderItem componentItem) {
            IComponentResponderItem.ModifyComponentOperation<T> modifyOp = new IComponentResponderItem.ModifyComponentOperation<>(
                    original.get(type), value, type
            );
            componentItem.setComponent(this.getCount(), builder, modifyOp, IComponentResponderItem.ComponentGetter.create(original));
            if (modifyOp.isCancelled) return original.get(type);
        }
        return this.components.set(type, value);
    }

    /* TODO just add magic damage to components and it should be added automatically
    @Unique
    private AttributeModifier lodestone$attributeModifier;

    @ModifyVariable(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;getId()Ljava/util/UUID;", ordinal = 0), index = 13)
    private AttributeModifier lodestone$getTooltip(AttributeModifier value) {
        this.lodestone$attributeModifier = value;
        return value;
    }

    @ModifyVariable(method = "getTooltipLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;getOperation()Lnet/minecraft/world/entity/ai/attributes/AttributeModifier$Operation;", ordinal = 0), index = 16)
    private boolean lodestone$getTooltip(boolean value, @Nullable Player player, TooltipFlag flag) {
        if (player != null) {
            if (lodestone$attributeModifier.getId().equals(LodestoneAttributes.UUIDS.get(LodestoneAttributes.MAGIC_DAMAGE))) {
                return true;
            }
        }
        return value;
    }

    @ModifyVariable(method = "getTooltipLines", at = @At("STORE"), argsOnly = true)
    private List<Component> lodestone$getTooltip(ItemStack value, @Local(argsOnly = true) Player player, @Local(argsOnly = true) Item.TooltipContext tooltipContext) {
        if (player != null) {
            Multimap<Attribute, AttributeModifier> copied = LinkedHashMultimap.create();
            for (Map.Entry<Attribute, AttributeModifier> entry : map.entries()) {
                Attribute key = entry.getKey();
                AttributeModifier modifier = entry.getValue();
                double amount = modifier.getAmount();
                if (modifier.getId().equals(LodestoneAttributes.UUIDS.get(LodestoneAttributes.MAGIC_DAMAGE))) {
                    AttributeInstance instance = player.getAttribute(LodestoneAttributes.MAGIC_PROFICIENCY.get());
                    if (instance != null && instance.getValue() > 0) {
                        amount *= (1 + instance.getValue() * 0.1f);
                    }
                    copied.put(key, new AttributeModifier(
                            modifier.getId(), modifier.getName(), amount, modifier.getOperation()
                    ));
                } else {
                    copied.put(key, modifier);
                }
            }

            return copied;
        }
        return map;
    }

     */
}