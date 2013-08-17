package exter.foundry;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;


/**
 * Miscellaneous utility methods
 */
public class FoundryUtils
{
  static public boolean IsItemInOreDictionary(String name,ItemStack stack)
  {
    List<ItemStack> ores = OreDictionary.getOres(name);
    for(ItemStack i:ores)
    {
      if(i.isItemEqual(stack))
      {
        return true;
      }
    }
    return false;
  }

  //Register item in the ore dictionary only if its not already registered
  static public void RegisterInOreDictionary(String name,ItemStack stack)
  {
    if(stack == null)
    {
      return;
    }
    if(!IsItemInOreDictionary(name,stack))
    {
      OreDictionary.registerOre(name, stack);
    }
  }

}
