package exter.foundry.api.recipe.manager;

import net.minecraftforge.fluids.FluidStack;
import exter.foundry.recipes.AlloyRecipe;

public interface IAlloyRecipeManager
{
  /**
   * Register an Alloy Mixer recipe.
   * @param out Output (fluid type and amount).
   * @param in_a First input (fluid type and amount required).
   * @param in_b Second input (fluid type and amount required).
   */
  public void AddRecipe(FluidStack out,FluidStack in_a,FluidStack in_b);
}
