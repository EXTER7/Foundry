package exter.foundry.recipes;

import java.util.List;

import exter.foundry.api.orestack.OreStack;
import exter.foundry.api.recipe.ICastingRecipe;
import exter.foundry.util.FoundryMiscUtils;
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
  
  @Override
  public boolean ContainsExtra(ItemStack stack)
  {
    if(stack == null)
    {
      return extra == null;
    }
    return FoundryMiscUtils.IsItemMatch(stack, extra) && stack.stackSize >= FoundryMiscUtils.GetStackSize(extra);
  }
  
  @Override
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
    } else if(extra instanceof OreStack)
    {
      return new OreStack((OreStack)extra);
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

  @Override
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

  public CastingRecipe(Object result,FluidStack in_fluid,ItemStack in_mold,Object in_extra)
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
    if(in_extra instanceof OreStack)
    {
      extra = new OreStack((OreStack)in_extra);
    } else if(in_extra instanceof ItemStack)
    {
      extra = ((ItemStack)in_extra).copy();
    } else
    {
      if(in_extra != null)
      {
        throw new IllegalArgumentException("Casting recipe extra item is not of a valid class.");
      }
      extra = null;
    }
  }
  
  @Override
  public boolean MatchesRecipe(ItemStack mold_stack,FluidStack fluid_stack)
  {
    if(GetOutputItem() == null)
    {
      return false;
    }
    return fluid_stack != null && fluid_stack.containsFluid(fluid) && mold_stack != null && mold.isItemEqual(mold_stack) && ItemStack.areItemStackTagsEqual(mold, mold_stack);
  }
}
