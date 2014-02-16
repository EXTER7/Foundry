package exter.foundry.creativetab;

import exter.foundry.block.BlockFoundryMachine;
import exter.foundry.block.FoundryBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class FoundryTabMachines extends CreativeTabs
{
  public static FoundryTabMachines tab = new FoundryTabMachines();

  private FoundryTabMachines()
  {
    super("foundryMachines");
  }
  
  @Override
  public ItemStack getIconItemStack()
  {
    return new ItemStack(FoundryBlocks.block_machine,1,BlockFoundryMachine.MACHINE_ICF);
  }

  @Override
  public Item getTabIconItem()
  {
    return null;
  }
}
