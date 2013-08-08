package exter.foundry.item;

import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;

public class FoundryItems
{
  static private int component_id;
  static private int mold_id;
  
  static public ItemFoundryComponent item_component;
  static public ItemMold item_mold;

  static public void RegisterItems(Configuration config)
  {
    int i;
    component_id = config.get(Configuration.CATEGORY_ITEM, "component", 9021).getInt(9021);
    mold_id = config.get(Configuration.CATEGORY_ITEM, "mold", 9022).getInt(9022);

    item_component = new ItemFoundryComponent(component_id);
    item_mold = new ItemMold(mold_id);
    
    for (i = 0; i < ItemFoundryComponent.NAMES.length; i++)
    {
      LanguageRegistry.addName(new ItemStack(item_component,  1, i), ItemFoundryComponent.NAMES[i]);
    }
    for (i = 0; i < ItemMold.NAMES.length; i++)
    {
      LanguageRegistry.addName(new ItemStack(item_mold,  1, i), ItemMold.NAMES[i]);
    }
  }
}
