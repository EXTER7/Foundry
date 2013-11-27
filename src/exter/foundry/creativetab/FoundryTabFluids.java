package exter.foundry.creativetab;

import java.util.Locale;

import exter.foundry.block.BlockFoundryMachine;
import exter.foundry.block.FoundryBlocks;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemRefractoryFluidContainer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class FoundryTabFluids extends CreativeTabs
{
  public static FoundryTabFluids tab = new FoundryTabFluids();

  private FoundryTabFluids()
  {
    super("foundryFluids");
  }
  
  @Override
  public ItemStack getIconItemStack()
  {
    return FoundryItems.item_container.EmptyContainer();
  }
}
