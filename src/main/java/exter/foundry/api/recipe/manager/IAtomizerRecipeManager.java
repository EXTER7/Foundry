package exter.foundry.api.recipe.manager;

import java.util.List;

import exter.foundry.api.recipe.IAtomizerRecipe;
import net.minecraftforge.fluids.FluidStack;

public interface IAtomizerRecipeManager
{

  /**
   * Register a Metal Atomizer recipe.
   * @param result Item produced. Can be an ItemStack, or a String.
   * @param in_fluid Fluid required (fluid type and amount).
   */
  public void AddRecipe(Object result,FluidStack in_fluid);
  
  /**
   * Get a list of all the recipes.
   * @return List of all the recipes.
   */
  public List<IAtomizerRecipe> GetRecipes();

  /**
   * Find an atomizer recipe given a FluidStack.
   * @param fluid FluidStack that contains the recipe's required fluid.
   * @return The Atomizer recipe, or null if no matching recipe.
   */
  public IAtomizerRecipe FindRecipe(FluidStack fluid);

  /**
   * Removes a recipe.
   * @param The recipe to remove.
   */
  public void RemoveRecipe(IAtomizerRecipe recipe);
}
