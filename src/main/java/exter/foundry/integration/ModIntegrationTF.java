package exter.foundry.integration;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import exter.foundry.api.FoundryAPI;
import exter.foundry.config.FoundryConfig;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.manager.AlloyMixerRecipeManager;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.registry.LiquidMetalRegistry;
import exter.foundry.util.FoundryMiscUtils;

public class ModIntegrationTF extends ModIntegration
{
  
  static private final String[] TOOL_METALS = 
  {
    "Bronze",
    "Copper",
    "Electrum",
    "Invar",
    "Lead",
    "Nickel",
    "Platinum",
    "Silver",
    "Tin"
  };

  static private final String[] GEAR_METALS = 
  {
    "Tin",
    "Platinum",
    "Bronze",
    "Silver",
    "Electrum",
    "Signalum",
    "Iron",
    "Invar",
    "Gold",
    "Lumium",
    "Mithril",
    "Copper",
    "Enderium",
    "Nickel",
    "Lead"
  };

  public boolean gear_recipes;
  public boolean override_redstone_melting;
  
  public ModIntegrationTF(String mod_name)
  {
    super(mod_name);
  }

  @Override
  public void OnPreInit(Configuration config)
  {
    gear_recipes = config.get("integration", Name + ".gears", true).getBoolean(true);
    override_redstone_melting = config.get("integration", Name + ".override_redstone_melting", true).getBoolean(true);
  }

  @Override
  public void OnInit()
  {
  }

  @Override
  public void OnPostInit()
  {
    if(!Loader.isModLoaded("ThermalFoundation"))
    {
      is_loaded = false;
      return;
    }

    ItemStack mithril_ingot = GameRegistry.findItemStack("ThermalFoundation", "ingotMithril", 1);
    ItemStack mithril_block = GameRegistry.findItemStack("ThermalFoundation", "blockMithril", 1);

    ItemStack enderium_ingot = GameRegistry.findItemStack("ThermalFoundation", "ingotEnderium", 1);
    ItemStack enderium_block = GameRegistry.findItemStack("ThermalFoundation", "blockEnderium", 1);

    ItemStack signalum_ingot = GameRegistry.findItemStack("ThermalFoundation", "ingotSignalum", 1);
    ItemStack signalum_block = GameRegistry.findItemStack("ThermalFoundation", "blockSignalum", 1);

    ItemStack lumium_ingot = GameRegistry.findItemStack("ThermalFoundation", "ingotLumium", 1);
    ItemStack lumium_block = GameRegistry.findItemStack("ThermalFoundation", "blockLumium", 1);

    ItemStack pyrotheum = GameRegistry.findItemStack("ThermalFoundation", "dustPyrotheum", 1);
    ItemStack cryotheum = GameRegistry.findItemStack("ThermalFoundation", "dustCryotheum", 1);

  
    if(is_loaded)
    {
      Fluid liquid_copper = LiquidMetalRegistry.instance.GetFluid("Copper");
      Fluid liquid_tin = LiquidMetalRegistry.instance.GetFluid("Tin");
      Fluid liquid_platinum = LiquidMetalRegistry.instance.GetFluid("Platinum");
      Fluid liquid_silver = LiquidMetalRegistry.instance.GetFluid("Silver");
      Fluid liquid_redstone = LiquidMetalRegistry.instance.GetFluid("Redstone");
      Fluid destabilized_redstone = FluidRegistry.getFluid("redstone");
      Fluid liquid_ender = FluidRegistry.getFluid("ender");
      Fluid liquid_glowstone = FluidRegistry.getFluid("glowstone");
      Fluid liquid_coal = FluidRegistry.getFluid("coal");
      Fluid liquid_pyrotheum = FluidRegistry.getFluid("pyrotheum");
      Fluid liquid_cryotheum = FluidRegistry.getFluid("cryotheum");

      Fluid liquid_mithril = LiquidMetalRegistry.instance.GetFluid("Mithril");
      Fluid liquid_enderium = LiquidMetalRegistry.instance.GetFluid("Enderium");
      Fluid liquid_signalum = LiquidMetalRegistry.instance.GetFluid("Signalum");
      Fluid liquid_lumium = LiquidMetalRegistry.instance.GetFluid("Lumium");

      ItemStack mold_ingot = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_INGOT);
      ItemStack mold_block = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_BLOCK);

      MeltingRecipeManager.instance.AddRecipe("dustCoal", new FluidStack(liquid_coal, 100), 1000);

      MeltingRecipeManager.instance.AddRecipe(new ItemStack(Items.ender_pearl), new FluidStack(liquid_ender, 250), 1500);

      MeltingRecipeManager.instance.AddRecipe(new ItemStack(Items.glowstone_dust), new FluidStack(liquid_glowstone, 250), 2500);
      MeltingRecipeManager.instance.AddRecipe(new ItemStack(Blocks.glowstone), new FluidStack(liquid_glowstone, 1000), 2500);

      MeltingRecipeManager.instance.AddRecipe(pyrotheum, new FluidStack(liquid_pyrotheum, 250), 2500);
      MeltingRecipeManager.instance.AddRecipe(cryotheum, new FluidStack(liquid_cryotheum, 100), 320, 25);

      if(override_redstone_melting)
      {
        MeltingRecipeManager.instance.RemoveRecipe(MeltingRecipeManager.instance.FindRecipe(new ItemStack(Items.redstone)));
        MeltingRecipeManager.instance.RemoveRecipe(MeltingRecipeManager.instance.FindRecipe(new ItemStack(Blocks.redstone_block)));
        MeltingRecipeManager.instance.AddRecipe(new ItemStack(Items.redstone), new FluidStack(destabilized_redstone, 100), 1000);
        MeltingRecipeManager.instance.AddRecipe(new ItemStack(Blocks.redstone_block), new FluidStack(destabilized_redstone, 900), 1000);
      }
      
      AlloyMixerRecipeManager.instance.AddRecipe(
          new FluidStack(liquid_enderium, 108),
          new FluidStack[] {
            new FluidStack(liquid_tin, 54),
            new FluidStack(liquid_silver, 27),
            new FluidStack(liquid_platinum, 27),
            new FluidStack(liquid_ender, 250)
            });

      AlloyMixerRecipeManager.instance.AddRecipe(
          new FluidStack(liquid_signalum, 108),
          new FluidStack[] {
            new FluidStack(liquid_copper, 81),
            new FluidStack(liquid_silver, 27),
            new FluidStack(destabilized_redstone, 250)
            });

      AlloyMixerRecipeManager.instance.AddRecipe(
          new FluidStack(liquid_signalum, 4),
          new FluidStack[] {
            new FluidStack(liquid_copper, 3),
            new FluidStack(liquid_silver, 4),
            new FluidStack(liquid_redstone, 4)
            });

      AlloyMixerRecipeManager.instance.AddRecipe(
          new FluidStack(liquid_lumium, 108),
          new FluidStack[] {
            new FluidStack(liquid_tin, 81),
            new FluidStack(liquid_silver, 27),
            new FluidStack(liquid_glowstone, 250)
            });

      CastingRecipeManager.instance.AddRecipe(new ItemStack(Blocks.redstone_block), new FluidStack(destabilized_redstone, 900), mold_block, null);

      CastingRecipeManager.instance.AddRecipe(mithril_ingot, new FluidStack(liquid_mithril, FoundryAPI.FLUID_AMOUNT_INGOT), mold_ingot, null);
      CastingRecipeManager.instance.AddRecipe(mithril_block, new FluidStack(liquid_mithril, FoundryAPI.FLUID_AMOUNT_BLOCK), mold_block, null);

      CastingRecipeManager.instance.AddRecipe(enderium_ingot, new FluidStack(liquid_enderium, FoundryAPI.FLUID_AMOUNT_INGOT), mold_ingot, null);
      CastingRecipeManager.instance.AddRecipe(enderium_block, new FluidStack(liquid_enderium, FoundryAPI.FLUID_AMOUNT_BLOCK), mold_block, null);

      CastingRecipeManager.instance.AddRecipe(signalum_ingot, new FluidStack(liquid_signalum, FoundryAPI.FLUID_AMOUNT_INGOT), mold_ingot, null);
      CastingRecipeManager.instance.AddRecipe(signalum_block, new FluidStack(liquid_signalum, FoundryAPI.FLUID_AMOUNT_BLOCK), mold_block, null);

      CastingRecipeManager.instance.AddRecipe(lumium_ingot, new FluidStack(liquid_lumium, FoundryAPI.FLUID_AMOUNT_INGOT), mold_ingot, null);
      CastingRecipeManager.instance.AddRecipe(lumium_block, new FluidStack(liquid_lumium, FoundryAPI.FLUID_AMOUNT_BLOCK), mold_block, null);


      if(FoundryConfig.recipe_tools_armor)
      {
        ItemStack extra_sticks1 = new ItemStack(Items.stick, 1);
        ItemStack extra_sticks2 = new ItemStack(Items.stick, 2);
        for(String metal_name:TOOL_METALS)
        {
          ItemStack pickaxe = GameRegistry.findItemStack("ThermalFoundation", "tool" + metal_name + "Pickaxe", 1);
          ItemStack axe = GameRegistry.findItemStack("ThermalFoundation", "tool" + metal_name + "Axe", 1);
          ItemStack shovel = GameRegistry.findItemStack("ThermalFoundation", "tool" + metal_name + "Shovel", 1);
          ItemStack hoe = GameRegistry.findItemStack("ThermalFoundation", "tool" + metal_name + "Hoe", 1);
          ItemStack sword = GameRegistry.findItemStack("ThermalFoundation", "tool" + metal_name + "Sword", 1);

          ItemStack helmet = GameRegistry.findItemStack("ThermalFoundation", "armor" + metal_name + "rHelmet", 1);
          ItemStack chestplate = GameRegistry.findItemStack("ThermalFoundation", "armor" + metal_name + "Plate", 1);
          ItemStack leggings = GameRegistry.findItemStack("ThermalFoundation", "armor" + metal_name + "Legs", 1);
          ItemStack boots = GameRegistry.findItemStack("ThermalFoundation", "armor" + metal_name + "Boots", 1);
          
          Fluid metal = LiquidMetalRegistry.instance.GetFluid(metal_name);
          RegisterCasting(pickaxe, metal, 3, ItemMold.MOLD_PICKAXE, extra_sticks2);
          RegisterCasting(axe, metal, 3, ItemMold.MOLD_AXE, extra_sticks2);
          RegisterCasting(shovel, metal, 1, ItemMold.MOLD_SHOVEL, extra_sticks2);
          RegisterCasting(sword, metal, 2, ItemMold.MOLD_SWORD, extra_sticks1);
          RegisterCasting(hoe, metal, 2, ItemMold.MOLD_HOE, extra_sticks2);
          RegisterCasting(leggings, metal, 7, ItemMold.MOLD_LEGGINGS, null);
          RegisterCasting(chestplate, metal, 8, ItemMold.MOLD_CHESTPLATE, null);
          RegisterCasting(helmet, metal, 5, ItemMold.MOLD_HELMET, null);
          RegisterCasting(boots, metal, 4, ItemMold.MOLD_BOOTS, null);

          FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CHESTPLATE_SOFT, chestplate);
          FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, leggings);
          FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_SOFT, helmet);
          FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_SOFT, boots);
          FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_SOFT, pickaxe);
          FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_SOFT, axe);
          FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_SOFT, shovel);
          FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_SOFT, hoe);
          FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_SOFT, sword);          
        }
      }
      
      if(!FoundryConfig.recipe_gear_useoredict && gear_recipes)
      {
        ItemStack mold_gear = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_GEAR);
        for(String metal_name:GEAR_METALS)
        {
          ItemStack gear = GameRegistry.findItemStack("ThermalFoundation", "gear" + metal_name, 1);
          if(gear != null)
          {
            Fluid metal = LiquidMetalRegistry.instance.GetFluid(metal_name);
            MeltingRecipeManager.instance.AddRecipe(gear, new FluidStack(metal, FoundryAPI.FLUID_AMOUNT_INGOT * 4));
            CastingRecipeManager.instance.AddRecipe(gear, new FluidStack(metal, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
          }
        }
      }
    }
  }
}
