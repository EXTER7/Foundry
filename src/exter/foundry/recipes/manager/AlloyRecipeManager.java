package exter.foundry.recipes.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraftforge.fluids.FluidStack;
import exter.foundry.api.recipe.IAlloyRecipe;
import exter.foundry.api.recipe.manager.IAlloyRecipeManager;
import exter.foundry.recipes.AlloyRecipe;

public class AlloyRecipeManager implements IAlloyRecipeManager
{
  private List<AlloyRecipe> recipes;

  public static final AlloyRecipeManager instance = new AlloyRecipeManager();

  
  private AlloyRecipeManager()
  {
    recipes = new ArrayList<AlloyRecipe>();
  }
  
  @Override
  public void AddRecipe(FluidStack out,FluidStack in_a,FluidStack in_b)
  {
    recipes.add(new AlloyRecipe(out,in_a,in_b));
  }
  
  /**
   * Find a valid recipe that contains the given inputs.
   * A recipe is found if the recipe's inputs contains the fluid in the parameters.
   * @param in_a FluidStack for the first input.
   * @param in_b FluidStack for the second input.
   * @return
   */
  public AlloyRecipe FindRecipe(FluidStack in_a,FluidStack in_b)
  {
    for(AlloyRecipe r:recipes)
    {
      if(r.MatchesRecipe(in_a, in_b))
      {
        return r;
      }
    }
    return null;
  }

  @Override
  public List<? extends IAlloyRecipe> GetRecipes()
  {
    return Collections.unmodifiableList(recipes);
  }
}
