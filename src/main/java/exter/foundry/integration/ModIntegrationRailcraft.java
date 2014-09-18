package exter.foundry.integration;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import exter.foundry.util.FoundryMiscUtils;
import exter.foundry.api.FoundryAPI;
import exter.foundry.api.substance.InfuserSubstance;
import exter.foundry.config.FoundryConfig;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.InfuserRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.registry.LiquidMetalRegistry;

public class ModIntegrationRailcraft extends ModIntegration
{
  static public final int ITEM_STEEL_PICKAXE = 0;
  static public final int ITEM_STEEL_AXE = 1;
  static public final int ITEM_STEEL_SHOVEL = 2;
  static public final int ITEM_STEEL_HOE = 3;
  static public final int ITEM_STEEL_SWORD = 4;

  static public final int ITEM_STEEL_HELMET = 5;
  static public final int ITEM_STEEL_CHESTPLATE = 6;
  static public final int ITEM_STEEL_LEGGINGS = 7;
  static public final int ITEM_STEEL_BOOTS = 8;

  static public final int ITEM_IRON_GEAR = 9;
  static public final int ITEM_STEEL_GEAR = 10;
  static public final int ITEM_COAL_COKE = 11;
  static public final int ITEM_COAL_COKE_BLOCK = 12;
  

  public ModIntegrationRailcraft(String mod_name)
  {
    super(mod_name);
  }


  @Override
  public void OnPreInit(Configuration config)
  {

  }


  @Override
  public void OnInit()
  {
    items = new ItemStack[13];

    items[ITEM_STEEL_PICKAXE] = GameRegistry.findItemStack("Railcraft", "tool.steel.pickaxe", 1);
    items[ITEM_STEEL_AXE] = GameRegistry.findItemStack("Railcraft", "tool.steel.axe", 1);
    items[ITEM_STEEL_SHOVEL] = GameRegistry.findItemStack("Railcraft", "tool.steel.shovel", 1);
    items[ITEM_STEEL_HOE] = GameRegistry.findItemStack("Railcraft", "tool.steel.hoe", 1);
    items[ITEM_STEEL_SWORD] = GameRegistry.findItemStack("Railcraft", "tool.steel.sword", 1);

    items[ITEM_STEEL_HELMET] = GameRegistry.findItemStack("Railcraft", "armor.steel.helmet", 1);
    items[ITEM_STEEL_CHESTPLATE] = GameRegistry.findItemStack("Railcraft", "armor.steel.plate", 1);
    items[ITEM_STEEL_LEGGINGS] = GameRegistry.findItemStack("Railcraft", "armor.steel.legs", 1);
    items[ITEM_STEEL_BOOTS] = GameRegistry.findItemStack("Railcraft", "armor.steel.boots", 1);

    items[ITEM_IRON_GEAR] = GameRegistry.findItemStack("Railcraft", "part.gear.iron", 1);
    items[ITEM_STEEL_GEAR] = GameRegistry.findItemStack("Railcraft", "part.gear.steel", 1);
    items[ITEM_COAL_COKE] = GameRegistry.findItemStack("Railcraft", "fuel.coke", 1);
    items[ITEM_COAL_COKE_BLOCK] = GameRegistry.findItemStack("Railcraft", "cube.coke", 1);

    Fluid liquid_steel = LiquidMetalRegistry.instance.GetFluid("Steel");
    Fluid liquid_iron = LiquidMetalRegistry.instance.GetFluid("Iron");
    Fluid liquid_copper = LiquidMetalRegistry.instance.GetFluid("Copper");
    Fluid liquid_tin = LiquidMetalRegistry.instance.GetFluid("Tin");
    Fluid liquid_gold = LiquidMetalRegistry.instance.GetFluid("Gold");

    if(FoundryConfig.recipe_tools_armor)
    {
      ItemStack extra_sticks1 = new ItemStack(Items.stick, 1);
      ItemStack extra_sticks2 = new ItemStack(Items.stick, 2);
      ItemStack mold_chestplate = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_CHESTPLATE);
      ItemStack mold_pickaxe = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_PICKAXE);
      ItemStack mold_axe = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_AXE);
      ItemStack mold_shovel = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_SHOVEL);
      ItemStack mold_hoe = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_HOE);
      ItemStack mold_sword = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_SWORD);
      ItemStack mold_leggings = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_LEGGINGS);
      ItemStack mold_helmet = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_HELMET);
      ItemStack mold_boots = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_BOOTS);

      if(items[ITEM_STEEL_CHESTPLATE] != null)
      {
        CastingRecipeManager.instance.AddRecipe(items[ITEM_STEEL_CHESTPLATE], new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 8), mold_chestplate, null);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CHESTPLATE_SOFT, items[ITEM_STEEL_CHESTPLATE]);
      }
      if(items[ITEM_STEEL_PICKAXE] != null)
      {
        CastingRecipeManager.instance.AddRecipe(items[ITEM_STEEL_PICKAXE], new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_pickaxe, extra_sticks2);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_SOFT, items[ITEM_STEEL_PICKAXE]);
      }
      if(items[ITEM_STEEL_AXE] != null)
      {
        CastingRecipeManager.instance.AddRecipe(items[ITEM_STEEL_AXE], new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_axe, extra_sticks2);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_SOFT, items[ITEM_STEEL_AXE]);
      }

      if(items[ITEM_STEEL_SHOVEL] != null)
      {
        CastingRecipeManager.instance.AddRecipe(items[ITEM_STEEL_SHOVEL], new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 1), mold_shovel, extra_sticks2);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_SOFT, items[ITEM_STEEL_SHOVEL]);
      }
      if(items[ITEM_STEEL_SWORD] != null)
      {
        CastingRecipeManager.instance.AddRecipe(items[ITEM_STEEL_SWORD], new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_sword, extra_sticks1);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_SOFT, items[ITEM_STEEL_SWORD]);
      }

      if(items[ITEM_STEEL_HOE] != null)
      {
        CastingRecipeManager.instance.AddRecipe(items[ITEM_STEEL_HOE], new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_hoe, extra_sticks2);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_SOFT, items[ITEM_STEEL_HOE]);
      }

      if(items[ITEM_STEEL_LEGGINGS] != null)
      {
        CastingRecipeManager.instance.AddRecipe(items[ITEM_STEEL_LEGGINGS], new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 7), mold_leggings, null);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, items[ITEM_STEEL_LEGGINGS]);
      }

      if(items[ITEM_STEEL_HELMET] != null)
      {
        CastingRecipeManager.instance.AddRecipe(items[ITEM_STEEL_HELMET], new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 5), mold_helmet, null);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_SOFT, items[ITEM_STEEL_HELMET]);
      }

      if(items[ITEM_STEEL_BOOTS] != null)
      {
        CastingRecipeManager.instance.AddRecipe(items[ITEM_STEEL_BOOTS], new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_boots, null);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_SOFT, items[ITEM_STEEL_BOOTS]);
      }
    }


    MeltingRecipeManager.instance.AddRecipe("orePoorIron", new FluidStack(liquid_iron,FoundryAPI.FLUID_AMOUNT_NUGGET * 2));
    MeltingRecipeManager.instance.AddRecipe("orePoorCopper", new FluidStack(liquid_copper,FoundryAPI.FLUID_AMOUNT_NUGGET * 2));
    MeltingRecipeManager.instance.AddRecipe("orePoorTin", new FluidStack(liquid_tin,FoundryAPI.FLUID_AMOUNT_NUGGET * 2));
    MeltingRecipeManager.instance.AddRecipe("orePoorGold", new FluidStack(liquid_gold,FoundryAPI.FLUID_AMOUNT_NUGGET * 2));
    
    if(items[ITEM_IRON_GEAR] != null)
    {
      FoundryMiscUtils.RegisterInOreDictionary("gearIron", items[ITEM_IRON_GEAR]);
    }
    if(items[ITEM_STEEL_GEAR] != null)
    {
      FoundryMiscUtils.RegisterInOreDictionary("gearSteel", items[ITEM_STEEL_GEAR]);
    }


    if(!FoundryConfig.recipe_gear_useoredict)
    {
      ItemStack mold_gear = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_GEAR);
      if(items[ITEM_IRON_GEAR] != null)
      {
        MeltingRecipeManager.instance.AddRecipe(items[ITEM_IRON_GEAR], new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 4));
        CastingRecipeManager.instance.AddRecipe(items[ITEM_IRON_GEAR], new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
      }

      if(items[ITEM_STEEL_GEAR] != null)
      {
        MeltingRecipeManager.instance.AddRecipe(items[ITEM_STEEL_GEAR], new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 4));
        CastingRecipeManager.instance.AddRecipe(items[ITEM_STEEL_GEAR], new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
      }
    }
  }


  @Override
  public void OnPostInit()
  {
    items[ITEM_COAL_COKE] = GameRegistry.findItemStack("Railcraft", "fuel.coke", 1);
    items[ITEM_COAL_COKE_BLOCK] = GameRegistry.findItemStack("Railcraft", "cube.coke", 1);

    if(items[ITEM_COAL_COKE] != null)
    {
      InfuserRecipeManager.instance.AddSubstanceRecipe(new InfuserSubstance("carbon", 36), items[ITEM_COAL_COKE], 110000);
    }
    
    if(items[ITEM_COAL_COKE_BLOCK] != null)
    {
      InfuserRecipeManager.instance.AddSubstanceRecipe(new InfuserSubstance("carbon", 324), items[ITEM_COAL_COKE_BLOCK], 880000);
    }
  }
}
