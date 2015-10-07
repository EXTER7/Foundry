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
  private List<IAtomizerRecipe> recipes;

  public static final AtomizerRecipeManager instance = new AtomizerRecipeManager();

  private AtomizerRecipeManager()
  {
    recipes = new ArrayList<IAtomizerRecipe>();
  }

  @Override
  public void addRecipe(Object result,FluidStack in_fluid)
  {
    IAtomizerRecipe recipe = new AtomizerRecipe(result,in_fluid);
    recipes.add(recipe);
  }

  @Override
  public IAtomizerRecipe findRecipe(FluidStack fluid)
  {
    if(fluid == null || fluid.amount == 0)
    {
      return null;
    }
    for(IAtomizerRecipe ar:recipes)
    {
      if(ar.matchesRecipe(fluid))
      {
        return ar;
      }
    }
    return null;
  }
  
  @Override
  public List<IAtomizerRecipe> getRecipes()
  {
    return Collections.unmodifiableList(recipes);
  }

  @Override
  public void removeRecipe(IAtomizerRecipe recipe)
  {
    recipes.remove(recipe);
  }
}
