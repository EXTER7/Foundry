package exter.foundry.creativetab;

import exter.foundry.block.FoundryBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

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
    return new ItemStack(FoundryBlocks.block_metal1,1,2);
  }

  @Override
  public Item getTabIconItem()
  {
    // TODO Auto-generated method stub
    return null;
  }
}
