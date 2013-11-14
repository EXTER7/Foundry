package exter.foundry.api.recipe.manager;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import exter.foundry.api.recipe.IMeltingRecipe;

public interface IMeltingRecipeManager
{
  
  /**
   * Register a Metal Smelter recipe
   * @param solid Ore Dictionary name of the item to be melted
   * @param fluid_stack Resulting fluid
   */
  public void AddRecipe(String solid,FluidStack fluid_stack);
  
  /**
   * Register a Metal Smelter recipe
   * @param solid Item to be melted
   * @param fluid_stack Resulting fluid
   */
  public void AddRecipe(ItemStack solid,FluidStack fluid_stack);

  /**
   * Get a list of all the recipes
   * @return List of all the recipes
   */
  public List<? extends IMeltingRecipe> GetRecipes();
}
