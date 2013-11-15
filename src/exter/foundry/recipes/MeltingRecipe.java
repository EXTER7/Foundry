package exter.foundry.recipes;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exter.foundry.api.recipe.IMeltingRecipe;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Metal Smelter recipe manager
 */
public class MeltingRecipe implements IMeltingRecipe
{
  /**
   * Produced fluid and amount.
   */
  public final FluidStack fluid;

  /**
   * Item required. Not used if solid_oredict is not null.
   */
  public final ItemStack solid;
  
  /**
   * Ore dictionary name of the required item.
   */
  public final String solid_oredict;
  
  public MeltingRecipe(ItemStack item,FluidStack fluid_stack)
  {
    solid = item;
    solid_oredict = null;
    fluid = fluid_stack.copy();
  }

  public MeltingRecipe(String item,FluidStack fluid_stack)
  {
    solid = null;
    solid_oredict = item;
    fluid = fluid_stack.copy();
  }
  
  public Object GetInput()
  {
    if(solid_oredict != null)
    {
      return solid_oredict;
    } else
    {
      return solid.copy();
    }
  }
  
  public FluidStack GetOutput()
  {
    return fluid.copy();
  }
  
  public boolean IsItemMatch(ItemStack item)
  {
    if(item == null)
    {
      return false;
    }
    if(solid_oredict != null)
    {
      if(FoundryMiscUtils.IsItemInOreDictionary(solid_oredict, item))
      {
        return true;
      }
    } else if(item.isItemEqual(solid) && ItemStack.areItemStackTagsEqual(item, solid))
    {
      return true;
    }
    return false;
  }
}
