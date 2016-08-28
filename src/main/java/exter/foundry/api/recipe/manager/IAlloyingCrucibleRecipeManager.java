package exter.foundry.api.recipe.manager;

import java.util.List;

import net.minecraftforge.fluids.FluidStack;
import exter.foundry.api.recipe.IAlloyingCrucibleRecipe;

public interface IAlloyingCrucibleRecipeManager
{
  /**
   * Register an Alloying Crucible recipe.
   * @param out Output.
   * @param in_a Input A.
   * @param in_b Input B.
   */
  public void addRecipe(FluidStack out,FluidStack in_a, FluidStack in_b);

  /**
   * Get a list of all the recipes
   * @return List of all the recipes
   */
  public List<IAlloyingCrucibleRecipe> getRecipes();
  
  /**
   * Find a valid recipe that contains the given inputs.
   * A recipe is found if the recipe's inputs contains the fluid in the parameters.
   * @param in_a FluidStack for the first input.
   * @param in_b FluidStack for the second input.
   * @param order [Output] Order in which the input fluids are matched.
   * @return
   */
  public IAlloyingCrucibleRecipe findRecipe(FluidStack in_a,FluidStack in_b);

  
  /**
   * Removes a recipe.
   * @param The recipe to remove.
   */
  public void removeRecipe(IAlloyingCrucibleRecipe recipe);
}
