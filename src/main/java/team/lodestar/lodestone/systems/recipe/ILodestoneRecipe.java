package team.lodestar.lodestone.systems.recipe;


import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class ILodestoneRecipe implements Recipe<Container> {
    @Deprecated
    @Override
    public boolean matches(Container inv, Level level) {
        return false;
    }

    @Deprecated
    @Override
    public ItemStack assemble(@NotNull Container inv, @NotNull RegistryAccess registry) {
        return ItemStack.EMPTY;
    }

    @Deprecated
    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Deprecated
    @Override
    public ItemStack getResultItem(@NotNull RegistryAccess registry) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

}