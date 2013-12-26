package exter.foundry.api.recipe.manager;

import java.util.List;

import net.minecraftforge.fluids.FluidStack;
import exter.foundry.api.recipe.IAlloyRecipe;

public interface IAlloyRecipeManager
{
  /**
   * Register an Alloy Mixer recipe.
   * @param out Output (fluid type and amount).
   * @param in Inputs (fluid type and amount required), length must be less or equal 4.
   */
  public void AddRecipe(FluidStack out,FluidStack[] in);
  
  /**
   * Get a list of all the recipes
   * @return List of all the recipes
   */
  public List<? extends IAlloyRecipe> GetRecipes();
}
