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
  
  static public ItemFoundryContainer item_container_empty;
  static public ItemFoundryContainer item_container_iron;
  static public ItemFoundryContainer item_container_gold;
  static public ItemFoundryContainer item_container_copper;
  static public ItemFoundryContainer item_container_tin;
  static public ItemFoundryContainer item_container_bronze;
  static public ItemFoundryContainer item_container_electrum;
  static public ItemFoundryContainer item_container_invar;
  static public ItemFoundryContainer item_container_nickel;
  static public ItemFoundryContainer item_container_zinc;
  static public ItemFoundryContainer item_container_brass;
  static public ItemFoundryContainer item_container_silver;
  static public ItemFoundryContainer item_container_steel;
  static public ItemFoundryContainer item_container_lead;

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
  }

  static public void RegisterContainerItems(Configuration config)
  {
    item_container_empty = new ItemFoundryContainer(config.getItem("container_empty", 9024).getInt(),null);
    item_container_iron = new ItemFoundryContainer(config.getItem("container_iron", 9025).getInt(),LiquidMetalRegistry.GetMetal("Iron").fluid);
    item_container_gold = new ItemFoundryContainer(config.getItem("container_gold", 9026).getInt(),LiquidMetalRegistry.GetMetal("Gold").fluid);
    item_container_copper = new ItemFoundryContainer(config.getItem("container_copper", 9027).getInt(),LiquidMetalRegistry.GetMetal("Copper").fluid);
    item_container_tin = new ItemFoundryContainer(config.getItem("container_tin", 9028).getInt(),LiquidMetalRegistry.GetMetal("Tin").fluid);
    item_container_bronze = new ItemFoundryContainer(config.getItem("container_bronze", 9029).getInt(),LiquidMetalRegistry.GetMetal("Bronze").fluid);
    item_container_electrum = new ItemFoundryContainer(config.getItem("container_electrum", 9030).getInt(),LiquidMetalRegistry.GetMetal("Electrum").fluid);
    item_container_invar = new ItemFoundryContainer(config.getItem("container_invar", 9031).getInt(),LiquidMetalRegistry.GetMetal("Invar").fluid);
    item_container_nickel = new ItemFoundryContainer(config.getItem("container_nickel", 9032).getInt(),LiquidMetalRegistry.GetMetal("Nickel").fluid);
    item_container_zinc = new ItemFoundryContainer(config.getItem("container_zinc", 9033).getInt(),LiquidMetalRegistry.GetMetal("Zinc").fluid);
    item_container_brass = new ItemFoundryContainer(config.getItem("container_brass", 9034).getInt(),LiquidMetalRegistry.GetMetal("Brass").fluid);
    item_container_silver = new ItemFoundryContainer(config.getItem("container_silver", 9035).getInt(),LiquidMetalRegistry.GetMetal("Silver").fluid);
    item_container_steel = new ItemFoundryContainer(config.getItem("container_steel", 9036).getInt(),LiquidMetalRegistry.GetMetal("Steel").fluid);
    item_container_lead = new ItemFoundryContainer(config.getItem("container_lead", 9037).getInt(),LiquidMetalRegistry.GetMetal("Lead").fluid);

}
  
  @SideOnly(Side.CLIENT)
  static public void RengisterContainerItemsRenderers()
  {
    MinecraftForgeClient.registerItemRenderer(item_container_empty.itemID, new RendererItemContainer());
    MinecraftForgeClient.registerItemRenderer(item_container_iron.itemID, new RendererItemContainer());
    MinecraftForgeClient.registerItemRenderer(item_container_gold.itemID, new RendererItemContainer());
    MinecraftForgeClient.registerItemRenderer(item_container_copper.itemID, new RendererItemContainer());
    MinecraftForgeClient.registerItemRenderer(item_container_tin.itemID, new RendererItemContainer());
    MinecraftForgeClient.registerItemRenderer(item_container_bronze.itemID, new RendererItemContainer());
    MinecraftForgeClient.registerItemRenderer(item_container_electrum.itemID, new RendererItemContainer());
    MinecraftForgeClient.registerItemRenderer(item_container_invar.itemID, new RendererItemContainer());
    MinecraftForgeClient.registerItemRenderer(item_container_nickel.itemID, new RendererItemContainer());
    MinecraftForgeClient.registerItemRenderer(item_container_zinc.itemID, new RendererItemContainer());
    MinecraftForgeClient.registerItemRenderer(item_container_brass.itemID, new RendererItemContainer());
    MinecraftForgeClient.registerItemRenderer(item_container_silver.itemID, new RendererItemContainer());
    MinecraftForgeClient.registerItemRenderer(item_container_steel.itemID, new RendererItemContainer());
    MinecraftForgeClient.registerItemRenderer(item_container_lead.itemID, new RendererItemContainer());

  }
}
