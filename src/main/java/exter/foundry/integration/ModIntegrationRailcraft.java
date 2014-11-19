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

  public boolean gear_recipes;

  public ModIntegrationRailcraft(String mod_name)
  {
    super(mod_name);
  }


  @Override
  public void OnPreInit(Configuration config)
  {
    gear_recipes = config.get("integration", Name + ".gears", true).getBoolean(true);
  }


  @Override
  public void OnInit()
  {
    ItemStack steel_pickaxe = GameRegistry.findItemStack("Railcraft", "tool.steel.pickaxe", 1);
    ItemStack steel_axe = GameRegistry.findItemStack("Railcraft", "tool.steel.axe", 1);
    ItemStack steel_shovel = GameRegistry.findItemStack("Railcraft", "tool.steel.shovel", 1);
    ItemStack steel_hoe = GameRegistry.findItemStack("Railcraft", "tool.steel.hoe", 1);
    ItemStack steel_sword = GameRegistry.findItemStack("Railcraft", "tool.steel.sword", 1);

    ItemStack steel_helmet = GameRegistry.findItemStack("Railcraft", "armor.steel.helmet", 1);
    ItemStack steel_chestplate = GameRegistry.findItemStack("Railcraft", "armor.steel.plate", 1);
    ItemStack steel_leggings = GameRegistry.findItemStack("Railcraft", "armor.steel.legs", 1);
    ItemStack steel_boots = GameRegistry.findItemStack("Railcraft", "armor.steel.boots", 1);

    ItemStack iron_gear = GameRegistry.findItemStack("Railcraft", "part.gear.iron", 1);
    ItemStack steel_gear = GameRegistry.findItemStack("Railcraft", "part.gear.steel", 1);



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

      if(steel_chestplate != null)
      {
        CastingRecipeManager.instance.AddRecipe(steel_chestplate, new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 8), mold_chestplate, null);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CHESTPLATE_SOFT, steel_chestplate);
      }
      if(steel_pickaxe != null)
      {
        CastingRecipeManager.instance.AddRecipe(steel_pickaxe, new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_pickaxe, extra_sticks2);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_SOFT, steel_pickaxe);
      }
      if(steel_axe != null)
      {
        CastingRecipeManager.instance.AddRecipe(steel_axe, new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_axe, extra_sticks2);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_SOFT, steel_axe);
      }

      if(steel_shovel != null)
      {
        CastingRecipeManager.instance.AddRecipe(steel_shovel, new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 1), mold_shovel, extra_sticks2);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_SOFT, steel_shovel);
      }
      if(steel_sword != null)
      {
        CastingRecipeManager.instance.AddRecipe(steel_sword, new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_sword, extra_sticks1);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_SOFT, steel_sword);
      }

      if(steel_hoe != null)
      {
        CastingRecipeManager.instance.AddRecipe(steel_hoe, new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_hoe, extra_sticks2);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_SOFT, steel_hoe);
      }

      if(steel_leggings != null)
      {
        CastingRecipeManager.instance.AddRecipe(steel_leggings, new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 7), mold_leggings, null);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, steel_leggings);
      }

      if(steel_helmet != null)
      {
        CastingRecipeManager.instance.AddRecipe(steel_helmet, new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 5), mold_helmet, null);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_SOFT, steel_helmet);
      }

      if(steel_boots != null)
      {
        CastingRecipeManager.instance.AddRecipe(steel_boots, new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_boots, null);
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_SOFT, steel_boots);
      }
    }


    MeltingRecipeManager.instance.AddRecipe("orePoorIron", new FluidStack(liquid_iron,FoundryAPI.FLUID_AMOUNT_NUGGET * 2));
    MeltingRecipeManager.instance.AddRecipe("orePoorCopper", new FluidStack(liquid_copper,FoundryAPI.FLUID_AMOUNT_NUGGET * 2));
    MeltingRecipeManager.instance.AddRecipe("orePoorTin", new FluidStack(liquid_tin,FoundryAPI.FLUID_AMOUNT_NUGGET * 2));
    MeltingRecipeManager.instance.AddRecipe("orePoorGold", new FluidStack(liquid_gold,FoundryAPI.FLUID_AMOUNT_NUGGET * 2));
    
    if(iron_gear != null)
    {
      FoundryMiscUtils.RegisterInOreDictionary("gearIron", iron_gear);
    }
    if(steel_gear != null)
    {
      FoundryMiscUtils.RegisterInOreDictionary("gearSteel", steel_gear);
    }


    if(!FoundryConfig.recipe_gear_useoredict && gear_recipes)
    {
      ItemStack mold_gear = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_GEAR);
      if(iron_gear != null)
      {
        MeltingRecipeManager.instance.AddRecipe(iron_gear, new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 4));
        CastingRecipeManager.instance.AddRecipe(iron_gear, new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
      }

      if(steel_gear != null)
      {
        MeltingRecipeManager.instance.AddRecipe(steel_gear, new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 4));
        CastingRecipeManager.instance.AddRecipe(steel_gear, new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
      }
    }
  }


  @Override
  public void OnPostInit()
  {
    ItemStack coal_coke = GameRegistry.findItemStack("Railcraft", "fuel.coke", 1);
    ItemStack coal_coke_block = GameRegistry.findItemStack("Railcraft", "cube.coke", 1);

    if(coal_coke != null)
    {
      InfuserRecipeManager.instance.AddSubstanceRecipe(new InfuserSubstance("carbon", 36), coal_coke, 110000);
    }
    
    if(coal_coke_block != null)
    {
      InfuserRecipeManager.instance.AddSubstanceRecipe(new InfuserSubstance("carbon", 324), coal_coke_block, 880000);
    }
  }
}
