package exter.foundry.recipes;

import java.util.Map.Entry;

import cpw.mods.fml.common.registry.FMLControlledNamespacedRegistry;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import exter.foundry.api.FoundryAPI;
import exter.foundry.api.FoundryUtils;
import exter.foundry.api.orestack.OreStack;
import exter.foundry.api.substance.InfuserSubstance;
import exter.foundry.block.BlockFoundryMachine;
import exter.foundry.block.BlockFoundryOre;
import exter.foundry.block.FoundryBlocks;
import exter.foundry.config.FoundryConfig;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemFoundryComponent;
import exter.foundry.item.ItemIngot;
import exter.foundry.item.ItemMold;
import exter.foundry.material.MaterialRegistry;
import exter.foundry.material.OreDictMaterial;
import exter.foundry.material.OreDictType;
import exter.foundry.recipes.manager.AlloyFurnaceRecipeManager;
import exter.foundry.recipes.manager.AlloyMixerRecipeManager;
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
  static public Fluid liquid_rubber;

  static public void PreInit()
  {
    int i;
    liquid_iron = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Iron", 1850, 15);
    liquid_gold = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Gold", 1350, 15);
    liquid_copper = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Copper", 1400, 15);
    liquid_tin = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Tin", 550, 7);
    liquid_bronze = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Bronze", 1400, 15);
    liquid_electrum = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Electrum", 1350, 15);
    liquid_invar = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Invar", 1850, 15);
    liquid_nickel = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Nickel", 1750, 15);
    liquid_zinc = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Zinc", 700, 15);
    liquid_brass = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Brass", 1400, 15);
    liquid_silver = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Silver", 1250, 15);
    liquid_steel = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Steel", 1850, 15);
    liquid_cupronickel = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Cupronickel", 1750, 15);

    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Lead", 650, 1);  
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Aluminum", 1100, 15);  
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Chromium", 2200, 8);   
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Platinum", 2050, 15);  
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Manganese", 1550, 15);   
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Titanium", 2000, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Rubber", 460, 0);

    LiquidMetalRegistry.instance.RegisterLiquidMetal( "StainlessSteel", 1900, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Kanthal", 1900, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Nichrome", 1950, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Enderium", 1900, 12);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Mithril", 1950, 12);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Signalum", 1400, 12);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Lumium", 2500, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "ElectrumFlux", 1500, 14);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Thaumium", 1850, 14);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Manasteel", 1950, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Terrasteel", 2100, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Elementium", 2400, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "Redstone", 1000, 8);
    LiquidMetalRegistry.instance.RegisterLiquidMetal( "RedAlloy", 1800, 10);

    for(String name:LiquidMetalRegistry.instance.GetFluidNames())
    {
      FoundryUtils.RegisterBasicMeltingRecipes(name,LiquidMetalRegistry.instance.GetFluid(name));
    }
    FoundryUtils.RegisterBasicMeltingRecipes("Chrome",LiquidMetalRegistry.instance.GetFluid("Chromium"));
    FoundryUtils.RegisterBasicMeltingRecipes("Aluminium",LiquidMetalRegistry.instance.GetFluid("Aluminum"));


    
    AlloyFurnaceRecipeManager.instance.AddRecipe(
        new ItemStack(FoundryItems.item_ingot, 4, ItemIngot.INGOT_BRONZE),
        new Object[] {
            new OreStack("ingotCopper", 3),
            new OreStack("dustCopper", 3) },
        new Object[] {
            new OreStack("ingotTin", 1),
            new OreStack("dustTin", 1) }
        );
    
    AlloyFurnaceRecipeManager.instance.AddRecipe(
        new ItemStack(FoundryItems.item_ingot, 4, ItemIngot.INGOT_BRASS),
        new Object[] {
            new OreStack("ingotCopper", 3),
            new OreStack("dustCopper", 3) },
        new Object[] {
            new OreStack("ingotZinc", 1),
            new OreStack("dustZinc", 1) }
        );
    
    AlloyFurnaceRecipeManager.instance.AddRecipe(
        new ItemStack(FoundryItems.item_ingot, 3, ItemIngot.INGOT_INVAR),
        new Object[] {
            new OreStack("ingotIron", 2),
            new OreStack("dustIron", 2) },
        new Object[] {
            new OreStack("ingotNickel", 1),
            new OreStack("dustNickel", 1) }
        );

    AlloyFurnaceRecipeManager.instance.AddRecipe(
        new ItemStack(FoundryItems.item_ingot, 2, ItemIngot.INGOT_ELECTRUM),
        new Object[] {
            new OreStack("ingotGold", 1),
            new OreStack("dustGold", 1) },
        new Object[] {
            new OreStack("ingotSilver", 1),
            new OreStack("dustSilver", 1) }
        );
    
    AlloyFurnaceRecipeManager.instance.AddRecipe(
        new ItemStack(FoundryItems.item_ingot, 2, ItemIngot.INGOT_CUPRONICKEL),
        new Object[] {
            new OreStack("ingotCopper", 1),
            new OreStack("dustCopper", 1) },
        new Object[] {
            new OreStack("ingotNickel", 1),
            new OreStack("dustNickel", 1) }
        );
    

    AlloyMixerRecipeManager.instance.AddRecipe(
        new FluidStack(liquid_bronze, 4),
        new FluidStack[] {
          new FluidStack(liquid_copper, 3),
          new FluidStack(liquid_tin, 1)
          });
    
    AlloyMixerRecipeManager.instance.AddRecipe(
        new FluidStack(liquid_brass, 4),
        new FluidStack[] {
          new FluidStack(liquid_copper, 3),
          new FluidStack(liquid_zinc, 1)
        });
    
    AlloyMixerRecipeManager.instance.AddRecipe(
        new FluidStack(liquid_invar, 3),
        new FluidStack[] {
          new FluidStack(liquid_iron, 2),
          new FluidStack(liquid_nickel, 1)
        });

    AlloyMixerRecipeManager.instance.AddRecipe(
        new FluidStack(liquid_electrum, 2),
        new FluidStack[] {
          new FluidStack(liquid_gold, 1),
          new FluidStack(liquid_silver, 1)
        });

    AlloyMixerRecipeManager.instance.AddRecipe(
        new FluidStack(liquid_cupronickel, 2),
        new FluidStack[] {
          new FluidStack(liquid_copper, 1),
          new FluidStack(liquid_nickel, 1)
        });

    ItemStack mold_ingot = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_INGOT);
    ItemStack mold_cable_ic2 = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_CABLE_IC2);
    ItemStack mold_casing_ic2 = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_CASING_IC2);
    ItemStack mold_slab = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_SLAB);
    ItemStack mold_stairs = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_STAIRS);
    ItemStack mold_plate_ic2 = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_PLATE_IC2);
    ItemStack mold_block = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_BLOCK);
    ItemStack mold_gear = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_GEAR);
    ItemStack extra_sticks1 = new ItemStack(Items.stick,1);
    ItemStack extra_sticks2 = new ItemStack(Items.stick,2);
    
    CastingRecipeManager.instance.AddMold(mold_ingot);
    CastingRecipeManager.instance.AddMold(mold_cable_ic2);
    CastingRecipeManager.instance.AddMold(mold_casing_ic2);
    CastingRecipeManager.instance.AddMold(mold_slab);
    CastingRecipeManager.instance.AddMold(mold_stairs);
    CastingRecipeManager.instance.AddMold(mold_plate_ic2);
    CastingRecipeManager.instance.AddMold(mold_block);
    CastingRecipeManager.instance.AddMold(mold_gear);

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

      CastingRecipeManager.instance.AddMold(mold_chestplate);
      CastingRecipeManager.instance.AddMold(mold_pickaxe);
      CastingRecipeManager.instance.AddMold(mold_axe);
      CastingRecipeManager.instance.AddMold(mold_shovel);
      CastingRecipeManager.instance.AddMold(mold_hoe);
      CastingRecipeManager.instance.AddMold(mold_sword);
      CastingRecipeManager.instance.AddMold(mold_leggings);
      CastingRecipeManager.instance.AddMold(mold_helmet);
      CastingRecipeManager.instance.AddMold(mold_boots);

      CastingRecipeManager.instance.AddRecipe(new ItemStack(Items.iron_chestplate, 1, 0), new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 8), mold_chestplate, null);
      CastingRecipeManager.instance.AddRecipe(new ItemStack(Items.golden_chestplate, 1, 0), new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 8), mold_chestplate, null);

      CastingRecipeManager.instance.AddRecipe(new ItemStack(Items.iron_pickaxe, 1, 0), new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_pickaxe, extra_sticks2);
      CastingRecipeManager.instance.AddRecipe(new ItemStack(Items.golden_pickaxe, 1, 0), new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_pickaxe, extra_sticks2);

      CastingRecipeManager.instance.AddRecipe(new ItemStack(Items.iron_axe, 1, 0), new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_axe, extra_sticks2);
      CastingRecipeManager.instance.AddRecipe(new ItemStack(Items.golden_axe, 1, 0), new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_axe, extra_sticks2);

      CastingRecipeManager.instance.AddRecipe(new ItemStack(Items.iron_shovel, 1, 0), new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 1), mold_shovel, extra_sticks2);
      CastingRecipeManager.instance.AddRecipe(new ItemStack(Items.golden_shovel, 1, 0), new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 1), mold_shovel, extra_sticks2);

      CastingRecipeManager.instance.AddRecipe(new ItemStack(Items.iron_sword, 1, 0), new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_sword, extra_sticks1);
      CastingRecipeManager.instance.AddRecipe(new ItemStack(Items.golden_sword, 1, 0), new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_sword, extra_sticks1);

      CastingRecipeManager.instance.AddRecipe(new ItemStack(Items.iron_hoe, 1, 0), new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_hoe, extra_sticks2);
      CastingRecipeManager.instance.AddRecipe(new ItemStack(Items.golden_hoe, 1, 0), new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_hoe, extra_sticks2);

      CastingRecipeManager.instance.AddRecipe(new ItemStack(Items.iron_leggings, 1, 0), new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 7), mold_leggings, null);
      CastingRecipeManager.instance.AddRecipe(new ItemStack(Items.golden_leggings, 1, 0), new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 7), mold_leggings, null);

      CastingRecipeManager.instance.AddRecipe(new ItemStack(Items.iron_helmet, 1, 0), new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 5), mold_helmet, null);
      CastingRecipeManager.instance.AddRecipe(new ItemStack(Items.golden_helmet, 1, 0), new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 5), mold_helmet, null);

      CastingRecipeManager.instance.AddRecipe(new ItemStack(Items.iron_boots, 1, 0), new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_boots, null);
      CastingRecipeManager.instance.AddRecipe(new ItemStack(Items.golden_boots, 1, 0), new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_boots, null);

    }
    
    //Ingot casting recipes.
    for(Entry<String,ItemStack> entry:FoundryItems.ingot_stacks.entrySet())
    {
      CastingRecipeManager.instance.AddRecipe(
          entry.getValue(),
          new FluidStack(
              LiquidMetalRegistry.instance.GetFluid(entry.getKey()),
              FoundryAPI.FLUID_AMOUNT_INGOT),
          mold_ingot, null);
    }
    
    
    //Metal block casting recipes.
    for(Entry<String,ItemStack> entry:FoundryBlocks.block_stacks.entrySet())
    {
      Fluid fluid = LiquidMetalRegistry.instance.GetFluid(entry.getKey());
      if(fluid != null)
      {
        CastingRecipeManager.instance.AddRecipe(
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
          LiquidMetalRegistry.instance.GetFluid(entry.getKey()),
          FoundryAPI.FLUID_AMOUNT_BLOCK / 2);

      CastingRecipeManager.instance.AddRecipe(stack, fluid, mold_slab, null);
      MeltingRecipeManager.instance.AddRecipe(stack, fluid);
    }

    //Metal stairs casting recipes
    for(i = 0; i < FoundryBlocks.block_metal_stairs.length; i++)
    {
      if(FoundryBlocks.block_metal_stairs[i] != null)
      {
        FoundryBlocks.MetalStair mr = FoundryBlocks.STAIRS_BLOCKS[i];
        ItemStack stack = new ItemStack(FoundryBlocks.block_metal_stairs[i]);
        FluidStack fluid = new FluidStack(
            LiquidMetalRegistry.instance.GetFluid(mr.metal),
            FoundryAPI.FLUID_AMOUNT_BLOCK * 3 / 4);
        
        CastingRecipeManager.instance.AddRecipe(stack, fluid, mold_stairs, null);
        MeltingRecipeManager.instance.AddRecipe(stack, fluid);
      }
    }
    
    InfuserRecipeManager.instance.AddSubstanceRecipe(new InfuserSubstance("carbon",36), new ItemStack(Items.coal,1,0), 240000);
    InfuserRecipeManager.instance.AddSubstanceRecipe(new InfuserSubstance("carbon",12), new ItemStack(Items.coal,1,1), 480000);
    InfuserRecipeManager.instance.AddSubstanceRecipe(new InfuserSubstance("carbon",324), new ItemStack(Blocks.coal_block,1), 1920000);
    InfuserRecipeManager.instance.AddSubstanceRecipe(new InfuserSubstance("carbon",36), "dustCoal", 160000);
    InfuserRecipeManager.instance.AddSubstanceRecipe(new InfuserSubstance("carbon",12), "dustCharcoal", 320000);

    InfuserRecipeManager.instance.AddRecipe(new FluidStack(liquid_steel,3), new FluidStack(liquid_iron,3), new InfuserSubstance("carbon", 2));

    
    if(FoundryConfig.recipe_gear_useoredict)
    {
      for(String name:LiquidMetalRegistry.instance.GetFluidNames())
      {
        Fluid fluid = LiquidMetalRegistry.instance.GetFluid(name);
        MeltingRecipeManager.instance.AddRecipe("gear" + name, new FluidStack(fluid,FoundryAPI.FLUID_AMOUNT_INGOT * 4));
        CastingRecipeManager.instance.AddRecipe("gear" + name, new FluidStack(fluid, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
      }
    }
           
    if(FoundryConfig.recipe_glass)
    {
      final String[] oredict_names = { "dyeBlack", "dyeRed", "dyeGreen", "dyeBrown", "dyeBlue", "dyePurple", "dyeCyan", "dyeLightGray", "dyeGray", "dyePink", "dyeLime", "dyeYellow", "dyeLightBlue", "dyeMagenta", "dyeOrange", "dyeWhite" };

      int temp = 1550;
      Fluid liquid_glass = LiquidMetalRegistry.instance.RegisterLiquidMetal("Glass", temp, 12);
      MeltingRecipeManager.instance.AddRecipe(new ItemStack(Blocks.sand), new FluidStack(liquid_glass,1000),temp,250);
      MeltingRecipeManager.instance.AddRecipe(new ItemStack(Blocks.glass), new FluidStack(liquid_glass,1000),temp,250);
      MeltingRecipeManager.instance.AddRecipe(new ItemStack(Blocks.glass_pane), new FluidStack(liquid_glass,375),temp,250);
      CastingRecipeManager.instance.AddRecipe(new ItemStack(Blocks.glass), new FluidStack(liquid_glass,1000),mold_block,null,400);
      
      for(i = 0; i < ItemDye.field_150921_b/*icon_names*/.length; i++)
      {
        String name = ItemDye.field_150921_b/*icon_names*/[i];
        int color = ItemDye.field_150922_c/*colors*/[i];
        int c1 = 63 + (color & 0xFF) * 3 / 4;
        int c2 = 63 + ((color >> 8 ) & 0xFF) * 3 / 4;
        int c3 = 63 + ((color >> 16) & 0xFF) * 3 / 4;
        int fluid_color = c1 | (c2 << 8) | (c3 << 16);
        
        Fluid liquid_glass_colored = LiquidMetalRegistry.instance.RegisterLiquidMetal("Glass." + name, temp, 12,"liquidGlass",fluid_color);

        int meta = ~i & 15;
        MeltingRecipeManager.instance.AddRecipe(new ItemStack(Blocks.stained_glass,1,meta), new FluidStack(liquid_glass_colored,1000),temp,250);
        MeltingRecipeManager.instance.AddRecipe(new ItemStack(Blocks.stained_glass_pane,1,meta), new FluidStack(liquid_glass_colored,375),temp,250);
        CastingRecipeManager.instance.AddRecipe(new ItemStack(Blocks.stained_glass,1,meta), new FluidStack(liquid_glass_colored,1000),mold_block,null,400);
        
        InfuserRecipeManager.instance.AddSubstanceRecipe(new InfuserSubstance("dye." + name,200), oredict_names[i], 25000);
        InfuserRecipeManager.instance.AddRecipe(new FluidStack(liquid_glass_colored,40),new FluidStack(liquid_glass,40),new InfuserSubstance("dye." + name,1));
      }
    }

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
    ItemStack foundryclay_stack = new ItemStack(FoundryItems.item_component,1,ItemFoundryComponent.COMPONENT_FOUNDRYCLAY);
    ItemStack foundryclay8_stack = new ItemStack(FoundryItems.item_component,8,ItemFoundryComponent.COMPONENT_FOUNDRYCLAY);
    ItemStack foundrybrick_stack = new ItemStack(FoundryItems.item_component,1,ItemFoundryComponent.COMPONENT_FOUNDRYBRICK);
    ItemStack blankmold_stack = new ItemStack(FoundryItems.item_component,1,ItemFoundryComponent.COMPONENT_BLANKMOLD);
    ItemStack heatingcoil_stack = new ItemStack(FoundryItems.item_component,1,ItemFoundryComponent.COMPONENT_HEATINGCOIL);
    ItemStack glasspane_stack = new ItemStack(Blocks.glass_pane);
    ItemStack emptycontainer2_stack = FoundryItems.item_container.EmptyContainer(2);
    ItemStack comparator_stack = new ItemStack(Items.comparator);
    ItemStack diamond_stack = new ItemStack(Items.diamond);
    
    GameRegistry.addRecipe(foundryclay8_stack,
        "CCC",
        "CSC",
        "CCC",
        'C', clay_stack,
        'S', sand_stack);

    GameRegistry.addShapelessRecipe(foundryclay8_stack,clayblock_stack, clayblock_stack, sand_stack);

    FurnaceRecipes.smelting().func_151394_a/*addSmelting*/(
        new ItemStack(FoundryItems.item_component, 1, ItemFoundryComponent.COMPONENT_FOUNDRYCLAY),
        foundrybrick_stack, 0.0f);

    GameRegistry.addRecipe(blankmold_stack,
        "CC",
        'C', foundryclay_stack);


    ItemStack heatingcoil2_stack = new ItemStack(FoundryItems.item_component,2,ItemFoundryComponent.COMPONENT_HEATINGCOIL);

    GameRegistry.addRecipe(new ShapedOreRecipe(
        emptycontainer2_stack,
        " T ",
        "BGB",
        " T ",
        'T', "ingotTin",
        'B', foundrybrick_stack,
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
        foundrybrick_stack);

    GameRegistry.addRecipe(new ShapedOreRecipe(
        new ItemStack(FoundryBlocks.block_machine, 1, BlockFoundryMachine.MACHINE_ICF),
        "IFI",
        "HCH",
        "HRH",
        'F', furnace_stack,
        'I', "ingotCopper",
        'C', casing_stack,
        'R', redstone_stack,
        'H', heatingcoil_stack));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        new ItemStack(FoundryBlocks.block_machine,1,BlockFoundryMachine.MACHINE_INFUSER),
        "IRI",
        "GCG",
        "HRH",
        'I', iron_stack, 
        'R', redstone_stack, 
        'B', foundrybrick_stack,
        'C', casing_stack,
        'G', "gearStone",
        'H', heatingcoil_stack));
    
    GameRegistry.addRecipe(
        new ItemStack(FoundryBlocks.block_alloy_furnace),
        "BBB",
        "BFB",
        "BBB",
        'B', foundrybrick_stack, 
        'F', furnace_stack);

    GameRegistry.addRecipe(
        new ItemStack(FoundryBlocks.block_machine,1,BlockFoundryMachine.MACHINE_CASTER),
        " H ",
        "RCR",
        "IPI",
        'H', chest_stack, 
        'I', iron_stack, 
        'P', piston_stack,
        'C', casing_stack,
        'R', redstone_stack);

    GameRegistry.addRecipe(new ShapedOreRecipe(
        new ItemStack(FoundryBlocks.block_machine,1,BlockFoundryMachine.MACHINE_ALLOYMIXER),
        "GIG",
        "GCG",
        "IRI",
        'I', iron_stack, 
        'C', casing_stack,
        'R', redstone_stack,
        'G', "gearStone"));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        new ItemStack(FoundryBlocks.block_machine,1,BlockFoundryMachine.MACHINE_MATERIALROUTER),
        "GIG",
        "DRD",
        "GCG",
        'I', diamond_stack, 
        'R', casing_stack,
        'D', redstone_stack,
        'C', comparator_stack,
        'G', "gearStone"));

    //Mold crafting with vanilla items
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BLOCK_SOFT, new ItemStack(Blocks.planks,1,-1));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BLOCK_SOFT, new ItemStack(Blocks.stone,1,-1));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_INGOT_SOFT, new ItemStack(Items.brick));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_INGOT_SOFT, new ItemStack(Items.netherbrick));
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_INGOT_SOFT, foundrybrick_stack);
    if(FoundryConfig.recipe_tools_armor)
    {
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CHESTPLATE_SOFT, new ItemStack(Items.iron_chestplate,1,-1));
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CHESTPLATE_SOFT, new ItemStack(Items.golden_chestplate,1,-1));
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_CHESTPLATE_SOFT, new ItemStack(Items.diamond_chestplate,1,-1));
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, new ItemStack(Items.iron_leggings,1,-1));
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, new ItemStack(Items.golden_leggings,1,-1));
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_LEGGINGS_SOFT, new ItemStack(Items.diamond_leggings,1,-1));
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_SOFT, new ItemStack(Items.iron_helmet,1,-1));
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_SOFT, new ItemStack(Items.golden_helmet,1,-1));
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HELMET_SOFT, new ItemStack(Items.diamond_helmet,1,-1));
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_SOFT, new ItemStack(Items.iron_boots,1,-1));
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_SOFT, new ItemStack(Items.golden_boots,1,-1));
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BOOTS_SOFT, new ItemStack(Items.diamond_boots,1,-1));
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_SOFT, new ItemStack(Items.wooden_pickaxe));
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_SOFT, new ItemStack(Items.stone_pickaxe));
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_SOFT, new ItemStack(Items.iron_pickaxe));
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_SOFT, new ItemStack(Items.golden_pickaxe));
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PICKAXE_SOFT, new ItemStack(Items.diamond_pickaxe));
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_SOFT, new ItemStack(Items.wooden_axe));
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_SOFT, new ItemStack(Items.stone_axe));
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_SOFT, new ItemStack(Items.iron_axe));
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_SOFT, new ItemStack(Items.golden_axe));
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_AXE_SOFT, new ItemStack(Items.diamond_axe));
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_SOFT, new ItemStack(Items.wooden_shovel));
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_SOFT, new ItemStack(Items.stone_shovel));
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_SOFT, new ItemStack(Items.iron_shovel));
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_SOFT, new ItemStack(Items.golden_shovel));
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SHOVEL_SOFT, new ItemStack(Items.diamond_shovel));
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_SOFT, new ItemStack(Items.wooden_hoe));
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_SOFT, new ItemStack(Items.stone_hoe));
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_SOFT, new ItemStack(Items.iron_hoe));
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_SOFT, new ItemStack(Items.golden_hoe));
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_HOE_SOFT, new ItemStack(Items.diamond_hoe));
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_SOFT, new ItemStack(Items.wooden_sword));
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_SOFT, new ItemStack(Items.stone_sword));
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_SOFT, new ItemStack(Items.iron_sword));
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_SOFT, new ItemStack(Items.golden_sword));
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SWORD_SOFT, new ItemStack(Items.diamond_sword));
    }

    FMLControlledNamespacedRegistry<Block> reg = GameData.getBlockRegistry();
    for(Object obj:reg)
    {
      Block block = (Block)obj;
      if(block instanceof BlockStairs)
      {

        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_STAIRS_SOFT, new ItemStack(block, 1, -1));
      } else if(block instanceof BlockSlab && !block.isOpaqueCube())
      {
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_SLAB_SOFT, new ItemStack(block, 1, -1));
      }
    }
    
    for(String name:LiquidMetalRegistry.instance.GetFluidNames())
    {
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_GEAR_SOFT, "gear" + name);
    }
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_GEAR_SOFT, "gearWood");
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_GEAR_SOFT, "gearDiamond");
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_GEAR_SOFT, "gearStone");

    //Ingot and block mold crafting recipes
    for(String name:OreDictionary.getOreNames())
    {
      if(name.startsWith("ingot"))
      {
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_INGOT_SOFT, name);
      } else if(name.startsWith("block"))
      {
        FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_BLOCK_SOFT, name);
      }
    }

    //Ore -> ingot furnace recipes
    FoundryMiscUtils.RegisterOreSmelting(BlockFoundryOre.ORE_COPPER,ItemIngot.INGOT_COPPER);
    FoundryMiscUtils.RegisterOreSmelting(BlockFoundryOre.ORE_TIN,ItemIngot.INGOT_TIN);
    FoundryMiscUtils.RegisterOreSmelting(BlockFoundryOre.ORE_ZINC,ItemIngot.INGOT_ZINC);
    FoundryMiscUtils.RegisterOreSmelting(BlockFoundryOre.ORE_NICKEL,ItemIngot.INGOT_NICKEL);
    FoundryMiscUtils.RegisterOreSmelting(BlockFoundryOre.ORE_SILVER,ItemIngot.INGOT_SILVER);
    
    //Clay mold furnace recipes
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_BLOCK_SOFT,ItemMold.MOLD_BLOCK);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_INGOT_SOFT,ItemMold.MOLD_INGOT);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_CABLE_IC2_SOFT,ItemMold.MOLD_CABLE_IC2);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_CASING_IC2_SOFT,ItemMold.MOLD_CASING_IC2);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_SLAB_SOFT,ItemMold.MOLD_SLAB);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_STAIRS_SOFT,ItemMold.MOLD_STAIRS);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_PLATE_IC2_SOFT,ItemMold.MOLD_PLATE_IC2);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_GEAR_SOFT,ItemMold.MOLD_GEAR);
    FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_INSULATED_CABLE_IC2_SOFT,ItemMold.MOLD_INSULATED_CABLE_IC2);

    if(FoundryConfig.recipe_tools_armor)
    {
      FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_CHESTPLATE_SOFT,ItemMold.MOLD_CHESTPLATE);
      FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_PICKAXE_SOFT,ItemMold.MOLD_PICKAXE);
      FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_AXE_SOFT,ItemMold.MOLD_AXE);
      FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_SHOVEL_SOFT,ItemMold.MOLD_SHOVEL);
      FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_SWORD_SOFT,ItemMold.MOLD_SWORD);
      FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_HOE_SOFT,ItemMold.MOLD_HOE);
      FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_LEGGINGS_SOFT,ItemMold.MOLD_LEGGINGS);
      FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_HELMET_SOFT,ItemMold.MOLD_HELMET);
      FoundryMiscUtils.RegisterMoldSmelting(ItemMold.MOLD_BOOTS_SOFT,ItemMold.MOLD_BOOTS);
    }

  }

  static public void PostInit()
  {
    if(OreDictionary.getOres("gearStone").size() == 0)
    {
      ItemStack cobble_stack = new ItemStack(Blocks.cobblestone, 1, -1);
      ItemStack stick_stack = new ItemStack(Items.stick);

      ItemStack gear_stack = new ItemStack(FoundryItems.item_component,1,ItemFoundryComponent.COMPONENT_GEAR);
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
          MaterialRegistry.instance.RegisterItem(item, material.suffix, type.name);
        }
      }
    }
  }
}
