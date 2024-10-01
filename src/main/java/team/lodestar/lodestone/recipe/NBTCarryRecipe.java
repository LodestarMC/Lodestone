package team.lodestar.lodestone.recipe;

import com.mojang.serialization.*;
import com.mojang.serialization.codecs.*;
import net.minecraft.core.*;
import net.minecraft.core.component.*;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.network.*;
import net.minecraft.network.codec.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.NotNull;
import team.lodestar.lodestone.recipe.builder.LodestoneShapedRecipeBuilder;
import team.lodestar.lodestone.registry.common.LodestoneRecipeSerializers;


public class NBTCarryRecipe extends ShapedRecipe {
    public static final String NAME = "nbt_carry";

    public final Ingredient copyFrom;

    public NBTCarryRecipe(ShapedRecipe compose, Ingredient copyFrom) {
        super(compose.getGroup(), compose.category(), compose.pattern, compose.result, compose.showNotification());
        this.copyFrom = copyFrom;
    }

    @Override
    public ItemStack assemble(CraftingInput pInput, HolderLookup.Provider pRegistries) {
        ItemStack out = super.assemble(pInput, pRegistries);
        for (int i = 0; i < pInput.size(); i++) {
            ItemStack stack = pInput.getItem(i);
            if (!stack.isEmpty() && copyFrom.test(stack) && !stack.getComponents().isEmpty()) {
                for (TypedDataComponent<?> component : stack.getComponents()) {
                    out.copyFrom(stack, component.type());
                }
                break;
            }
        }
        return out;
    }

    @NotNull
    @Override
    public RecipeSerializer<?> getSerializer() {
        return LodestoneRecipeSerializers.NBT_CARRY_RECIPE_SERIALIZER.get();
    }

    public static class Serializer implements RecipeSerializer<NBTCarryRecipe> {

        public static final MapCodec<NBTCarryRecipe> CODEC = RecordCodecBuilder.mapCodec((obj) -> obj.group(
                ShapedRecipe.Serializer.CODEC.fieldOf("recipe").forGetter((recipe) -> recipe),
                Ingredient.CODEC.fieldOf("copyFrom").forGetter((recipe) -> recipe.copyFrom)
        ).apply(obj, NBTCarryRecipe::new));


        public static final StreamCodec<RegistryFriendlyByteBuf, NBTCarryRecipe> STREAM_CODEC = StreamCodec.of(NBTCarryRecipe.Serializer::toNetwork, NBTCarryRecipe.Serializer::fromNetwork);

        @Override
        public MapCodec<NBTCarryRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, NBTCarryRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        public static NBTCarryRecipe fromNetwork(RegistryFriendlyByteBuf byteBuf) {
            ShapedRecipe recipe = ShapedRecipe.Serializer.STREAM_CODEC.decode(byteBuf);
            var copyFrom = Ingredient.CONTENTS_STREAM_CODEC.decode(byteBuf);
            return new NBTCarryRecipe(recipe, copyFrom);
        }

        public static void toNetwork(RegistryFriendlyByteBuf byteBuf, @NotNull NBTCarryRecipe recipe) {
            ShapedRecipe.Serializer.STREAM_CODEC.encode(byteBuf, recipe);
            Ingredient.CONTENTS_STREAM_CODEC.encode(byteBuf, recipe.copyFrom);
        }
    }

    public static class Builder extends LodestoneShapedRecipeBuilder {
        Ingredient copyFrom;

        public Builder(ShapedRecipeBuilder parent, Ingredient copyFrom) {
            super(parent);
            this.copyFrom = copyFrom;
        }

        @Override
        public ShapedRecipe build(ResourceLocation id) {
            return new NBTCarryRecipe(super.build(id), this.copyFrom);
        }
    }
}