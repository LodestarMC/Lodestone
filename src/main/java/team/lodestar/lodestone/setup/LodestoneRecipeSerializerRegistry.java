package team.lodestar.lodestone.setup;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.recipe.NBTCarryRecipe;

public class LodestoneRecipeSerializerRegistry {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, LodestoneLib.LODESTONE);

    public static final RegistryObject<RecipeSerializer<NBTCarryRecipe>> NBT_CARRY_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register(NBTCarryRecipe.NAME, NBTCarryRecipe.Serializer::new);
}
