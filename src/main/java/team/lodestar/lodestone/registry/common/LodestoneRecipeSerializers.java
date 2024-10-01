package team.lodestar.lodestone.registry.common;

import io.github.fabricators_of_create.porting_lib.util.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.recipe.NBTCarryRecipe;

import java.util.function.Supplier;

public class LodestoneRecipeSerializers {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, LodestoneLib.LODESTONE);

    public static final Supplier<RecipeSerializer<NBTCarryRecipe>> NBT_CARRY_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register(NBTCarryRecipe.NAME, NBTCarryRecipe.Serializer::new);
}
