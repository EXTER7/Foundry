package exter.foundry.item;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import exter.foundry.registry.ItemRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class FoundryItems
{
  static public ItemFoundryComponent item_component;
  static public ItemMold item_mold;
  static public ItemIngot item_ingot;
  
  static public ItemRefractoryFluidContainer item_container;
  
  static public Map<String,ItemStack> ingot_stacks = new HashMap<String,ItemStack>();

  static public void RegisterItems(Configuration config)
  {
    int i;

    item_component = new ItemFoundryComponent();
    item_mold = new ItemMold();
    item_ingot = new ItemIngot();
    
    GameRegistry.registerItem(item_component, "foundryComponent");
    GameRegistry.registerItem(item_mold, "foundryMold");
    GameRegistry.registerItem(item_ingot, "foundryIngot");
    
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
    ingot_stacks.put("Iron", new ItemStack(Items.iron_ingot));
    ingot_stacks.put("Gold", new ItemStack(Items.gold_ingot));
    
    item_container = new ItemRefractoryFluidContainer(FluidContainerRegistry.BUCKET_VOLUME);
    GameRegistry.registerItem(item_container, "foundryContainer");
    LanguageRegistry.addName(item_container, "Refractory Fluid Container");
    ItemRegistry.instance.RegisterItem("itemRefractoryFluidContainer", item_container.EmptyContainer(1));

  }
}
