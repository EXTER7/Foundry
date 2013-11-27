package exter.foundry.item;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import exter.foundry.registry.ItemRegistry;
import exter.foundry.renderer.RendererItemContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class FoundryItems
{
  //Default item id.
  static private int next_id = 9021;

  static public int GetNextID()
  {
    return next_id++;
  }

  
  static public ItemFoundryComponent item_component;
  static public ItemMold item_mold;
  static public ItemIngot item_ingot;
  
  static public ItemRefractoryFluidContainer item_container;
  
  static public Map<String,ItemStack> ingot_stacks = new HashMap<String,ItemStack>();

  static public void RegisterItems(Configuration config)
  {
    int i;

    item_component = new ItemFoundryComponent(config.getItem("component", GetNextID()).getInt() - 256);
    item_mold = new ItemMold(config.getItem("mold", GetNextID()).getInt() - 256);
    item_ingot = new ItemIngot(config.getItem("ingot", GetNextID()).getInt() - 256);
    
    for (i = 0; i < ItemFoundryComponent.NAMES.length; i++)
    {
      ItemStack stack = new ItemStack(item_component,  1, i);
      LanguageRegistry.addName(stack, ItemFoundryComponent.NAMES[i]);
      ItemRegistry.instance.RegisterItem(ItemFoundryComponent.REGISTRY_NAMES[i], stack);
    }
    for (i = 0; i < ItemMold.NAMES.length; i++)
    {
      ItemStack stack = new ItemStack(item_mold,  1, i);
      LanguageRegistry.addName(stack, ItemMold.NAMES[i]);
      ItemRegistry.instance.RegisterItem(ItemMold.REGISTRY_NAMES[i], stack);
    }
    for (i = 0; i < ItemIngot.NAMES.length; i++)
    {
      ItemStack is = new ItemStack(item_ingot,  1, i);
      LanguageRegistry.addName(is, ItemIngot.NAMES[i]);
      OreDictionary.registerOre(ItemIngot.OREDICT_NAMES[i], is);
      ingot_stacks.put(ItemIngot.METAL_NAMES[i], is);
    }
    ingot_stacks.put("Iron", new ItemStack(Item.ingotIron));
    ingot_stacks.put("Gold", new ItemStack(Item.ingotGold));
    
    item_container = new ItemRefractoryFluidContainer(config.getItem("container", GetNextID()).getInt() - 256,FluidContainerRegistry.BUCKET_VOLUME);
    LanguageRegistry.addName(item_container, "Refractory Fluid Container");
    ItemRegistry.instance.RegisterItem("itemRefractoryFluidContainer", item_container.EmptyContainer(1));

  }
}
