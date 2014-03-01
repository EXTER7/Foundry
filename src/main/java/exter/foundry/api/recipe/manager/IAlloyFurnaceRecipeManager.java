package exter.foundry.api.recipe.manager;

import java.util.List;

import net.minecraft.item.ItemStack;
import exter.foundry.api.recipe.IAlloyFurnaceRecipe;

public interface IAlloyFurnaceRecipeManager
{
  /**
   * Register an Alloy Mixer recipe.
   * @param out Output (fluid type and amount).
   * @param in Inputs (fluid type and amount required), length must be less or equal 4.
   */
  public void AddRecipe(ItemStack out,Object in_a, Object in_b);
  
  /**
   * Get a list of all the recipes
   * @return List of all the recipes
   */
  public List<IAlloyFurnaceRecipe> GetRecipes();
  
  /**
   * Find a valid recipe that contains the given inputs.
   * A recipe is found if the recipe's inputs contains the fluid in the parameters.
   * @param in_a FluidStack for the first input.
   * @param in_b FluidStack for the second input.
   * @param order [Output] Order in which the input fluids are matched.
   * @return
   */
  public IAlloyFurnaceRecipe FindRecipe(ItemStack in_a,ItemStack in_b);

}
