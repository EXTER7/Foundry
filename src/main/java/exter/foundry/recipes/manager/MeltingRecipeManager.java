package exter.foundry.recipes.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import exter.foundry.api.recipe.IMeltingRecipe;
import exter.foundry.api.recipe.manager.IMeltingRecipeManager;
import exter.foundry.recipes.MeltingRecipe;

public class MeltingRecipeManager implements IMeltingRecipeManager
{
  private List<IMeltingRecipe> recipes;
  
  public static final MeltingRecipeManager instance = new MeltingRecipeManager();

  private MeltingRecipeManager()
  {
    recipes = new ArrayList<IMeltingRecipe>();
  }

  @Override
  public IMeltingRecipe FindRecipe(ItemStack item)
  {
    if(item == null)
    {
      return null;
    }
    for (IMeltingRecipe r : recipes)
    {
      if(r.MatchesRecipe(item))
      {
        return r;
      }
    }
    return null;
  }
  
  @Override
  public void AddRecipe(Object solid,FluidStack fluid_stack)
  {
    recipes.add(new MeltingRecipe(solid,fluid_stack,fluid_stack.getFluid().getTemperature()));
  }
  
  @Override
  public void AddRecipe(Object solid,FluidStack fluid_stack,int melting_point)
  {
    recipes.add(new MeltingRecipe(solid,fluid_stack,melting_point));
  }

  @Override
  public List<IMeltingRecipe> GetRecipes()
  {
    return Collections.unmodifiableList(recipes);
  }

}
