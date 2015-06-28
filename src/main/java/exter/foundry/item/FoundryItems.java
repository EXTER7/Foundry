package exter.foundry.item;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.common.registry.GameRegistry;
import exter.foundry.item.ammo.ItemAmmo;
import exter.foundry.item.ammo.ItemAmmoJacketed;
import exter.foundry.item.ammo.ItemFireAmmo;
import exter.foundry.item.ammo.ItemHollowAmmo;
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
  static public ItemRevolver item_revolver;
  static public ItemAmmo item_ammo;
  static public ItemAmmoJacketed item_ammo_jacketed;
  static public ItemHollowAmmo item_ammo_hollow;
  static public ItemFireAmmo item_ammo_fire;
  
  static public ItemRefractoryFluidContainer item_container;
  
  static public Map<String,ItemStack> ingot_stacks = new HashMap<String,ItemStack>();

  static public void RegisterItems(Configuration config)
  {
    int i;

    item_component = new ItemFoundryComponent();
    item_mold = new ItemMold();
    item_ingot = new ItemIngot();
    item_revolver = new ItemRevolver();
    item_ammo = new ItemAmmo();
    item_ammo_jacketed = new ItemAmmoJacketed();
    item_ammo_hollow = new ItemHollowAmmo();
    item_ammo_fire = new ItemFireAmmo();
    
    GameRegistry.registerItem(item_component, "foundryComponent");
    GameRegistry.registerItem(item_mold, "foundryMold");
    GameRegistry.registerItem(item_ingot, "foundryIngot");
    GameRegistry.registerItem(item_revolver, "foundryRevolver");
    GameRegistry.registerItem(item_ammo, "foundryAmmo");
    GameRegistry.registerItem(item_ammo_jacketed, "foundryAmmoJacketed");
    GameRegistry.registerItem(item_ammo_hollow, "foundryAmmoHollow");
    GameRegistry.registerItem(item_ammo_fire, "foundryAmmoFire");
    
    for (i = 0; i < ItemFoundryComponent.REGISTRY_NAMES.length; i++)
    {
      ItemStack stack = new ItemStack(item_component,  1, i);
      ItemRegistry.instance.RegisterItem(ItemFoundryComponent.REGISTRY_NAMES[i], stack);
    }
    
    for (i = 0; i < ItemMold.REGISTRY_NAMES.length; i++)
    {
      ItemStack stack = new ItemStack(item_mold,  1, i);
      ItemRegistry.instance.RegisterItem(ItemMold.REGISTRY_NAMES[i], stack);
    }
    for (i = 0; i < ItemIngot.METAL_NAMES.length; i++)
    {
      ItemStack is = new ItemStack(item_ingot,  1, i);
      OreDictionary.registerOre(ItemIngot.OREDICT_NAMES[i], is);
      ingot_stacks.put(ItemIngot.METAL_NAMES[i], is);
    }
    ingot_stacks.put("Iron", new ItemStack(Items.iron_ingot));
    ingot_stacks.put("Gold", new ItemStack(Items.gold_ingot));
    
    item_container = new ItemRefractoryFluidContainer(FluidContainerRegistry.BUCKET_VOLUME);
    GameRegistry.registerItem(item_container, "foundryContainer");
    ItemRegistry.instance.RegisterItem("itemRefractoryFluidContainer", item_container.EmptyContainer(1));

  }
}
