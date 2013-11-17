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
  private List<CastingRecipe> recipes;
  private List<ItemStack> molds;

  public static final CastingRecipeManager instance = new CastingRecipeManager();

  private CastingRecipeManager()
  {
    recipes = new ArrayList<CastingRecipe>();
    molds = new ArrayList<ItemStack>();
  }

  @Override
  public void AddRecipe(Object result,FluidStack in_fluid,ItemStack in_mold,ItemStack in_extra)
  {
    recipes.add(new CastingRecipe(result,in_fluid,in_mold,in_extra));
  }

  @Override
  public void AddRecipe(Object result,FluidStack in_fluid,ItemStack in_mold,String in_extra,int extra_amount)
  {
    recipes.add(new CastingRecipe(result,in_fluid,in_mold,in_extra,extra_amount));
  }
  
  /**
   * Find a casting recipe given a FluidStack and a mold.
   * @param fluid FluidStack that contains the recipe's required fluid.
   * @param mold Mold used by the recipe.
   * @return The casting recipe, or null if no matching recipe.
   */
  public CastingRecipe FindRecipe(FluidStack fluid,ItemStack mold)
  {
    if(mold == null || fluid == null)
    {
      return null;
    }
    for(CastingRecipe cr:recipes)
    {
      if(fluid.containsFluid(cr.fluid) && cr.mold.isItemEqual(mold) && ItemStack.areItemStackTagsEqual(cr.mold, mold))
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

  /**
   * Check if an item is registered as a mold.
   * @param stack Item to check
   * @return true if an item is registered, false if not.
   */
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
  public List<? extends ICastingRecipe> GetRecipes()
  {
    return Collections.unmodifiableList(recipes);
  }
}
