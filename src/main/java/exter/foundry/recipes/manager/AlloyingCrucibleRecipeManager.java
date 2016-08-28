package exter.foundry.recipes.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import exter.foundry.api.recipe.IAlloyingCrucibleRecipe;
import exter.foundry.api.recipe.manager.IAlloyingCrucibleRecipeManager;
import exter.foundry.recipes.AlloyingCrucibleRecipe;
import net.minecraftforge.fluids.FluidStack;

public class AlloyingCrucibleRecipeManager implements IAlloyingCrucibleRecipeManager
{
  public List<IAlloyingCrucibleRecipe> recipes;

  public static final AlloyingCrucibleRecipeManager instance = new AlloyingCrucibleRecipeManager();
  
  private AlloyingCrucibleRecipeManager()
  {
    recipes = new ArrayList<IAlloyingCrucibleRecipe>();
  }
  
  @Override
  public void addRecipe(FluidStack out, FluidStack in_a,FluidStack in_b)
  {
    recipes.add(new AlloyingCrucibleRecipe(out,in_a,in_b));
  }

  @Override
  public IAlloyingCrucibleRecipe findRecipe(FluidStack in_a,FluidStack in_b)
  {
    for(IAlloyingCrucibleRecipe r:recipes)
    {
      if(r.matchesRecipe(in_a,in_b))
      {
        return r;
      }
    }
    return null;
  }

  @Override
  public List<IAlloyingCrucibleRecipe> getRecipes()
  {
    return Collections.unmodifiableList(recipes);
  }

  @Override
  public void removeRecipe(IAlloyingCrucibleRecipe recipe)
  {
    recipes.remove(recipe);    
  }
}
