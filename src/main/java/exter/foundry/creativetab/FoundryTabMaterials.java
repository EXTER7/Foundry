package exter.foundry.creativetab;

import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemFoundryComponent;
import net.minecraft.creativetab.CreativeTabs;
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
    return new ItemStack(FoundryItems.item_component,1,ItemFoundryComponent.COMPONENT_FOUNDRYBRICK);
  }
}
