package exter.foundry.creativetab;

import exter.foundry.block.FoundryBlocks;
import exter.foundry.block.BlockFoundryMachine.EnumMachine;
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
    return FoundryBlocks.block_machine.asItemStack(EnumMachine.ICF);
  }

  @Override
  public Item getTabIconItem()
  {
    return null;
  }
}
