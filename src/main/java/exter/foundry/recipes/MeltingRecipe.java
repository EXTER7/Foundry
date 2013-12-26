package exter.foundry.recipes;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exter.foundry.api.recipe.IMeltingRecipe;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
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
   * Item required.
   * It can be an {@link ItemStack} of the item or a @{link String} of it's Ore Dictionary name.
   */
  public final Object solid;
  
  /**
   * Melting point of the item in K.
   */
  public final int melting_point;
  
  public MeltingRecipe(Object item,FluidStack fluid_stack, int melt)
  {
    if(!(item instanceof ItemStack) && !(item instanceof String) && !(item instanceof Item) && !(item instanceof Block))
    {
      throw new IllegalArgumentException("Melting recipe item is not of a valid class.");
    }
    if(item instanceof ItemStack)
    {
      solid = ((ItemStack)item).copy();
    } else
    {
      solid = item;
    }
    fluid = fluid_stack.copy();
    melting_point = melt;
  }
  
  public Object GetInput()
  {
    if(solid instanceof ItemStack)
    {
      return ((ItemStack)solid).copy();
    }
    return solid;
  }
  
  public FluidStack GetOutput()
  {
    return fluid.copy();
  }

  @Override
  public int GetMeltingPoint()
  {
    return melting_point;
  }
  
  @Override
  public boolean MatchesRecipe(ItemStack item)
  {
    return FoundryMiscUtils.IsItemMatch(item, solid);
  }
}
