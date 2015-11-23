package exter.foundry.recipes.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraftforge.fluids.FluidStack;
import exter.foundry.api.recipe.IAlloyMixerRecipe;
import exter.foundry.api.recipe.manager.IAlloyMixerRecipeManager;
import exter.foundry.recipes.AlloyMixerRecipe;

public class AlloyMixerRecipeManager implements IAlloyMixerRecipeManager
{
  public List<IAlloyMixerRecipe> recipes;

  public static final AlloyMixerRecipeManager instance = new AlloyMixerRecipeManager();

  private int[] recipe_order;

  
  private AlloyMixerRecipeManager()
  {
    recipes = new ArrayList<IAlloyMixerRecipe>();
    recipe_order = new int[4];
  }
  
  @Override
  public void AddRecipe(FluidStack out, FluidStack[] in)
  {
    recipes.add(new AlloyMixerRecipe(out,in));
  }

  @Override
  public IAlloyMixerRecipe FindRecipe(FluidStack[] in,int[] order)
  {
    int inputs = 0;
    if(in == null || in.length > 4)
    {
      return null;
    }
    IAlloyMixerRecipe result = null;
    if(order != null && order.length < 4)
    {
      order = null;
    }
    for(IAlloyMixerRecipe r:recipes)
    {
      if(r.MatchesRecipe(in,recipe_order) && r.GetInputCount() > inputs)
      {
        if(order != null)
        {
          System.arraycopy(recipe_order, 0, order, 0, recipe_order.length);
        }
        inputs = r.GetInputCount();
        result = r;
      }
    }
    return result;
  }

  @Override
  public List<IAlloyMixerRecipe> GetRecipes()
  {
    return Collections.unmodifiableList(recipes);
  }

  @Override
  public void RemoveRecipe(IAlloyMixerRecipe recipe)
  {
    recipes.remove(recipe);
  }

}
