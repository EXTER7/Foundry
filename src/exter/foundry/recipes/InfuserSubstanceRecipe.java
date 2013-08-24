package exter.foundry.recipes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class InfuserSubstanceRecipe
{
  static private List<InfuserSubstanceRecipe> recipes = new ArrayList<InfuserSubstanceRecipe>();
  
  public final InfuserSubstance substance;
  public final ItemStack item;
  public final String oredict_item;
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

  static public void RegisterRecipe(InfuserSubstance subs,ItemStack itm, int time)
  {
    recipes.add(new InfuserSubstanceRecipe(subs,itm,time));
  }

  static public void RegisterRecipe(InfuserSubstance subs,String itm, int time)
  {
    recipes.add(new InfuserSubstanceRecipe(subs,itm,time));
  }

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
