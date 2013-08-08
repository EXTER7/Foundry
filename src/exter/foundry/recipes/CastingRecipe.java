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
  
  public FluidStack GetFluid()
  {
    return fluid.copy();
  }

  public ItemStack GetMold()
  {
    return mold.copy();
  }

  public ItemStack GetExtra()
  {
    if(extra == null)
    {
      return null;
    }
    return extra.copy();
  }


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

  static public void RegisterRecipe(String result,FluidStack in_fluid,ItemStack in_mold,ItemStack in_extra)
  {
    recipes.add(new CastingRecipe(result,in_fluid,in_mold,in_extra));
  }

  static public void RegisterRecipe(ItemStack result,FluidStack in_fluid,ItemStack in_mold,ItemStack in_extra)
  {
    recipes.add(new CastingRecipe(result,in_fluid,in_mold,in_extra));
  }
  
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
  
  static public void RegisterMold(ItemStack mold)
  {
    molds.add(mold.copy());
  }

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
