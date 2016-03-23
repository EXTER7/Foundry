package exter.foundry.api.recipe.manager;

import java.util.List;

import exter.foundry.api.recipe.IInfuserRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;


public interface IInfuserRecipeManager
{
  /**
   * Register a Metal Infuser recipe.
   * @param result Fluid produced.
   * @param in_fluid Fluid required (fluid type and amount).
   * @param item Item required.
   * @param energy Energy required (100 Energy = 1 MJ, 10 Energy = 1 RF, 40 Energy = 1 EU).
   */
  public void addRecipe(FluidStack result,FluidStack fluid,Object item,int energy);

  public IInfuserRecipe findRecipe(FluidStack fluid,ItemStack item);

  /**
   * Get a list of all the recipes
   * @return List of all the recipes
   */
  public List<IInfuserRecipe> getRecipes();
  
  /**
   * Removes a recipe.
   * @param The recipe to remove.
   */
  public void removeRecipe(IInfuserRecipe recipe);
}
