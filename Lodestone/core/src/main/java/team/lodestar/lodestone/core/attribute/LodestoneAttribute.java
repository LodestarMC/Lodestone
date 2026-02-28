package team.lodestar.lodestone.core.attribute;

import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.*;

public class LodestoneAttribute extends Attribute {

    private final ResourceLocation baseId;

    private final boolean forcePercentage;

    public static LodestoneAttributeBuilder create(ResourceLocation id, double defaultValue) {
        return new LodestoneAttributeBuilder(id, defaultValue);
    }
    protected LodestoneAttribute(ResourceLocation id, ResourceLocation baseId, double defaultValue, boolean forcePercentage) {
        super("attribute.name." + id.getNamespace() + "." + id.getPath(), defaultValue);
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
        return Component.translatable("neoforge.value.flat", FORMAT.format(value));
    }

}