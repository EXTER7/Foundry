package exter.foundry.creativetab;

import java.util.Locale;

import exter.foundry.block.BlockFoundryMachine;
import exter.foundry.block.BlockMetal;
import exter.foundry.block.FoundryBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class FoundryTabBlocks extends CreativeTabs
{
  public static FoundryTabBlocks tab = new FoundryTabBlocks();

  private FoundryTabBlocks()
  {
    super("foundryBlocks");
  }
  
  @Override
  public ItemStack getIconItemStack()
  {
    return new ItemStack(FoundryBlocks.block_metal,1,BlockMetal.BLOCK_BRONZE);
  }
}
