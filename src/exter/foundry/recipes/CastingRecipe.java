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
  public final FluidStack fluid;
  public final ItemStack mold;
  public final ItemStack extra;
  
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

  public CastingRecipe(ItemStack result,FluidStack in_fluid,ItemStack in_mold,ItemStack in_extra)
  {
    output = result.copy();
    output_oredict = null;
    fluid = in_fluid.copy();
    mold = in_mold.copy();
    if(in_extra != null)
    {
      extra = in_extra.copy();
    } else
    {
      extra = null;
    }
  }

  public CastingRecipe(String result_name,FluidStack in_fluid,ItemStack in_mold,ItemStack in_extra)
  {
    output_oredict = result_name;
    output = null;
    fluid = in_fluid.copy();
    mold = in_mold.copy();
    if(in_extra != null)
    {
      extra = in_extra.copy();
    } else
    {
      extra = null;
    }
  }
}
