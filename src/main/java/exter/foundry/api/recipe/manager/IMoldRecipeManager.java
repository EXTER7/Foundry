package exter.foundry.api.recipe.manager;

import java.util.List;

import exter.foundry.api.recipe.IMoldRecipe;
import net.minecraft.item.ItemStack;

public interface IMoldRecipeManager
{
  public void addRecipe(ItemStack result, int width, int height, int[] recipe);

  public List<IMoldRecipe> getRecipes();
 
  public IMoldRecipe findRecipe(int[] grid);

  /**
   * Removes a recipe.
   * @param The recipe to remove.
   */
  public void removeRecipe(IMoldRecipe recipe);
}
