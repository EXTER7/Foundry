package exter.foundry.recipes;

import java.util.ArrayList;
import java.util.List;

import exter.foundry.api.recipe.IInfuserSubstanceRecipe;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Metal Infuser substance recipe manager
 */
public class InfuserSubstanceRecipe implements IInfuserSubstanceRecipe
{
  /**
   * Substance produced.
   */
  public final InfuserSubstance substance;

  /**
   * Item required.
   */
  public final Object item;

  /**
   * Amount of energy needed to extract.
   */
  public final int extract_energy;
  
  @Override
  public Object GetInputItem()
  {
    if(item instanceof ItemStack)
    {
      return ((ItemStack)item).copy();
    }
    return item;
  }
  
  @Override
  public String GetOutputSubstanceType()
  {
    return substance.type;
  }
  
  @Override
  public int GetOutputSubstanceAmount()
  {
    return substance.amount;
  }
  
  @Override
  public int GetEneryNeeded()
  {
    return extract_energy;
  }
  
  public InfuserSubstanceRecipe(InfuserSubstance subs,Object itm, int energy)
  {
    if(!(itm instanceof ItemStack) && !(itm instanceof String) && !(itm instanceof Item) && !(itm instanceof Block))
    {
      throw new IllegalArgumentException("Infuser substance recipe item is not of a valid class.");
    }
    substance = new InfuserSubstance(subs);
    if(itm instanceof ItemStack)
    {
      item = ((ItemStack)itm).copy();
    } else
    {
      item = itm;
    }
    extract_energy = energy;
  }
}
