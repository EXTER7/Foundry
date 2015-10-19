package exter.foundry.recipes;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import exter.foundry.api.FoundryAPI;
import exter.foundry.api.FoundryUtils;
import exter.foundry.api.orestack.OreStack;
import exter.foundry.api.recipe.IMeltingRecipe;
import exter.foundry.api.substance.InfuserSubstance;
import exter.foundry.block.BlockFoundryOre;
import exter.foundry.block.FoundryBlocks;
import exter.foundry.block.BlockFoundryMachine.EnumMachine;
import exter.foundry.config.FoundryConfig;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemComponent;
import exter.foundry.item.ItemIngot;
import exter.foundry.item.ItemMold;
import exter.foundry.material.MaterialRegistry;
import exter.foundry.material.OreDictMaterial;
import exter.foundry.material.OreDictType;
import exter.foundry.recipes.manager.AlloyFurnaceRecipeManager;
import exter.foundry.recipes.manager.AlloyMixerRecipeManager;
import exter.foundry.recipes.manager.AtomizerRecipeManager;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.InfuserRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.registry.LiquidMetalRegistry;
import exter.foundry.util.FoundryMiscUtils;

public class FoundryRecipes
{
  static public Fluid liquid_iron;
  static public Fluid liquid_gold;
  static public Fluid liquid_copper;
  static public Fluid liquid_tin;
  static public Fluid liquid_bronze;
  static public Fluid liquid_electrum;
  static public Fluid liquid_invar;
  static public Fluid liquid_nickel;
  static public Fluid liquid_zinc;
  static public Fluid liquid_brass;
  static public Fluid liquid_silver;
  static public Fluid liquid_steel;
  static public Fluid liquid_cupronickel;
  static public Fluid liquid_lead;
  static public Fluid liquid_rubber;

  static public void PreInit()
  {
    liquid_iron = LiquidMetalRegistry.instance.registerLiquidMetal( "Iron", 1850, 15);
    liquid_gold = LiquidMetalRegistry.instance.registerLiquidMetal( "Gold", 1350, 15);
    liquid_copper = LiquidMetalRegistry.instance.registerLiquidMetal( "Copper", 1400, 15);
    liquid_tin = LiquidMetalRegistry.instance.registerLiquidMetal( "Tin", 550, 7);
    liquid_bronze = LiquidMetalRegistry.instance.registerLiquidMetal( "Bronze", 1400, 15);
    liquid_electrum = LiquidMetalRegistry.instance.registerLiquidMetal( "Electrum", 1350, 15);
    liquid_invar = LiquidMetalRegistry.instance.registerLiquidMetal( "Invar", 1850, 15);
    liquid_nickel = LiquidMetalRegistry.instance.registerLiquidMetal( "Nickel", 1750, 15);
    liquid_zinc = LiquidMetalRegistry.instance.registerLiquidMetal( "Zinc", 700, 15);
    liquid_brass = LiquidMetalRegistry.instance.registerLiquidMetal( "Brass", 1400, 15);
    liquid_silver = LiquidMetalRegistry.instance.registerLiquidMetal( "Silver", 1250, 15);
    liquid_steel = LiquidMetalRegistry.instance.registerLiquidMetal( "Steel", 1850, 15);
    liquid_cupronickel = LiquidMetalRegistry.instance.registerLiquidMetal( "Cupronickel", 1750, 15);
    liquid_lead = LiquidMetalRegistry.instance.registerLiquidMetal( "Lead", 650, 1);  

    LiquidMetalRegistry.instance.registerLiquidMetal( "Aluminum", 1100, 15);  
    LiquidMetalRegistry.instance.registerLiquidMetal( "Chromium", 2200, 8);   
    LiquidMetalRegistry.instance.registerLiquidMetal( "Platinum", 2050, 15);  
    LiquidMetalRegistry.instance.registerLiquidMetal( "Manganese", 1550, 15);   
    LiquidMetalRegistry.instance.registerLiquidMetal( "Titanium", 2000, 15);
    LiquidMetalRegistry.instance.registerLiquidMetal( "Rubber", 460, 0);
    LiquidMetalRegistry.instance.registerLiquidMetal( "StainlessSteel", 1900, 15);
    LiquidMetalRegistry.instance.registerLiquidMetal( "Kanthal", 1900, 15);
    LiquidMetalRegistry.instance.registerLiquidMetal( "Nichrome", 1950, 15);
    LiquidMetalRegistry.instance.registerLiquidMetal( "Enderium", 1900, 12);
    LiquidMetalRegistry.instance.registerLiquidMetal( "Mithril", 1950, 12);
    LiquidMetalRegistry.instance.registerLiquidMetal( "Signalum", 1400, 12);
    LiquidMetalRegistry.instance.registerLiquidMetal( "Lumium", 2500, 15);
    LiquidMetalRegistry.instance.registerLiquidMetal( "ElectrumFlux", 1500, 14);
    LiquidMetalRegistry.instance.registerLiquidMetal( "Redstone", 1000, 8);
    LiquidMetalRegistry.instance.registerLiquidMetal( "RedAlloy", 1350, 10);    
    LiquidMetalRegistry.instance.registerLiquidMetal( "Adamantine", 2000, 15);
    LiquidMetalRegistry.instance.registerLiquidMetal( "Atlarus", 2000, 15);
    LiquidMetalRegistry.instance.registerLiquidMetal( "Rubracium", 2000, 15);
    LiquidMetalRegistry.instance.registerLiquidMetal( "Haderoth", 2000, 15);
    LiquidMetalRegistry.instance.registerLiquidMetal( "Tartarite", 2000, 15);
    LiquidMetalRegistry.instance.registerLiquidMetal( "Midasium", 1900, 15);
    LiquidMetalRegistry.instance.registerLiquidMetal( "DamascusSteel", 1850, 13);
    LiquidMetalRegistry.instance.registerLiquidMetal( "Angmallen", 1850, 15);
    LiquidMetalRegistry.instance.registerLiquidMetal( "Quicksilver", 2050, 15);
    LiquidMetalRegistry.instance.registerLiquidMetal( "Orichalcum", 2000, 10);
    LiquidMetalRegistry.instance.registerLiquidMetal( "Celenegil", 2050, 15);
    LiquidMetalRegistry.instance.registerLiquidMetal( "Vyroxeres", 1900, 15);
    LiquidMetalRegistry.instance.registerLiquidMetal( "Sanguinite", 1900, 15);
    LiquidMetalRegistry.instance.registerLiquidMetal( "Carmot", 1900, 15);
    LiquidMetalRegistry.instance.registerLiquidMetal( "Infuscolium", 1900, 15);
    LiquidMetalRegistry.instance.registerLiquidMetal( "Meutoite", 2200, 15);
    LiquidMetalRegistry.instance.registerLiquidMetal( "Hepatizon", 2000, 15);
    LiquidMetalRegistry.instance.registerLiquidMetal( "Eximite", 2200, 15);
    LiquidMetalRegistry.instance.registerLiquidMetal( "Desichalkos", 2200, 15);
    LiquidMetalRegistry.instance.registerLiquidMetal( "DeepIron", 1900, 14);
    LiquidMetalRegistry.instance.registerLiquidMetal( "Ceruclase", 2000, 15);
    LiquidMetalRegistry.instance.registerLiquidMetal( "BlackSteel", 1900, 14);
    LiquidMetalRegistry.instance.registerLiquidMetal( "AstralSilver", 1500, 15);
    LiquidMetalRegistry.instance.registerLiquidMetal( "Amordrine", 2000, 15);
    LiquidMetalRegistry.instance.registerLiquidMetal( "Alduorite", 2000, 15);
    LiquidMetalRegistry.instance.registerLiquidMetal( "Kalendrite", 2000, 15);
    LiquidMetalRegistry.instance.registerLiquidMetal( "Lemurite", 2000, 15);
    LiquidMetalRegistry.instance.registerLiquidMetal( "Inolashite", 2000, 15);
    LiquidMetalRegistry.instance.registerLiquidMetal( "ShadowIron", 2000, 12);
    LiquidMetalRegistry.instance.registerLiquidMetal( "ShadowSteel", 2000, 12);
    LiquidMetalRegistry.instance.registerLiquidMetal( "Oureclase", 1900, 14);
    LiquidMetalRegistry.instance.registerLiquidMetal( "Ignatius", 2100, 15);
    LiquidMetalRegistry.instance.registerLiquidMetal( "Vulcanite", 2100, 15);
    LiquidMetalRegistry.instance.registerLiquidMetal( "Prometheum", 1900, 14);
    
    for(String name:LiquidMetalRegistry.instance.getFluidNames())
    {
      FoundryUtils.registerBasicMeltingRecipes(name,LiquidMetalRegistry.instance.getFluid(name));
    }
    FoundryUtils.registerBasicMeltingRecipes("Chrome",LiquidMetalRegistry.instance.getFluid("Chromium"));
    FoundryUtils.registerBasicMeltingRecipes("Aluminium",LiquidMetalRegistry.instance.getFluid("Aluminum"));


    
    AlloyFurnaceRecipeManager.instance.addRecipe(
        new ItemStack(FoundryItems.item_ingot, 4, ItemIngot.INGOT_BRONZE),
        new Object[] {
            new OreStack("ingotCopper", 3),
            new OreStack("dustCopper", 3) },
        new Object[] {
            new OreStack("ingotTin", 1),
            new OreStack("dustTin", 1) }
        );
    
    AlloyFurnaceRecipeManager.instance.addRecipe(
        new ItemStack(FoundryItems.item_ingot, 4, ItemIngot.INGOT_BRASS),
        new Object[] {
            new OreStack("ingotCopper", 3),
            new OreStack("dustCopper", 3) },
        new Object[] {
            new OreStack("ingotZinc", 1),
            new OreStack("dustZinc", 1) }
        );
    
    AlloyFurnaceRecipeManager.instance.addRecipe(
        new ItemStack(FoundryItems.item_ingot, 3, ItemIngot.INGOT_INVAR),
        new Object[] {
            new OreStack("ingotIron", 2),
            new OreStack("dustIron", 2) },
        new Object[] {
            new OreStack("ingotNickel", 1),
            new OreStack("dustNickel", 1) }
        );

    AlloyFurnaceRecipeManager.instance.addRecipe(
        new ItemStack(FoundryItems.item_ingot, 2, ItemIngot.INGOT_ELECTRUM),
        new Object[] {
            new OreStack("ingotGold", 1),
            new OreStack("dustGold", 1) },
        new Object[] {
            new OreStack("ingotSilver", 1),
            new OreStack("dustSilver", 1) }
        );
    
    AlloyFurnaceRecipeManager.instance.addRecipe(
        new ItemStack(FoundryItems.item_ingot, 2, ItemIngot.INGOT_CUPRONICKEL),
        new Object[] {
            new OreStack("ingotCopper", 1),
            new OreStack("dustCopper", 1) },
        new Object[] {
            new OreStack("ingotNickel", 1),
            new OreStack("dustNickel", 1) }
        );
    

    AlloyMixerRecipeManager.instance.addRecipe(
        new FluidStack(liquid_bronze, 4),
        new FluidStack[] {
          new FluidStack(liquid_copper, 3),
          new FluidStack(liquid_tin, 1)
          });
    
    AlloyMixerRecipeManager.instance.addRecipe(
        new FluidStack(liquid_brass, 4),
        new FluidStack[] {
          new FluidStack(liquid_copper, 3),
          new FluidStack(liquid_zinc, 1)
        });
    
    AlloyMixerRecipeManager.instance.addRecipe(
        new FluidStack(liquid_invar, 3),
        new FluidStack[] {
          new FluidStack(liquid_iron, 2),
          new FluidStack(liquid_nickel, 1)
        });

    AlloyMixerRecipeManager.instance.addRecipe(
        new FluidStack(liquid_electrum, 2),
        new FluidStack[] {
          new FluidStack(liquid_gold, 1),
          new FluidStack(liquid_silver, 1)
        });

    AlloyMixerRecipeManager.instance.addRecipe(
        new FluidStack(liquid_cupronickel, 2),
        new FluidStack[] {
          new FluidStack(liquid_copper, 1),
          new FluidStack(liquid_nickel, 1)
        });

    ItemStack mold_ingot = FoundryItems.mold(ItemMold.MOLD_INGOT);
    ItemStack mold_cable_ic2 = FoundryItems.mold(ItemMold.MOLD_CABLE_IC2);
    ItemStack mold_insulated_cable_ic2 = FoundryItems.mold(ItemMold.MOLD_INSULATED_CABLE_IC2);
    ItemStack mold_casing_ic2 = FoundryItems.mold(ItemMold.MOLD_CASING_IC2);
    ItemStack mold_slab = FoundryItems.mold(ItemMold.MOLD_SLAB);
    ItemStack mold_stairs = FoundryItems.mold(ItemMold.MOLD_STAIRS);
    ItemStack mold_plate_ic2 = FoundryItems.mold(ItemMold.MOLD_PLATE);
    ItemStack mold_wire_pr = FoundryItems.mold(ItemMold.MOLD_WIRE_PR);
    ItemStack mold_block = FoundryItems.mold(ItemMold.MOLD_BLOCK);
    ItemStack mold_gear = FoundryItems.mold(ItemMold.MOLD_GEAR);
    ItemStack extra_sticks1 = new ItemStack(Items.stick,1);
    ItemStack extra_sticks2 = new ItemStack(Items.stick,2);

    ItemStack mold_bullet = FoundryItems.mold(ItemMold.MOLD_BULLET);
    ItemStack mold_bullet_hollow = FoundryItems.mold(ItemMold.MOLD_BULLET_HOLLOW);
    ItemStack mold_bullet_casing = FoundryItems.mold(ItemMold.MOLD_BULLET_CASING);
    ItemStack mold_pellet = FoundryItems.mold(ItemMold.MOLD_PELLET);
    ItemStack mold_shell_casing = FoundryItems.mold(ItemMold.MOLD_SHELL_CASING);
    ItemStack mold_gun_barrel = FoundryItems.mold(ItemMold.MOLD_GUN_BARREL);
    ItemStack mold_revolver_drum = FoundryItems.mold(ItemMold.MOLD_REVOLVER_DRUM);
    ItemStack mold_revolver_frame = FoundryItems.mold(ItemMold.MOLD_REVOLVER_FRAME);
    ItemStack mold_shotgun_pump = FoundryItems.mold(ItemMold.MOLD_SHOTGUN_PUMP);
    ItemStack mold_shotgun_frame = FoundryItems.mold(ItemMold.MOLD_SHOTGUN_FRAME);

    
    CastingRecipeManager.instance.addMold(mold_ingot);
    CastingRecipeManager.instance.addMold(mold_cable_ic2);
    CastingRecipeManager.instance.addMold(mold_insulated_cable_ic2);
    CastingRecipeManager.instance.addMold(mold_casing_ic2);
    CastingRecipeManager.instance.addMold(mold_slab);
    CastingRecipeManager.instance.addMold(mold_stairs);
    CastingRecipeManager.instance.addMold(mold_plate_ic2);
    CastingRecipeManager.instance.addMold(mold_wire_pr);
    CastingRecipeManager.instance.addMold(mold_block);
    CastingRecipeManager.instance.addMold(mold_gear);
    CastingRecipeManager.instance.addMold(mold_bullet);
    CastingRecipeManager.instance.addMold(mold_bullet_hollow);
    CastingRecipeManager.instance.addMold(mold_bullet_casing);
    CastingRecipeManager.instance.addMold(mold_gun_barrel);
    CastingRecipeManager.instance.addMold(mold_revolver_drum);
    CastingRecipeManager.instance.addMold(mold_revolver_frame);
    CastingRecipeManager.instance.addMold(mold_pellet);
    CastingRecipeManager.instance.addMold(mold_shell_casing);
    CastingRecipeManager.instance.addMold(mold_shotgun_pump);
    CastingRecipeManager.instance.addMold(mold_shotgun_frame);

    if(FoundryConfig.recipe_tools_armor)
    {
      ItemStack mold_chestplate = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_CHESTPLATE);
      ItemStack mold_pickaxe = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_PICKAXE);
      ItemStack mold_axe = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_AXE);
      ItemStack mold_shovel = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_SHOVEL);
      ItemStack mold_hoe = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_HOE);
      ItemStack mold_sword = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_SWORD);
      ItemStack mold_leggings = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_LEGGINGS);
      ItemStack mold_helmet = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_HELMET);
      ItemStack mold_boots = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_BOOTS);

      CastingRecipeManager.instance.addMold(mold_chestplate);
      CastingRecipeManager.instance.addMold(mold_pickaxe);
      CastingRecipeManager.instance.addMold(mold_axe);
      CastingRecipeManager.instance.addMold(mold_shovel);
      CastingRecipeManager.instance.addMold(mold_hoe);
      CastingRecipeManager.instance.addMold(mold_sword);
      CastingRecipeManager.instance.addMold(mold_leggings);
      CastingRecipeManager.instance.addMold(mold_helmet);
      CastingRecipeManager.instance.addMold(mold_boots);

      CastingRecipeManager.instance.addRecipe(new ItemStack(Items.iron_chestplate, 1, 0), new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 8), mold_chestplate, null);
      CastingRecipeManager.instance.addRecipe(new ItemStack(Items.golden_chestplate, 1, 0), new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 8), mold_chestplate, null);

      CastingRecipeManager.instance.addRecipe(new ItemStack(Items.iron_pickaxe, 1, 0), new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_pickaxe, extra_sticks2);
      CastingRecipeManager.instance.addRecipe(new ItemStack(Items.golden_pickaxe, 1, 0), new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_pickaxe, extra_sticks2);

      CastingRecipeManager.instance.addRecipe(new ItemStack(Items.iron_axe, 1, 0), new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_axe, extra_sticks2);
      CastingRecipeManager.instance.addRecipe(new ItemStack(Items.golden_axe, 1, 0), new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_axe, extra_sticks2);

      CastingRecipeManager.instance.addRecipe(new ItemStack(Items.iron_shovel, 1, 0), new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 1), mold_shovel, extra_sticks2);
      CastingRecipeManager.instance.addRecipe(new ItemStack(Items.golden_shovel, 1, 0), new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 1), mold_shovel, extra_sticks2);

      CastingRecipeManager.instance.addRecipe(new ItemStack(Items.iron_sword, 1, 0), new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_sword, extra_sticks1);
      CastingRecipeManager.instance.addRecipe(new ItemStack(Items.golden_sword, 1, 0), new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_sword, extra_sticks1);

      CastingRecipeManager.instance.addRecipe(new ItemStack(Items.iron_hoe, 1, 0), new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_hoe, extra_sticks2);
      CastingRecipeManager.instance.addRecipe(new ItemStack(Items.golden_hoe, 1, 0), new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_hoe, extra_sticks2);

      CastingRecipeManager.instance.addRecipe(new ItemStack(Items.iron_leggings, 1, 0), new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 7), mold_leggings, null);
      CastingRecipeManager.instance.addRecipe(new ItemStack(Items.golden_leggings, 1, 0), new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 7), mold_leggings, null);

      CastingRecipeManager.instance.addRecipe(new ItemStack(Items.iron_helmet, 1, 0), new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 5), mold_helmet, null);
      CastingRecipeManager.instance.addRecipe(new ItemStack(Items.golden_helmet, 1, 0), new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 5), mold_helmet, null);

      CastingRecipeManager.instance.addRecipe(new ItemStack(Items.iron_boots, 1, 0), new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_boots, null);
      CastingRecipeManager.instance.addRecipe(new ItemStack(Items.golden_boots, 1, 0), new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_boots, null);

    }
    
    //Ingot casting recipes.
    for(Entry<String,ItemStack> entry:FoundryItems.ingot_stacks.entrySet())
    {
      CastingRecipeManager.instance.addRecipe(
          entry.getValue(),
          new FluidStack(
              LiquidMetalRegistry.instance.getFluid(entry.getKey()),
              FoundryAPI.FLUID_AMOUNT_INGOT),
          mold_ingot, null);
    }
    
    
    //Metal block casting recipes.
    for(Entry<String,ItemStack> entry:FoundryBlocks.block_stacks.entrySet())
    {
      Fluid fluid = LiquidMetalRegistry.instance.getFluid(entry.getKey());
      if(fluid != null)
      {
        CastingRecipeManager.instance.addRecipe(
            entry.getValue(),
            new FluidStack(
                fluid,
                FoundryAPI.FLUID_AMOUNT_BLOCK),
            mold_block, null);
      }
    }
    
    //Metal slab casting recipes
    for(Entry<String,ItemStack> entry:FoundryBlocks.slab_stacks.entrySet())
    {
      ItemStack stack = entry.getValue();
      FluidStack fluid = new FluidStack(
          LiquidMetalRegistry.instance.getFluid(entry.getKey()),
          FoundryAPI.FLUID_AMOUNT_BLOCK / 2);

      CastingRecipeManager.instance.addRecipe(stack, fluid, mold_slab, null);
      MeltingRecipeManager.instance.addRecipe(stack, fluid);
    }

    //Metal stairs casting recipes
    for(Map.Entry<String, BlockStairs> e:FoundryBlocks.block_metal_stairs.entrySet())
    {
        ItemStack stack = new ItemStack(e.getValue());
        FluidStack fluid = new FluidStack(
            LiquidMetalRegistry.instance.getFluid(e.getKey()),
            FoundryAPI.FLUID_AMOUNT_BLOCK * 3 / 4);
        
        CastingRecipeManager.instance.addRecipe(stack, fluid, mold_stairs, null);
        MeltingRecipeManager.instance.addRecipe(stack, fluid);
    }
    
    if(FoundryConfig.recipe_steel_enable)
    {
      InfuserRecipeManager.instance.addSubstanceRecipe(new InfuserSubstance("carbon",36), new ItemStack(Items.coal,1,0), 240000);
      InfuserRecipeManager.instance.addSubstanceRecipe(new InfuserSubstance("carbon",12), new ItemStack(Items.coal,1,1), 480000);
      InfuserRecipeManager.instance.addSubstanceRecipe(new InfuserSubstance("carbon",324), new ItemStack(Blocks.coal_block,1), 1920000);
      InfuserRecipeManager.instance.addSubstanceRecipe(new InfuserSubstance("carbon",36), "dustCoal", 160000);
      InfuserRecipeManager.instance.addSubstanceRecipe(new InfuserSubstance("carbon",12), "dustCharcoal", 320000);

      InfuserRecipeManager.instance.addRecipe(new FluidStack(liquid_steel,3), new FluidStack(liquid_iron,3), new InfuserSubstance("carbon", 2));
    }
    
    if(FoundryConfig.recipe_gear_useoredict)
    {
      for(String name:LiquidMetalRegistry.instance.getFluidNames())
      {
        Fluid fluid = LiquidMetalRegistry.instance.getFluid(name);
        MeltingRecipeManager.instance.addRecipe("gear" + name, new FluidStack(fluid,FoundryAPI.FLUID_AMOUNT_INGOT * 4));
        CastingRecipeManager.instance.addRecipe("gear" + name, new FluidStack(fluid, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
      }
    }
           
    if(FoundryConfig.recipe_glass)
    {
      final String[] oredict_names = { "dyeBlack", "dyeRed", "dyeGreen", "dyeBrown", "dyeBlue", "dyePurple", "dyeCyan", "dyeLightGray", "dyeGray", "dyePink", "dyeLime", "dyeYellow", "dyeLightBlue", "dyeMagenta", "dyeOrange", "dyeWhite" };

      int temp = 1550;
      Fluid liquid_glass = LiquidMetalRegistry.instance.registerLiquidMetal("Glass", temp, 12);
      MeltingRecipeManager.instance.addRecipe(new ItemStack(Blocks.sand), new FluidStack(liquid_glass,1000),temp,250);
      MeltingRecipeManager.instance.addRecipe(new ItemStack(Blocks.glass), new FluidStack(liquid_glass,1000),temp,250);
      MeltingRecipeManager.instance.addRecipe(new ItemStack(Blocks.glass_pane), new FluidStack(liquid_glass,375),temp,250);
      CastingRecipeManager.instance.addRecipe(new ItemStack(Blocks.glass), new FluidStack(liquid_glass,1000),mold_block,null,400);
      
      for(EnumDyeColor dye:EnumDyeColor.values())
      {
        String name = dye.getName();
        int color = ItemDye.dyeColors[dye.getDyeDamage()];
        int c1 = 63 + (color & 0xFF) * 3 / 4;
        int c2 = 63 + ((color >> 8 ) & 0xFF) * 3 / 4;
        int c3 = 63 + ((color >> 16) & 0xFF) * 3 / 4;
        int fluid_color = c1 | (c2 << 8) | (c3 << 16);
        
        Fluid liquid_glass_colored = LiquidMetalRegistry.instance.registerLiquidMetal("Glass." + name, temp, 12,"liquidGlass",fluid_color);

        int meta = dye.getMetadata();
        MeltingRecipeManager.instance.addRecipe(new ItemStack(Blocks.stained_glass,1,meta), new FluidStack(liquid_glass_colored,1000),temp,250);
        MeltingRecipeManager.instance.addRecipe(new ItemStack(Blocks.stained_glass_pane,1,meta), new FluidStack(liquid_glass_colored,375),temp,250);
        CastingRecipeManager.instance.addRecipe(new ItemStack(Blocks.stained_glass,1,meta), new FluidStack(liquid_glass_colored,1000),mold_block,null,400);
        
        InfuserRecipeManager.instance.addSubstanceRecipe(new InfuserSubstance("dye." + name,200), oredict_names[dye.getDyeDamage()], 25000);
        InfuserRecipeManager.instance.addRecipe(new FluidStack(liquid_glass_colored,40),new FluidStack(liquid_glass,40),new InfuserSubstance("dye." + name,1));
      }
    }
    
    ItemStack bullet = FoundryItems.component(ItemComponent.COMPONENT_AMMO_BULLET);
    ItemStack bullet_hollow = FoundryItems.component(ItemComponent.COMPONENT_AMMO_BULLET_HOLLOW);
    ItemStack bullet_jacketed = FoundryItems.component(ItemComponent.COMPONENT_AMMO_BULLET_JACKETED);
    ItemStack bullet_casing = FoundryItems.component(ItemComponent.COMPONENT_AMMO_CASING);
    ItemStack pellet = FoundryItems.component(ItemComponent.COMPONENT_AMMO_PELLET);
    ItemStack shell_casing = FoundryItems.component(ItemComponent.COMPONENT_AMMO_CASING_SHELL);
    ItemStack gun_barrel = FoundryItems.component(ItemComponent.COMPONENT_GUN_BARREL);
    ItemStack revolver_drum = FoundryItems.component(ItemComponent.COMPONENT_REVOLVER_DRUM);
    ItemStack revolver_frame = FoundryItems.component(ItemComponent.COMPONENT_REVOLVER_FRAME);
    ItemStack shotgun_pump = FoundryItems.component(ItemComponent.COMPONENT_SHOTGUN_PUMP);
    ItemStack shotgun_frame = FoundryItems.component(ItemComponent.COMPONENT_SHOTGUN_FRAME);
    ItemStack bullet_steel = FoundryItems.component(ItemComponent.COMPONENT_AMMO_BULLET_STEEL);
    ItemStack pellet_steel = FoundryItems.component(ItemComponent.COMPONENT_AMMO_PELLET_STEEL);
    
       
    MeltingRecipeManager.instance.addRecipe(
        bullet,
        new FluidStack(liquid_lead, FoundryAPI.FLUID_AMOUNT_NUGGET * 3));
    MeltingRecipeManager.instance.addRecipe(
        bullet_hollow,
        new FluidStack(liquid_lead, FoundryAPI.FLUID_AMOUNT_NUGGET * 3));
    MeltingRecipeManager.instance.addRecipe(
        bullet_jacketed,
        new FluidStack(liquid_copper, FoundryAPI.FLUID_AMOUNT_NUGGET));
    MeltingRecipeManager.instance.addRecipe(
        bullet_casing,
        new FluidStack(liquid_brass, FoundryAPI.FLUID_AMOUNT_NUGGET));
    MeltingRecipeManager.instance.addRecipe(
        pellet,
        new FluidStack(liquid_lead, FoundryAPI.FLUID_AMOUNT_NUGGET));
    MeltingRecipeManager.instance.addRecipe(
        shell_casing,
        new FluidStack(liquid_brass, FoundryAPI.FLUID_AMOUNT_NUGGET * 2));
    MeltingRecipeManager.instance.addRecipe(
        bullet_steel,
        new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_NUGGET * 3));
    MeltingRecipeManager.instance.addRecipe(
        pellet_steel,
        new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_NUGGET));


    MeltingRecipeManager.instance.addRecipe(
        gun_barrel,
        new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT));
    MeltingRecipeManager.instance.addRecipe(
        revolver_drum,
        new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 3 / 4));
    MeltingRecipeManager.instance.addRecipe(
        revolver_frame,
        new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 3 / 2));
    MeltingRecipeManager.instance.addRecipe(
        shotgun_pump,
        new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT / 2));
    MeltingRecipeManager.instance.addRecipe(
        shotgun_frame,
        new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 3 / 2));


    CastingRecipeManager.instance.addRecipe(
        bullet,
        new FluidStack(liquid_lead, FoundryAPI.FLUID_AMOUNT_NUGGET * 3), mold_bullet, null);
    CastingRecipeManager.instance.addRecipe(
        bullet_hollow,
        new FluidStack(liquid_lead, FoundryAPI.FLUID_AMOUNT_NUGGET * 3), mold_bullet_hollow, null);
    CastingRecipeManager.instance.addRecipe(
        bullet_jacketed,
        new FluidStack(liquid_copper, FoundryAPI.FLUID_AMOUNT_NUGGET), mold_bullet, bullet);
    CastingRecipeManager.instance.addRecipe(
        bullet_casing,
        new FluidStack(liquid_brass, FoundryAPI.FLUID_AMOUNT_NUGGET), mold_bullet_casing, null);
    CastingRecipeManager.instance.addRecipe(
        pellet,
        new FluidStack(liquid_lead, FoundryAPI.FLUID_AMOUNT_NUGGET), mold_pellet, null);
    CastingRecipeManager.instance.addRecipe(
        shell_casing,
        new FluidStack(liquid_brass, FoundryAPI.FLUID_AMOUNT_NUGGET * 2), mold_shell_casing, null);
    CastingRecipeManager.instance.addRecipe(
        bullet_steel,
        new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_NUGGET * 3), mold_bullet, null);
    CastingRecipeManager.instance.addRecipe(
        pellet_steel,
        new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_NUGGET), mold_pellet, null);

    
    CastingRecipeManager.instance.addRecipe(
        gun_barrel,
        new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT), mold_gun_barrel, null);
    CastingRecipeManager.instance.addRecipe(
        revolver_drum,
        new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 3 / 4), mold_revolver_drum, null);
    CastingRecipeManager.instance.addRecipe(
        revolver_frame,
        new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 3 / 2), mold_revolver_frame, null);
    CastingRecipeManager.instance.addRecipe(
        shotgun_pump,
        new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT / 2), mold_shotgun_pump, null);
    CastingRecipeManager.instance.addRecipe(
        shotgun_frame,
        new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 3 / 2), mold_shotgun_frame, null);

  }

  static public void Init()
  {
    ItemStack iron_stack = new ItemStack(Items.iron_ingot);
    ItemStack redstone_stack = new ItemStack(Items.redstone);
    ItemStack furnace_stack = new ItemStack(Blocks.furnace);
    ItemStack clay_stack = new ItemStack(Items.clay_ball);
    ItemStack sand_stack = new ItemStack(Blocks.sand,1,-1);
    ItemStack clayblock_stack = new ItemStack(Blocks.clay, 1, -1);
    ItemStack casing_stack = new ItemStack(FoundryBlocks.block_refractory_casing);
    ItemStack piston_stack = new ItemStack(Blocks.piston);
    ItemStack goldnugget_stack = new ItemStack(Items.gold_nugget);
    ItemStack chest_stack = new ItemStack(Blocks.chest);
    ItemStack paper_stack = new ItemStack(Items.paper);
    ItemStack foundryclay_stack = FoundryItems.component(ItemComponent.COMPONENT_REFRACTORYCLAY);
    ItemStack foundryclay8_stack = FoundryItems.component(ItemComponent.COMPONENT_REFRACTORYCLAY,8);
    ItemStack refbrick_stack = FoundryItems.component(ItemComponent.COMPONENT_REFRACTORYBRICK);
    ItemStack blankmold_stack = FoundryItems.component(ItemComponent.COMPONENT_BLANKMOLD);
    ItemStack heatingcoil_stack = FoundryItems.component(ItemComponent.COMPONENT_HEATINGCOIL);
    ItemStack glasspane_stack = new ItemStack(Blocks.glass_pane);
    ItemStack emptycontainer2_stack = FoundryItems.item_container.empty(2);
    ItemStack comparator_stack = new ItemStack(Items.comparator);
    ItemStack diamond_stack = new ItemStack(Items.diamond);
    ItemStack bucket_stack = new ItemStack(Items.bucket);

    GameRegistry.addRecipe(foundryclay8_stack,
        "CCC",
        "CSC",
        "CCC",
        'C', clay_stack,
        'S', sand_stack);

    GameRegistry.addShapelessRecipe(foundryclay8_stack,clayblock_stack, clayblock_stack, sand_stack);

    GameRegistry.addSmelting(
        FoundryItems.component(ItemComponent.COMPONENT_REFRACTORYCLAY),
        refbrick_stack, 0.0f);

    GameRegistry.addRecipe(blankmold_stack,
        "CC",
        'C', foundryclay_stack);


    ItemStack heatingcoil2_stack = FoundryItems.component(ItemComponent.COMPONENT_HEATINGCOIL,2);

    GameRegistry.addRecipe(new ShapedOreRecipe(
        emptycontainer2_stack,
        " T ",
        "BGB",
        " T ",
        'T', "ingotTin",
        'B', refbrick_stack,
        'G', glasspane_stack));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        heatingcoil2_stack,
        "RCR",
        "CGC",
        "RCR",
        'C', "ingotCupronickel",
        'G', goldnugget_stack,
        'R', redstone_stack));

    GameRegistry.addRecipe(
        casing_stack,
        "IBI",
        "B B",
        "IBI",
        'I', iron_stack, 'B',
        refbrick_stack);

    GameRegistry.addRecipe(new ShapedOreRecipe(
        FoundryBlocks.block_machine.asItemStack(EnumMachine.ICF),
        "IFI",
        "HCH",
        "HRH",
        'F', furnace_stack,
        'I', "ingotCopper",
        'C', casing_stack,
        'R', redstone_stack,
        'H', heatingcoil_stack));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        FoundryBlocks.block_machine.asItemStack(EnumMachine.INFUSER),
        "IRI",
        "GCG",
        "HRH",
        'I', iron_stack, 
        'R', redstone_stack, 
        'B', refbrick_stack,
        'C', casing_stack,
        'G', "gearStone",
        'H', heatingcoil_stack));
    
    GameRegistry.addRecipe(
        new ItemStack(FoundryBlocks.block_alloy_furnace),
        "BBB",
        "BFB",
        "BBB",
        'B', refbrick_stack, 
        'F', furnace_stack);

    GameRegistry.addRecipe(
        new ItemStack(FoundryBlocks.block_refractory_hopper),
        "R R",
        "RBR",
        " R ",
        'R', refbrick_stack, 
        'B', bucket_stack);

    GameRegistry.addRecipe(new ShapedOreRecipe(
        FoundryBlocks.block_machine.asItemStack(EnumMachine.ATOMIZER),
        "GHG",
        "RCR",
        " B ",
        'H', new ItemStack(FoundryBlocks.block_refractory_hopper), 
        'B', Items.bucket, 
        'R', Items.redstone,
        'C', casing_stack,
        'G', "gearStone"));

    GameRegistry.addRecipe(
        FoundryBlocks.block_machine.asItemStack(EnumMachine.CASTER),
        " H ",
        "RCR",
        "IPI",
        'H', chest_stack, 
        'I', iron_stack, 
        'P', piston_stack,
        'C', casing_stack,
        'R', redstone_stack);

    
    GameRegistry.addRecipe(new ShapedOreRecipe(
        FoundryBlocks.block_machine.asItemStack(EnumMachine.ALLOYMIXER),
        "GIG",
        "GCG",
        "IRI",
        'I', iron_stack, 
        'C', casing_stack,
        'R', redstone_stack,
        'G', "gearStone"));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        FoundryBlocks.block_machine.asItemStack(EnumMachine.MATERIALROUTER),
        "GIG",
        "DRD",
        "GCG",
        'I', diamond_stack, 
        'R', casing_stack,
        'D', redstone_stack,
        'C', comparator_stack,
        'G', "gearStone"));

    GameRegistry.addRecipe(
        FoundryItems.item_revolver.empty(),
        "BD",
        " F",
        'B', FoundryItems.component(ItemComponent.COMPONENT_GUN_BARREL), 
        'D', FoundryItems.component(ItemComponent.COMPONENT_REVOLVER_DRUM),
        'F', FoundryItems.component(ItemComponent.COMPONENT_REVOLVER_FRAME));

    GameRegistry.addRecipe(
        FoundryItems.item_shotgun.empty(),
        "BB",
        "PF",
        'B', FoundryItems.component(ItemComponent.COMPONENT_GUN_BARREL), 
        'P', FoundryItems.component(ItemComponent.COMPONENT_SHOTGUN_PUMP),
        'F', FoundryItems.component(ItemComponent.COMPONENT_SHOTGUN_FRAME));

    GameRegistry.addRecipe(
        new ItemStack(FoundryItems.item_component,4,ItemComponent.COMPONENT_GUNPOWDER_SMALL),
        "  ",
        " G",
        'G', Items.gunpowder);

    GameRegistry.addRecipe(
        new ItemStack(Items.gunpowder),
        "GG",
        "GG",
        'G', FoundryItems.component(ItemComponent.COMPONENT_GUNPOWDER_SMALL));

    GameRegistry.addRecipe(
        new ItemStack(FoundryItems.item_component,4,ItemComponent.COMPONENT_BLAZEPOWDER_SMALL),
        "  ",
        " B",
        'B', Items.blaze_powder);

    GameRegistry.addRecipe(
        new ItemStack(Items.blaze_powder),
        "BB",
        "BB",
        'B', FoundryItems.component(ItemComponent.COMPONENT_BLAZEPOWDER_SMALL));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        FoundryItems.item_round,
        "B",
        "G",
        "C",
        'B', FoundryItems.component(ItemComponent.COMPONENT_AMMO_BULLET), 
        'G', "dustSmallGunpowder",
        'C', FoundryItems.component(ItemComponent.COMPONENT_AMMO_CASING)));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        FoundryItems.item_round_hollow,
        "B",
        "G",
        "C",
        'B', FoundryItems.component(ItemComponent.COMPONENT_AMMO_BULLET_HOLLOW), 
        'G', "dustSmallGunpowder",
        'C', FoundryItems.component(ItemComponent.COMPONENT_AMMO_CASING)));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        FoundryItems.item_round_jacketed,
        "B",
        "G",
        "C",
        'B', FoundryItems.component(ItemComponent.COMPONENT_AMMO_BULLET_JACKETED), 
        'G', "dustSmallGunpowder",
        'C', FoundryItems.component(ItemComponent.COMPONENT_AMMO_CASING)));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        FoundryItems.item_round_fire,
        "B",
        "A",
        'B', "dustSmallBlaze", 
        'A', FoundryItems.item_round_hollow));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        FoundryItems.item_shell,
        "PAP",
        "PGP",
        "PCP",
        'P', FoundryItems.component(ItemComponent.COMPONENT_AMMO_PELLET), 
        'A', paper_stack, 
        'G', "dustSmallGunpowder",
        'C', FoundryItems.component(ItemComponent.COMPONENT_AMMO_CASING_SHELL)));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        FoundryItems.item_round_ap,
        "B",
        "G",
        "C",
        'B', FoundryItems.component(ItemComponent.COMPONENT_AMMO_BULLET_STEEL), 
        'G', "dustSmallGunpowder",
        'C', FoundryItems.component(ItemComponent.COMPONENT_AMMO_CASING)));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        FoundryItems.item_shell_ap,
        "PAP",
        "PGP",
        "PCP",
        'P', FoundryItems.component(ItemComponent.COMPONENT_AMMO_PELLET_STEEL), 
        'A', paper_stack, 
        'G', "dustSmallGunpowder",
        'C', FoundryItems.component(ItemComponent.COMPONENT_AMMO_CASING_SHELL)));

    GameRegistry.addShapelessRecipe(
        new ItemStack(FoundryItems.item_round_poison,2),
        Items.spider_eye, 
        FoundryItems.item_round_hollow,
        FoundryItems.item_round_hollow);

    GameRegistry.addRecipe(new ShapedOreRecipe(
        FoundryItems.component(ItemComponent.COMPONENT_DUST_BRASS,4),
        "CC",
        "CZ",
        'C', "dustCopper", 
        'Z', "dustZinc"));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        FoundryItems.component(ItemComponent.COMPONENT_DUST_CUPRONICKEL,2),
        "CN",
        'C', "dustCopper", 
        'N', "dustNickel"));

    GameRegistry.addSmelting(
        FoundryItems.component(ItemComponent.COMPONENT_DUST_ZINC),
        FoundryItems.ingot(ItemIngot.INGOT_ZINC),
        0);

    GameRegistry.addSmelting(
        FoundryItems.component(ItemComponent.COMPONENT_DUST_BRASS),
        FoundryItems.ingot(ItemIngot.INGOT_BRASS),
        0);

    GameRegistry.addSmelting(
        FoundryItems.component(ItemComponent.COMPONENT_DUST_CUPRONICKEL),
        FoundryItems.ingot(ItemIngot.INGOT_CUPRONICKEL),
        0);
    
    //Mold crafting with vanilla items
    FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_BLOCK_SOFT, new ItemStack(Blocks.planks,1,-1));
    FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_BLOCK_SOFT, new ItemStack(Blocks.stone,1,-1));
    FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_INGOT_SOFT, new ItemStack(Items.brick));
    FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_INGOT_SOFT, new ItemStack(Items.netherbrick));
    FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_INGOT_SOFT, refbrick_stack);
    if(FoundryConfig.recipe_tools_armor)
    {
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_CHESTPLATE_SOFT, new ItemStack(Items.iron_chestplate,1,-1));
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_CHESTPLATE_SOFT, new ItemStack(Items.golden_chestplate,1,-1));
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_CHESTPLATE_SOFT, new ItemStack(Items.diamond_chestplate,1,-1));
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, new ItemStack(Items.iron_leggings,1,-1));
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, new ItemStack(Items.golden_leggings,1,-1));
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, new ItemStack(Items.diamond_leggings,1,-1));
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_HELMET_SOFT, new ItemStack(Items.iron_helmet,1,-1));
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_HELMET_SOFT, new ItemStack(Items.golden_helmet,1,-1));
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_HELMET_SOFT, new ItemStack(Items.diamond_helmet,1,-1));
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_BOOTS_SOFT, new ItemStack(Items.iron_boots,1,-1));
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_BOOTS_SOFT, new ItemStack(Items.golden_boots,1,-1));
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_BOOTS_SOFT, new ItemStack(Items.diamond_boots,1,-1));
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_PICKAXE_SOFT, new ItemStack(Items.wooden_pickaxe));
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_PICKAXE_SOFT, new ItemStack(Items.stone_pickaxe));
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_PICKAXE_SOFT, new ItemStack(Items.iron_pickaxe));
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_PICKAXE_SOFT, new ItemStack(Items.golden_pickaxe));
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_PICKAXE_SOFT, new ItemStack(Items.diamond_pickaxe));
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_AXE_SOFT, new ItemStack(Items.wooden_axe));
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_AXE_SOFT, new ItemStack(Items.stone_axe));
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_AXE_SOFT, new ItemStack(Items.iron_axe));
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_AXE_SOFT, new ItemStack(Items.golden_axe));
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_AXE_SOFT, new ItemStack(Items.diamond_axe));
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_SHOVEL_SOFT, new ItemStack(Items.wooden_shovel));
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_SHOVEL_SOFT, new ItemStack(Items.stone_shovel));
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_SHOVEL_SOFT, new ItemStack(Items.iron_shovel));
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_SHOVEL_SOFT, new ItemStack(Items.golden_shovel));
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_SHOVEL_SOFT, new ItemStack(Items.diamond_shovel));
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_HOE_SOFT, new ItemStack(Items.wooden_hoe));
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_HOE_SOFT, new ItemStack(Items.stone_hoe));
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_HOE_SOFT, new ItemStack(Items.iron_hoe));
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_HOE_SOFT, new ItemStack(Items.golden_hoe));
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_HOE_SOFT, new ItemStack(Items.diamond_hoe));
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_SWORD_SOFT, new ItemStack(Items.wooden_sword));
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_SWORD_SOFT, new ItemStack(Items.stone_sword));
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_SWORD_SOFT, new ItemStack(Items.iron_sword));
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_SWORD_SOFT, new ItemStack(Items.golden_sword));
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_SWORD_SOFT, new ItemStack(Items.diamond_sword));
    }

    FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_BULLET_SOFT, FoundryItems.component(ItemComponent.COMPONENT_AMMO_BULLET));
    FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_BULLET_HOLLOW_SOFT, FoundryItems.component(ItemComponent.COMPONENT_AMMO_BULLET_HOLLOW));
    FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_BULLET_CASING_SOFT, FoundryItems.component(ItemComponent.COMPONENT_AMMO_CASING));
    FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_GUN_BARREL_SOFT, FoundryItems.component(ItemComponent.COMPONENT_GUN_BARREL));
    FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_REVOLVER_DRUM_SOFT, FoundryItems.component(ItemComponent.COMPONENT_REVOLVER_DRUM));
    FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_REVOLVER_FRAME_SOFT, FoundryItems.component(ItemComponent.COMPONENT_REVOLVER_FRAME));
    FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_PELLET_SOFT, FoundryItems.component(ItemComponent.COMPONENT_AMMO_PELLET));
    FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_SHELL_CASING_SOFT, FoundryItems.component(ItemComponent.COMPONENT_AMMO_CASING_SHELL));
    FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_SHOTGUN_PUMP_SOFT, FoundryItems.component(ItemComponent.COMPONENT_SHOTGUN_PUMP));
    FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_SHOTGUN_FRAME_SOFT, FoundryItems.component(ItemComponent.COMPONENT_SHOTGUN_FRAME));

    FMLControlledNamespacedRegistry<Block> reg = GameData.getBlockRegistry();
    for(Object obj:reg)
    {
      Block block = (Block)obj;
      if(block instanceof BlockStairs)
      {

        FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_STAIRS_SOFT, new ItemStack(block, 1, -1));
      } else if(block instanceof BlockSlab && !block.isOpaqueCube())
      {
        FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_SLAB_SOFT, new ItemStack(block, 1, -1));
      }
    }
    
    for(String name:LiquidMetalRegistry.instance.getFluidNames())
    {
      FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_GEAR_SOFT, "gear" + name);
    }
    FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_GEAR_SOFT, "gearWood");
    FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_GEAR_SOFT, "gearDiamond");
    FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_GEAR_SOFT, "gearStone");

    //Ingot and block mold crafting recipes
    for(String name:OreDictionary.getOreNames())
    {
      if(name.startsWith("ingot"))
      {
        FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_INGOT_SOFT, name);
      } else if(name.startsWith("block"))
      {
        FoundryMiscUtils.registerMoldRecipe(ItemMold.MOLD_BLOCK_SOFT, name);
      }
    }

    //Ore -> ingot furnace recipes
    FoundryMiscUtils.registerOreSmelting(BlockFoundryOre.EnumOre.COPPER,ItemIngot.INGOT_COPPER);
    FoundryMiscUtils.registerOreSmelting(BlockFoundryOre.EnumOre.TIN,ItemIngot.INGOT_TIN);
    FoundryMiscUtils.registerOreSmelting(BlockFoundryOre.EnumOre.ZINC,ItemIngot.INGOT_ZINC);
    FoundryMiscUtils.registerOreSmelting(BlockFoundryOre.EnumOre.NICKEL,ItemIngot.INGOT_NICKEL);
    FoundryMiscUtils.registerOreSmelting(BlockFoundryOre.EnumOre.SILVER,ItemIngot.INGOT_SILVER);
    FoundryMiscUtils.registerOreSmelting(BlockFoundryOre.EnumOre.LEAD,ItemIngot.INGOT_LEAD);
    
    //Clay mold furnace recipes
    FoundryMiscUtils.registerMoldSmelting(ItemMold.MOLD_BLOCK_SOFT,ItemMold.MOLD_BLOCK);
    FoundryMiscUtils.registerMoldSmelting(ItemMold.MOLD_INGOT_SOFT,ItemMold.MOLD_INGOT);
    FoundryMiscUtils.registerMoldSmelting(ItemMold.MOLD_CABLE_IC2_SOFT,ItemMold.MOLD_CABLE_IC2);
    FoundryMiscUtils.registerMoldSmelting(ItemMold.MOLD_CASING_IC2_SOFT,ItemMold.MOLD_CASING_IC2);
    FoundryMiscUtils.registerMoldSmelting(ItemMold.MOLD_SLAB_SOFT,ItemMold.MOLD_SLAB);
    FoundryMiscUtils.registerMoldSmelting(ItemMold.MOLD_STAIRS_SOFT,ItemMold.MOLD_STAIRS);
    FoundryMiscUtils.registerMoldSmelting(ItemMold.MOLD_PLATE_SOFT,ItemMold.MOLD_PLATE);
    FoundryMiscUtils.registerMoldSmelting(ItemMold.MOLD_GEAR_SOFT,ItemMold.MOLD_GEAR);
    FoundryMiscUtils.registerMoldSmelting(ItemMold.MOLD_INSULATED_CABLE_IC2_SOFT,ItemMold.MOLD_INSULATED_CABLE_IC2);
    FoundryMiscUtils.registerMoldSmelting(ItemMold.MOLD_WIRE_PR_SOFT,ItemMold.MOLD_WIRE_PR);

    FoundryMiscUtils.registerMoldSmelting(ItemMold.MOLD_BULLET_SOFT,ItemMold.MOLD_BULLET);
    FoundryMiscUtils.registerMoldSmelting(ItemMold.MOLD_BULLET_HOLLOW_SOFT,ItemMold.MOLD_BULLET_HOLLOW);
    FoundryMiscUtils.registerMoldSmelting(ItemMold.MOLD_BULLET_CASING_SOFT,ItemMold.MOLD_BULLET_CASING);
    FoundryMiscUtils.registerMoldSmelting(ItemMold.MOLD_GUN_BARREL_SOFT,ItemMold.MOLD_GUN_BARREL);
    FoundryMiscUtils.registerMoldSmelting(ItemMold.MOLD_REVOLVER_DRUM_SOFT,ItemMold.MOLD_REVOLVER_DRUM);
    FoundryMiscUtils.registerMoldSmelting(ItemMold.MOLD_REVOLVER_FRAME_SOFT,ItemMold.MOLD_REVOLVER_FRAME);
    FoundryMiscUtils.registerMoldSmelting(ItemMold.MOLD_PELLET_SOFT,ItemMold.MOLD_PELLET);
    FoundryMiscUtils.registerMoldSmelting(ItemMold.MOLD_SHELL_CASING_SOFT,ItemMold.MOLD_SHELL_CASING);
    FoundryMiscUtils.registerMoldSmelting(ItemMold.MOLD_SHOTGUN_PUMP_SOFT,ItemMold.MOLD_SHOTGUN_PUMP);
    FoundryMiscUtils.registerMoldSmelting(ItemMold.MOLD_SHOTGUN_FRAME_SOFT,ItemMold.MOLD_SHOTGUN_FRAME);

    if(FoundryConfig.recipe_tools_armor)
    {
      FoundryMiscUtils.registerMoldSmelting(ItemMold.MOLD_CHESTPLATE_SOFT,ItemMold.MOLD_CHESTPLATE);
      FoundryMiscUtils.registerMoldSmelting(ItemMold.MOLD_PICKAXE_SOFT,ItemMold.MOLD_PICKAXE);
      FoundryMiscUtils.registerMoldSmelting(ItemMold.MOLD_AXE_SOFT,ItemMold.MOLD_AXE);
      FoundryMiscUtils.registerMoldSmelting(ItemMold.MOLD_SHOVEL_SOFT,ItemMold.MOLD_SHOVEL);
      FoundryMiscUtils.registerMoldSmelting(ItemMold.MOLD_SWORD_SOFT,ItemMold.MOLD_SWORD);
      FoundryMiscUtils.registerMoldSmelting(ItemMold.MOLD_HOE_SOFT,ItemMold.MOLD_HOE);
      FoundryMiscUtils.registerMoldSmelting(ItemMold.MOLD_LEGGINGS_SOFT,ItemMold.MOLD_LEGGINGS);
      FoundryMiscUtils.registerMoldSmelting(ItemMold.MOLD_HELMET_SOFT,ItemMold.MOLD_HELMET);
      FoundryMiscUtils.registerMoldSmelting(ItemMold.MOLD_BOOTS_SOFT,ItemMold.MOLD_BOOTS);
      FoundryMiscUtils.registerMoldSmelting(ItemMold.MOLD_SICKLE_SOFT,ItemMold.MOLD_SICKLE);
      FoundryMiscUtils.registerMoldSmelting(ItemMold.MOLD_BOW_SOFT,ItemMold.MOLD_BOW);
    }
  }

  static public void PostInit()
  {
    if(OreDictionary.getOres("gearStone").size() == 0)
    {
      ItemStack cobble_stack = new ItemStack(Blocks.cobblestone, 1, -1);
      ItemStack stick_stack = new ItemStack(Items.stick);

      ItemStack gear_stack = FoundryItems.component(ItemComponent.COMPONENT_GEAR);
      OreDictionary.registerOre("gearStone", gear_stack);
      GameRegistry.addRecipe((ItemStack)gear_stack,
          " C ",
          "CSC",
          " C ",
          'C', cobble_stack,
          'S', stick_stack);
    }
    
    for(OreDictType type:OreDictType.TYPES)
    {
      for(OreDictMaterial material:OreDictMaterial.MATERIALS)
      {
        String od_name = type.prefix + material.suffix;
        for(ItemStack item:OreDictionary.getOres(od_name))
        {
          MaterialRegistry.instance.registerItem(item, material.suffix, type.name);
        }
      }
    }
    
    for(Object obj:FurnaceRecipes.instance().getSmeltingList().entrySet())
    {
      @SuppressWarnings("unchecked")
      Map.Entry<Object, ItemStack> entry = (Map.Entry<Object, ItemStack>)obj;
      Object key = entry.getKey();
      ItemStack stack = null;
      if(key instanceof Item)
      {
        stack = new ItemStack((Item)key);
      } else if(key instanceof Block)
      {
        stack = new ItemStack((Block)key);
      } else if(key instanceof ItemStack)
      {
        stack = ((ItemStack)key).copy();
      }
      
      if(stack != null && MeltingRecipeManager.instance.findRecipe(stack) == null)
      {
        ItemStack result = entry.getValue();
        IMeltingRecipe recipe = MeltingRecipeManager.instance.findRecipe(result);
        if(recipe != null)
        {
          Fluid liquid_metal = recipe.getOutput().getFluid();
          int base_amount = recipe.getOutput().amount;

          int[] ids = OreDictionary.getOreIDs(stack);
          for(int j : ids)
          {
            if(OreDictionary.getOreName(j).startsWith("ore"))
            {
              base_amount = FoundryAPI.FLUID_AMOUNT_ORE;
              break;
            }
          }
          MeltingRecipeManager.instance.addRecipe(stack, new FluidStack(liquid_metal, base_amount * result.stackSize),recipe.getMeltingPoint(),recipe.getMeltingSpeed());
        }
      }
    }
    
    ItemStack ingot_mold = FoundryItems.mold(ItemMold.MOLD_INGOT);
    for(String name:LiquidMetalRegistry.instance.getFluidNames())
    {
      if(!name.startsWith("Glass"))
      {
        FluidStack fluid = new FluidStack(LiquidMetalRegistry.instance.getFluid(name), FoundryAPI.FLUID_AMOUNT_INGOT);
        List<ItemStack> ores = OreDictionary.getOres("ingot" + name);
        if(ores != null && ores.size() > 0)
        {
          if(CastingRecipeManager.instance.findRecipe(fluid, ingot_mold, null) == null)
          {
            CastingRecipeManager.instance.addRecipe("ingot" + name, fluid, ingot_mold, null);
          }
        }
        
        AtomizerRecipeManager.instance.addRecipe("dust" + name, fluid);
      }
    }
  }
}
