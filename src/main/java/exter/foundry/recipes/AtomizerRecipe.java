package exter.foundry.recipes;

import java.util.List;

import exter.foundry.api.recipe.IAtomizerRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Metal Caster recipe manager
 */
public class AtomizerRecipe implements IAtomizerRecipe
{
  private final FluidStack fluid;
  
  private final Object output;
  
  
  @Override
  public FluidStack GetInputFluid()
  {
    return fluid.copy();
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

  public AtomizerRecipe(Object result,FluidStack in_fluid)
  {
    if(result instanceof ItemStack)
    {
      output = ((ItemStack)result).copy();
    } else if(result instanceof String)
    {
      output = result;
    } else
    {
      throw new IllegalArgumentException("Atomizer recipe output is not of a valid class.");
    }
    if(in_fluid == null)
    {
      throw new IllegalArgumentException("Atomizer recipe input cannot be null");
    }
    fluid = in_fluid.copy();
  }
  
  @Override
  public boolean MatchesRecipe(FluidStack fluid_stack)
  {
    if(GetOutputItem() == null)
    {
      return false;
    }
    return fluid_stack != null && fluid_stack.containsFluid(fluid);
  }
}
