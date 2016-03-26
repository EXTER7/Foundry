package exter.foundry.creativetab;

import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemComponent;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class FoundryTabMaterials extends CreativeTabs
{
  public static FoundryTabMaterials tab = new FoundryTabMaterials();

  private FoundryTabMaterials()
  {
    super("foundryMaterials");
  }
  
  @Override
  public ItemStack getIconItemStack()
  {
    return FoundryItems.component(ItemComponent.SubItem.REFRACTORYCLAY);
  }

  @Override
  public Item getTabIconItem()
  {
    return null;
  }
}
