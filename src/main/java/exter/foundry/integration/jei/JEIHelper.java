package exter.foundry.integration.jei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import exter.foundry.api.orestack.OreStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class JEIHelper
{

  public static List<ItemStack> toItemStackList(@Nullable Object input)
  {
    if(input instanceof ItemStack)
    {
      return Collections.singletonList((ItemStack) input);
    }
    if(input instanceof String)
    {
      return OreDictionary.getOres((String) input);
    }
    if(input instanceof OreStack)
    {
      OreStack orestack = (OreStack)input;
      List<ItemStack> result = new ArrayList<ItemStack>();
      for(ItemStack ore:OreDictionary.getOres(orestack.name))
      {
        ore = ore.copy();
        ore.stackSize = orestack.amount;
        result.add(ore);
      }
      return result;
    }
    return Collections.emptyList();
  }
}
