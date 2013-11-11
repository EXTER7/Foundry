package exter.foundry.recipes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Metal Infuser substance recipe manager
 */
public class InfuserSubstanceRecipe
{
  static private List<InfuserSubstanceRecipe> recipes = new ArrayList<InfuserSubstanceRecipe>();
  
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
  public int extract_time;
  
  private InfuserSubstanceRecipe(InfuserSubstance subs,ItemStack itm, int time)
  {
    substance = subs;
    item = itm.copy();
    oredict_item = null;
    extract_time = time;
  }

  private InfuserSubstanceRecipe(InfuserSubstance subs,String itm, int time)
  {
    substance = subs;
    item = null;
    oredict_item = itm;
    extract_time = time;
  }

  /**
   * Register a Metal Infuser substance recipe.
   * @param subs Substance produced.
   * @param itm Item required.
   * @param time Energy required.
   */
  static public void RegisterRecipe(InfuserSubstance subs,ItemStack itm, int time)
  {
    recipes.add(new InfuserSubstanceRecipe(subs,itm,time));
  }

  /**
   * Register a Metal Infuser substance recipe.
   * @param subs Substance produced.
   * @param itm Item required (Ore Dictionary name).
   * @param time Energy required.
   */
  static public void RegisterRecipe(InfuserSubstance subs,String itm, int time)
  {
    recipes.add(new InfuserSubstanceRecipe(subs,itm,time));
  }

  /**
   * Find a substance recipe given a Item.
   * @param item The item required in the recipe
   * @return The substance recipe, or null if no matching recipe.
   */
  static public InfuserSubstanceRecipe FindRecipe(ItemStack item)
  {
    if(item == null)
    {
      return null;
    }
    for(InfuserSubstanceRecipe isr:recipes)
    {
      if(isr.oredict_item != null)
      {
        List<ItemStack> ores = OreDictionary.getOres(isr.oredict_item);
        if(ores != null)
        {
          for(ItemStack ore:ores)
          {
            if(ore.isItemEqual(item))
            {
              return isr;
            }
          }
        }
      } else if(isr.item.isItemEqual(item))
      {
        return isr;
      }
    }
    return null;
  }
}
