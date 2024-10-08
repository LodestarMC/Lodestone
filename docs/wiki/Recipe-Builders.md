# Overview
As of Lodestone 1.21-1.8

Lodestone includes a small package built on top of vanilla recipe builders. The basic classes are `LodestoneRecipeBuilder<Recipe>`, `AutonamedRecipeBuilder<Recipe>` and a `Lodestone[Type]RecipeBuilder` for each of the different vanilla `[Type]RecipeBuilder` types.

These implementations have the following use cases:
- `LodestoneRecipeBuilder`: For creating your own recipe builder, offers a default save function and others
- `AutonamedRecipeBuilder`: Extension of `LodestoneRecipeBuilder`, with the output-based naming function of vanilla `RecipeBuilder`s. Prefer vanilla if you want to allow additional unlock criteria or add the recipe into a certain group.
- `Lodestone[Type]RecipeBuilder`: Extension of it's corresponding `[Type]RecipeBuilder`. Implements `LodestoneRecipeBuilder` and is intended chiefly for adding additional parameters or modifying save paths.


# Lodestone recipe builders
A basic implementation of `LodestoneRecipeBuilder` looks like this:
```java
public class TestRecipeBuilder implements LodestoneRecipeBuilder<TestRecipe> {
    protected final ItemStack ing1, ing2, output;

    // Advancement will be saved in recipes/misc/
    protected final RecipeCategory category = RecipeCategory.MISC;

    // Automatically added to the built advancement 
    protected final Map<String, Criterion<?>> criteria = new LinkedHashMap();

    public TestRecipeBuilder(ItemStack ing1, ItemStack ing2, ItemStack output) {
        this.ing1 = ing1;
        this.ing2 = ing2;
        this.output = output;
    }

    @Override
    public TestRecipe build(ResourceLocation id) {
        // Custom ID check implementation if you feel like it
        return new TestRecipe(ing1, ing2, output);
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation id) {
        defaultSaveFunc(recipeOutput, id)
    }

    // Optional
    @Override
    public void tweakAdvancement(Advancement.Builder advancement) {
        // advancement.doSomething...
    }
}
```