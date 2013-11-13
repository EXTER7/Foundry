package exter.foundry.recipes;

import java.util.ArrayList;
import java.util.List;

import exter.foundry.api.recipe.IInfuserSubstanceRecipe;
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
   * Item required. Not used if oredict_item is not null.
   */
  public final ItemStack item;

  /**
   * Ore dictionary name of the item. If not null this is used instead of item.
   */
  public final String oredict_item;

  /**
   * Amount of energy needed to extract.
   */
  public final int extract_energy;
  
  @Override
  public Object GetInputItem()
  {
    return item.copy();
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
  
  public InfuserSubstanceRecipe(InfuserSubstance subs,ItemStack itm, int energy)
  {
    substance = new InfuserSubstance(subs);
    item = itm.copy();
    oredict_item = null;
    extract_energy = energy;
  }

  public InfuserSubstanceRecipe(InfuserSubstance subs,String itm, int energy)
  {
    substance = new InfuserSubstance(subs);
    item = null;
    oredict_item = itm;
    extract_energy = energy;
  }
}
