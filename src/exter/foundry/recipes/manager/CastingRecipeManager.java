package exter.foundry.recipes.manager;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
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

  /**
   * Register a Metal Caster recipe.
   * Note: the mold must be registered with {@link RegisterMold}.
   * @param result Item produced (Ore dictionary name).
   * @param in_fluid Fluid required (fluid type and amount).
   * @param in_mold Mold required.
   * @param in_extra Extra item required (null, if no extra item is required).
   */
  public void AddRecipe(String result,FluidStack in_fluid,ItemStack in_mold,ItemStack in_extra)
  {
    recipes.add(new CastingRecipe(result,in_fluid,in_mold,in_extra));
  }

  /**
   * Register a Metal Caster recipe.
   * Note: the mold must be registered with {@link RegisterMold}.
   * @param result Item produced.
   * @param in_fluid Fluid required (fluid type and amount).
   * @param in_mold Mold required.
   * @param in_extra Extra item required (null, if no extra item is required).
   */
  public void AddRecipe(ItemStack result,FluidStack in_fluid,ItemStack in_mold,ItemStack in_extra)
  {
    recipes.add(new CastingRecipe(result,in_fluid,in_mold,in_extra));
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
      if(fluid.containsFluid(cr.fluid) && cr.mold.isItemEqual(mold))
      {
        return cr;
      }
    }
    return null;
  }
  
  /**
   * Register an item as a mold. Only registered items are accepted in the Metal Caster's mold slot.
   * @param mold Item to be registered.
   */
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
}
