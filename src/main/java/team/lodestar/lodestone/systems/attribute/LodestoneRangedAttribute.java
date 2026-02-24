package team.lodestar.lodestone.systems.attribute;

import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.NotNull;

import javax.annotation.*;

public class LodestoneRangedAttribute extends RangedAttribute {

    private final ResourceLocation baseId;

    private final boolean forcePercentage;

    public static LodestoneAttributeBuilder create(ResourceLocation id, double defaultValue, double min, double max) {
        return new LodestoneAttributeBuilder(id, defaultValue, min, max);
    }
    protected LodestoneRangedAttribute(ResourceLocation id, ResourceLocation baseId, double defaultValue, double min, double max, boolean forcePercentage) {
        super("attribute.name." + id.getNamespace() + "." + id.getPath(), defaultValue, min, max);
        this.baseId = baseId;
        this.forcePercentage = forcePercentage;
    }

    @Override
    @Nullable
    public ResourceLocation getBaseId() {
        return baseId;
    }

    @Override
    public @NotNull MutableComponent toValueComponent(@Nullable AttributeModifier.Operation op, double value, TooltipFlag flag) {
        if (forcePercentage) {
            return Component.translatable("neoforge.value.percent", FORMAT.format(value * 100));
        }
        return super.toValueComponent(op, value, flag);
    }

}