package exter.foundry.item;

import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.LiquidMetalRegistry;
import exter.foundry.renderer.RendererItemContainer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.oredict.OreDictionary;

public class FoundryItems
{

  
  static public ItemFoundryComponent item_component;
  static public ItemMold item_mold;
  static public ItemIngot item_ingot;
  
  static public ItemEmptyFoundryContainer item_container_empty;

  static public void RegisterItems(Configuration config)
  {
    int i;

    item_component = new ItemFoundryComponent(config.getItem("component", 9021).getInt());
    item_mold = new ItemMold(config.getItem("mold", 9022).getInt());
    item_ingot = new ItemIngot(config.getItem("ingot", 9023).getInt());
    
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
    item_container_empty = new ItemEmptyFoundryContainer(config.getItem("container_empty", 9024).getInt());

  }
}
