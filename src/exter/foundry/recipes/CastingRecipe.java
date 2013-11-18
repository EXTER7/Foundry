package exter.foundry.recipes;

import java.util.ArrayList;
import java.util.List;

import exter.foundry.api.recipe.ICastingRecipe;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Metal Caster recipe manager
 */
public class CastingRecipe implements ICastingRecipe
{
  public final FluidStack fluid;
  public final ItemStack mold;
  public final Object extra;
  public final int extra_amount;
  
  private final Object output;
  
  @Override
  public FluidStack GetInputFluid()
  {
    return fluid.copy();
  }

  @Override
  public ItemStack GetInputMold()
  {
    return mold.copy();
  }
  
  public boolean ContainsExtra(ItemStack stack)
  {
    if(stack == null)
    {
      return extra == null;
    }
    return (FoundryMiscUtils.IsItemMatch(stack, extra) && stack.stackSize >= extra_amount);
  }
  
  public boolean RequiresExtra()
  {
    return extra != null;
  }

  @Override
  public Object GetInputExtra()
  {
    if(extra instanceof ItemStack)
    {
      return ((ItemStack)extra).copy();
    }
    return extra;
  }

  @Override
  public Object GetOutput()
  {
    if(output instanceof ItemStack)
    {
      return ((ItemStack)output).copy();
    }
    return output;
  }

  /**
   * Get item produced by casting.
   * @return ItemStack containing the item produced.
   */
  public ItemStack GetOutputItem()
  {
    if(output instanceof String)
    {
      List<ItemStack> ores = OreDictionary.getOres((String)output);
      if(ores != null && ores.size() > 0)
      {
        ItemStack out = ores.get(0).copy();
        out.stackSize = 1;
        return out;
      } else
      {
        return null;
      }
    } else // output instance of ItemStack
    {
      return ((ItemStack)output).copy();
    }
  }

  public CastingRecipe(Object result,FluidStack in_fluid,ItemStack in_mold,String in_extra,int in_extra_amount)
  {
    if(result instanceof ItemStack)
    {
      output = ((ItemStack)result).copy();
    } else if(result instanceof String)
    {
      output = result;
    } else
    {
      throw new IllegalArgumentException("Casting recipe result is not of a valid class.");
    }
    fluid = in_fluid.copy();
    mold = in_mold.copy();
    extra = in_extra;
    extra_amount = in_extra_amount;
  }

  public CastingRecipe(Object result,FluidStack in_fluid,ItemStack in_mold,ItemStack in_extra)
  {
    output = result;
    fluid = in_fluid.copy();
    mold = in_mold.copy();
    if(in_extra != null)
    {
      extra = in_extra.copy();
      extra_amount = in_extra.stackSize;
    } else
    {
      extra = null;
      extra_amount = 0;
    }
  }

  @Override
  public int GetInputExtraAmount()
  {
    return extra_amount;
  }
}
