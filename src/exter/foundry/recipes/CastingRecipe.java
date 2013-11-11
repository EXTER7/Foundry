package exter.foundry.recipes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Metal Caster recipe manager
 */
public class CastingRecipe
{
  static private List<CastingRecipe> recipes = new ArrayList<CastingRecipe>();
  static private List<ItemStack> molds = new ArrayList<ItemStack>();
  
  
  private FluidStack fluid;
  private ItemStack mold;
  private ItemStack extra;
  
  private ItemStack output;
  private String output_oredict;
  
  /**
   * Get fluid required for casting.
   * @return FluidStack containing the required fluid.
   */
  public FluidStack GetFluid()
  {
    return fluid.copy();
  }

  /**
   * Get mold required for casting.
   * @return ItemStack containing the required mold.
   */
  public ItemStack GetMold()
  {
    return mold.copy();
  }

  /**
   * Get extra item required for casting.
   * @return ItemStack containing the required extra item, or null if no extra item is required.
   */
  public ItemStack GetExtra()
  {
    if(extra == null)
    {
      return null;
    }
    return extra.copy();
  }


  /**
   * Get item produced by casting.
   * @return ItemStack containing the item produced.
   */
  public ItemStack GetOutput()
  {
    if(output_oredict != null)
    {
      List<ItemStack> ores = OreDictionary.getOres(output_oredict);
      if(ores != null && ores.size() > 0)
      {
        ItemStack out = ores.get(0).copy();
        out.stackSize = 1;
        return out;
      } else
      {
        return null;
      }
    } else
    {
      return output.copy();
    }
  }

  private CastingRecipe(ItemStack result,FluidStack in_fluid,ItemStack in_mold,ItemStack in_extra)
  {
    output = result.copy();
    output_oredict = null;
    fluid = in_fluid.copy();
    mold = in_mold.copy();
    if(in_extra != null)
    {
      extra = in_extra.copy();
    }
  }

  private CastingRecipe(String result_name,FluidStack in_fluid,ItemStack in_mold,ItemStack in_extra)
  {
    output_oredict = result_name;
    fluid = in_fluid.copy();
    mold = in_mold.copy();
    if(in_extra != null)
    {
      extra = in_extra.copy();
    }
  }

  /**
   * Register a Metal Caster recipe.
   * Note: the mold must be registered with {@link RegisterMold}.
   * @param result Item produced (Ore dictionary name).
   * @param in_fluid Fluid required (fluid type and amount).
   * @param in_mold Mold required.
   * @param in_extra Extra item required (null, if no extra item is required).
   */
  static public void RegisterRecipe(String result,FluidStack in_fluid,ItemStack in_mold,ItemStack in_extra)
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
  static public void RegisterRecipe(ItemStack result,FluidStack in_fluid,ItemStack in_mold,ItemStack in_extra)
  {
    recipes.add(new CastingRecipe(result,in_fluid,in_mold,in_extra));
  }
  
  /**
   * Find a casting recipe given a FluidStack and a mold.
   * @param fluid FluidStack that contains the recipe's required fluid.
   * @param mold Mold used by the recipe.
   * @return The casting recipe, or null if no matching recipe.
   */
  static public CastingRecipe FindRecipe(FluidStack fluid,ItemStack mold)
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
  static public void RegisterMold(ItemStack mold)
  {
    molds.add(mold.copy());
  }

  /**
   * Check if an item is registered as a mold.
   * @param stack Item to check
   * @return true if an item is registered, false if not.
   */
  static public boolean IsItemMold(ItemStack stack)
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
