package exter.foundry.recipes.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraftforge.fluids.FluidStack;
import exter.foundry.api.recipe.IAtomizerRecipe;
import exter.foundry.api.recipe.manager.IAtomizerRecipeManager;
import exter.foundry.recipes.AtomizerRecipe;

public class AtomizerRecipeManager implements IAtomizerRecipeManager
{
  public List<IAtomizerRecipe> recipes;

  public static final AtomizerRecipeManager instance = new AtomizerRecipeManager();

  private AtomizerRecipeManager()
  {
    recipes = new ArrayList<IAtomizerRecipe>();
  }

  @Override
  public void AddRecipe(Object result,FluidStack in_fluid)
  {
    IAtomizerRecipe recipe = new AtomizerRecipe(result,in_fluid);
    recipes.add(recipe);
  }

  @Override
  public IAtomizerRecipe FindRecipe(FluidStack fluid)
  {
    if(fluid == null || fluid.amount == 0)
    {
      return null;
    }
    for(IAtomizerRecipe ar:recipes)
    {
      if(ar.MatchesRecipe(fluid))
      {
        return ar;
      }
    }
    return null;
  }
  
  @Override
  public List<IAtomizerRecipe> GetRecipes()
  {
    return Collections.unmodifiableList(recipes);
  }

  @Override
  public void RemoveRecipe(IAtomizerRecipe recipe)
  {
    recipes.remove(recipe);
  }
}
