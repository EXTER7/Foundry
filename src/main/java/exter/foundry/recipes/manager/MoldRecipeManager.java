package exter.foundry.recipes.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import exter.foundry.api.recipe.IMoldRecipe;
import exter.foundry.api.recipe.manager.IMoldRecipeManager;
import exter.foundry.recipes.MoldRecipe;

public class MoldRecipeManager implements IMoldRecipeManager
{
  public List<IMoldRecipe> recipes;

  public static final MoldRecipeManager instance = new MoldRecipeManager();
  
  private MoldRecipeManager()
  {
    recipes = new ArrayList<IMoldRecipe>();
  }
  
  @Override
  public void addRecipe(ItemStack result, int width, int height, int[] recipe)
  {
    recipes.add(new MoldRecipe(result, width, height, recipe));
  }

  @Override
  public IMoldRecipe findRecipe(int[] grid)
  {
    for(IMoldRecipe r:recipes)
    {
      if(r.matchesRecipe(grid))
      {
        return r;
      }
    }
    return null;
  }

  @Override
  public List<IMoldRecipe> getRecipes()
  {
    return Collections.unmodifiableList(recipes);
  }

  @Override
  public void removeRecipe(IMoldRecipe recipe)
  {
    recipes.remove(recipe);    
  }
}
