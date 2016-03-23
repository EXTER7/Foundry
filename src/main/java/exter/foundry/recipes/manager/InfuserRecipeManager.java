package exter.foundry.recipes.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import exter.foundry.api.recipe.IInfuserRecipe;
import exter.foundry.api.recipe.manager.IInfuserRecipeManager;
import exter.foundry.recipes.InfuserRecipe;


public class InfuserRecipeManager implements IInfuserRecipeManager
{
  public List<IInfuserRecipe> recipes;

  public static final InfuserRecipeManager instance = new InfuserRecipeManager();


  private InfuserRecipeManager()
  {
    recipes = new ArrayList<IInfuserRecipe>();
  }
  
  
  
  @Override
  public void addRecipe(FluidStack result,FluidStack in_fluid,Object item, int energy)
  {
    recipes.add(new InfuserRecipe(result,in_fluid,item,energy));
  }
  
  @Override
  public IInfuserRecipe findRecipe(FluidStack fluid,ItemStack item)
  {
    if(fluid == null || item == null)
    {
      return null;
    }
    for(IInfuserRecipe ir:recipes)
    {
      if(ir.matchesRecipe(fluid, item))
      {
        return ir;
      }
    }
    return null;
  }
  


  @Override
  public List<IInfuserRecipe> getRecipes()
  {
    return Collections.unmodifiableList(recipes);
  }

  @Override
  public void removeRecipe(IInfuserRecipe recipe)
  {
    recipes.remove(recipe);
  }
}
