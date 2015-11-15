package exter.foundry.recipes.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import exter.foundry.api.recipe.ICastingRecipe;
import exter.foundry.api.recipe.manager.ICastingRecipeManager;
import exter.foundry.recipes.CastingRecipe;

public class CastingRecipeManager implements ICastingRecipeManager
{
  public List<ICastingRecipe> recipes;
  public List<ItemStack> molds;

  public static final CastingRecipeManager instance = new CastingRecipeManager();

  private CastingRecipeManager()
  {
    recipes = new ArrayList<ICastingRecipe>();
    molds = new ArrayList<ItemStack>();
  }

  @Override
  public void AddRecipe(Object result,FluidStack in_fluid,ItemStack in_mold,Object in_extra,int cast_speed)
  {
    ICastingRecipe recipe = new CastingRecipe(result,in_fluid,in_mold,in_extra,cast_speed);
    if(recipe.RequiresExtra())
    {
      recipes.add(0,recipe);
    } else
    {
      recipes.add(recipe);
    }
  }

  @Override
  public void AddRecipe(Object result,FluidStack in_fluid,ItemStack in_mold,Object in_extra)
  {
    AddRecipe(result,in_fluid,in_mold,in_extra,100);
  }

  @Override
  public ICastingRecipe FindRecipe(FluidStack fluid,ItemStack mold,ItemStack extra)
  {
    if(mold == null || fluid == null || fluid.amount == 0)
    {
      return null;
    }
    for(ICastingRecipe cr:recipes)
    {
      if(cr.MatchesRecipe(mold, fluid, extra))
      {
        return cr;
      }
    }
    return null;
  }
  
  @Override
  public void AddMold(ItemStack mold)
  {
    molds.add(mold.copy());
  }

  @Override
  public boolean IsItemMold(ItemStack stack)
  {
    if(stack == null)
    {
      return false;
    }
    for(ItemStack m:molds)
    {
      if(m.isItemEqual(stack))
      {
        return true;
      }
    }
    return false;
  }

  @Override
  public List<ICastingRecipe> GetRecipes()
  {
    return Collections.unmodifiableList(recipes);
  }

  @Override
  public List<ItemStack> GetMolds()
  {
    return Collections.unmodifiableList(molds);
  }

  @Override
  public void RemoveRecipe(ICastingRecipe recipe)
  {
    recipes.remove(recipe);
  }
}
