package exter.foundry.recipes.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraftforge.fluids.FluidStack;
import exter.foundry.ModFoundry;
import exter.foundry.api.recipe.IAlloyRecipe;
import exter.foundry.api.recipe.manager.IAlloyRecipeManager;
import exter.foundry.recipes.AlloyRecipe;

public class AlloyRecipeManager implements IAlloyRecipeManager
{
  private List<AlloyRecipe> recipes;

  public static final AlloyRecipeManager instance = new AlloyRecipeManager();

  private int[] recipe_order;

  
  private AlloyRecipeManager()
  {
    recipes = new ArrayList<AlloyRecipe>();
    recipe_order = new int[4];
  }
  
  @Override
  public void AddRecipe(FluidStack out, FluidStack[] in)
  {
    recipes.add(new AlloyRecipe(out,in));
  }

  /**
   * Find a valid recipe that contains the given inputs.
   * A recipe is found if the recipe's inputs contains the fluid in the parameters.
   * @param in_a FluidStack for the first input.
   * @param in_b FluidStack for the second input.
   * @return
   */
  public AlloyRecipe FindRecipe(FluidStack[] in,int[] order)
  {
    int inputs = 0;
    AlloyRecipe result = null;
    if(order.length < 4)
    {
      order = null;
    }
    for(AlloyRecipe r:recipes)
    {
      if(r.MatchesRecipe(in,recipe_order) && r.inputs.length > inputs)
      {
        if(order != null)
        {
          System.arraycopy(recipe_order, 0, order, 0, recipe_order.length);
        }
        inputs = r.inputs.length;
        result = r;
      }
    }
    return result;
  }

  @Override
  public List<? extends IAlloyRecipe> GetRecipes()
  {
    return Collections.unmodifiableList(recipes);
  }

}
