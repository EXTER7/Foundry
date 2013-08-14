package exter.foundry.item;

import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.oredict.OreDictionary;

public class FoundryItems
{
  static private int component_id;
  static private int mold_id;
  static private int ingot_id;
  
  static public ItemFoundryComponent item_component;
  static public ItemMold item_mold;
  static public ItemIngot item_ingot;

  static public void RegisterItems(Configuration config)
  {
    int i;
    component_id = config.getItem("component", 9021).getInt();
    mold_id = config.getItem("mold", 9022).getInt();
    ingot_id = config.getItem("ingot", 9023).getInt();

    item_component = new ItemFoundryComponent(component_id);
    item_mold = new ItemMold(mold_id);
    item_ingot = new ItemIngot(ingot_id);
    
    for (i = 0; i < ItemFoundryComponent.NAMES.length; i++)
    {
      LanguageRegistry.addName(new ItemStack(item_component,  1, i), ItemFoundryComponent.NAMES[i]);
    }
    for (i = 0; i < ItemMold.NAMES.length; i++)
    {
      LanguageRegistry.addName(new ItemStack(item_mold,  1, i), ItemMold.NAMES[i]);
    }
    for (i = 0; i < ItemIngot.NAMES.length; i++)
    {
      ItemStack is = new ItemStack(item_ingot,  1, i);
      LanguageRegistry.addName(is, ItemIngot.NAMES[i]);
      OreDictionary.registerOre(ItemIngot.OREDICT_NAMES[i], is);
    }
  }
}
