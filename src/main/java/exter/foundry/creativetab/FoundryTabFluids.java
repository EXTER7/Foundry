package exter.foundry.creativetab;

import exter.foundry.item.FoundryItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

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
    return FoundryItems.item_container.empty(1);
  }

  @Override
  public ItemStack getTabIconItem()
  {
    return FoundryItems.item_container.empty(1);
  }
}
