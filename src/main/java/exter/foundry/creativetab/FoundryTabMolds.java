package exter.foundry.creativetab;

import java.util.Locale;

import exter.foundry.block.BlockFoundryMachine;
import exter.foundry.block.FoundryBlocks;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemMold;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class FoundryTabMolds extends CreativeTabs
{
  public static FoundryTabMolds tab = new FoundryTabMolds();

  private FoundryTabMolds()
  {
    super("foundryMolds");
  }
  
  @Override
  public ItemStack getIconItemStack()
  {
    return new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_INGOT);
  }
}
