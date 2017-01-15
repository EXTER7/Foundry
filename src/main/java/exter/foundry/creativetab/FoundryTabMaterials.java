package exter.foundry.creativetab;

import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemComponent;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class FoundryTabMaterials extends CreativeTabs
{
  public static FoundryTabMaterials tab = new FoundryTabMaterials();

  private FoundryTabMaterials()
  {
    super("foundry.materials");
  }
  
  @Override
  public ItemStack getIconItemStack()
  {
    return FoundryItems.component(ItemComponent.SubItem.REFRACTORYCLAY);
  }

  @Override
  public ItemStack getTabIconItem()
  {
    return FoundryItems.component(ItemComponent.SubItem.REFRACTORYCLAY);
  }
}
