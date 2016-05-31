package exter.foundry.recipes;

import java.util.List;
import java.util.Map;


import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import exter.foundry.api.FoundryAPI;
import exter.foundry.api.FoundryUtils;
import exter.foundry.api.recipe.ICastingTableRecipe;
import exter.foundry.api.recipe.IMeltingRecipe;
import exter.foundry.api.recipe.matcher.IItemMatcher;
import exter.foundry.api.recipe.matcher.ItemStackMatcher;
import exter.foundry.api.recipe.matcher.OreMatcher;
import exter.foundry.block.FoundryBlocks;
import exter.foundry.block.BlockCastingTable.EnumTable;
import exter.foundry.block.BlockComponent;
import exter.foundry.block.BlockFoundryMachine.EnumMachine;
import exter.foundry.config.FoundryConfig;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemComponent;
import exter.foundry.item.ItemMold;
import exter.foundry.material.MaterialRegistry;
import exter.foundry.material.OreDictMaterial;
import exter.foundry.material.OreDictType;
import exter.foundry.recipes.manager.AlloyFurnaceRecipeManager;
import exter.foundry.recipes.manager.AlloyMixerRecipeManager;
import exter.foundry.recipes.manager.AtomizerRecipeManager;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.CastingTableRecipeManager;
import exter.foundry.recipes.manager.InfuserRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.recipes.manager.MoldRecipeManager;
import exter.foundry.registry.FluidLiquidMetal;
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
  static public Fluid liquid_platinum;
  static public Fluid liquid_signalum;
  static public Fluid liquid_lumium;
  static public Fluid liquid_enderium;

  static public void preInit()
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
    liquid_platinum = LiquidMetalRegistry.instance.registerLiquidMetal( "Platinum", 2100, 15);
    liquid_signalum = LiquidMetalRegistry.instance.registerLiquidMetal( "Signalum", 1400, 12);
    liquid_lumium = LiquidMetalRegistry.instance.registerLiquidMetal( "Lumium", 1250, 15);
    liquid_enderium = LiquidMetalRegistry.instance.registerLiquidMetal( "Enderium", 1900, 12);

    LiquidMetalRegistry.instance.registerLiquidMetal( "Aluminum", 1100, 15);  
    LiquidMetalRegistry.instance.registerLiquidMetal( "Chromium", 2200, 8);   
    LiquidMetalRegistry.instance.registerLiquidMetal( "Manganese", 1550, 15);   
    LiquidMetalRegistry.instance.registerLiquidMetal( "Titanium", 2000, 15);
    LiquidMetalRegistry.instance.registerLiquidMetal( "Rubber", 460, 0);
    LiquidMetalRegistry.instance.registerLiquidMetal( "StainlessSteel", 1900, 15);
    LiquidMetalRegistry.instance.registerLiquidMetal( "Kanthal", 1900, 15);
    LiquidMetalRegistry.instance.registerLiquidMetal( "Nichrome", 1950, 15);

    
    for(String name:LiquidMetalRegistry.instance.getFluidNames())
    {
      FluidLiquidMetal fluid = LiquidMetalRegistry.instance.getFluid(name);
      if(!fluid.special)
      {
        FoundryUtils.registerBasicMeltingRecipes(name,fluid);
      }
    }
    FoundryUtils.registerBasicMeltingRecipes("Chrome",LiquidMetalRegistry.instance.getFluid("Chromium"));
    FoundryUtils.registerBasicMeltingRecipes("Aluminium",LiquidMetalRegistry.instance.getFluid("Aluminum"));
    
    if(FoundryConfig.recipe_glass)
    {
      //final String[] oredict_names = { "dyeBlack", "dyeRed", "dyeGreen", "dyeBrown", "dyeBlue", "dyePurple", "dyeCyan", "dyeLightGray", "dyeGray", "dyePink", "dyeLime", "dyeYellow", "dyeLightBlue", "dyeMagenta", "dyeOrange", "dyeWhite" };

      int temp = 1550;
      Fluid liquid_glass = LiquidMetalRegistry.instance.registerSpecialLiquidMetal("Glass", temp, 12, new ItemStack(Blocks.GLASS));
      MeltingRecipeManager.instance.addRecipe(new ItemStackMatcher(Blocks.SAND), new FluidStack(liquid_glass,1000),temp,250);
      MeltingRecipeManager.instance.addRecipe(new ItemStackMatcher(Blocks.GLASS), new FluidStack(liquid_glass,1000),temp,250);
      MeltingRecipeManager.instance.addRecipe(new ItemStackMatcher(Blocks.GLASS_PANE), new FluidStack(liquid_glass,375),temp,250);
      CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(Blocks.GLASS), new FluidStack(liquid_glass,1000),FoundryItems.mold(ItemMold.SubItem.BLOCK),null,400);
      /*
      for(EnumDyeColor dye:EnumDyeColor.values())
      {
        String name = dye.getName();
        int color = ItemDye.dyeColors[dye.getDyeDamage()];
        int c1 = 63 + (color & 0xFF) * 3 / 4;
        int c2 = 63 + ((color >> 8 ) & 0xFF) * 3 / 4;
        int c3 = 63 + ((color >> 16) & 0xFF) * 3 / 4;
        int fluid_color = c1 | (c2 << 8) | (c3 << 16);
        
        int meta = dye.getMetadata();
        ItemStack stained_glass = new ItemStack(Blocks.stained_glass,1,meta);

        Fluid liquid_glass_colored = LiquidMetalRegistry.instance.registerSpecialLiquidMetal("Glass." + name, temp, 12, "liquidGlass", fluid_color, stained_glass);

        MeltingRecipeManager.instance.addRecipe(stained_glass, new FluidStack(liquid_glass_colored,1000),temp,250);
        MeltingRecipeManager.instance.addRecipe(new ItemStack(Blocks.stained_glass_pane,1,meta), new FluidStack(liquid_glass_colored,375),temp,250);
        CastingRecipeManager.instance.addRecipe(stained_glass, new FluidStack(liquid_glass_colored,1000),FoundryItems.mold(ItemMold.SubItem.BLOCK),null,400);
        
        InfuserRecipeManager.instance.addSubstanceRecipe(new InfuserSubstance("dye." + name,200), oredict_names[dye.getDyeDamage()], 25000);
        InfuserRecipeManager.instance.addRecipe(new FluidStack(liquid_glass_colored,40),new FluidStack(liquid_glass,40),new InfuserSubstance("dye." + name,1));
      }*/
    }
  }

  static public void init()
  {
    Fluid liquid_redstone = FluidRegistry.getFluid("liquidredstone");
    Fluid liquid_glowstone = FluidRegistry.getFluid("liquidglowstone");
    Fluid liquid_enderpearl = FluidRegistry.getFluid("liquidenderpearl");
    
    
    MeltingRecipeManager.instance.addRecipe(new OreMatcher("dustRedstone"), new FluidStack(liquid_redstone,100));
    MeltingRecipeManager.instance.addRecipe(new OreMatcher("dustGlowstone"), new FluidStack(liquid_glowstone,250),liquid_glowstone.getTemperature(),90);
    MeltingRecipeManager.instance.addRecipe(new OreMatcher("dustEnderpearl"), new FluidStack(liquid_enderpearl,250),liquid_enderpearl.getTemperature(),75);
    MeltingRecipeManager.instance.addRecipe(new ItemStackMatcher(Items.ENDER_PEARL), new FluidStack(liquid_enderpearl,250),liquid_enderpearl.getTemperature(),75);

    MeltingRecipeManager.instance.addRecipe(new OreMatcher("blockRedstone"), new FluidStack(liquid_redstone,900));
    MeltingRecipeManager.instance.addRecipe(new OreMatcher("blockGlowstone"), new FluidStack(liquid_glowstone,1000),liquid_glowstone.getTemperature(),90);

    MoldRecipeManager.instance.addRecipe(FoundryItems.mold(ItemMold.SubItem.INGOT), 2, 4, new int[]
        {
            2, 2,
            2, 2,
            2, 2,
            2, 2
        });

    MoldRecipeManager.instance.addRecipe(FoundryItems.mold(ItemMold.SubItem.PLATE), 4, 4, new int[]
        {
            1, 1, 1, 1,
            1, 1, 1, 1,
            1, 1, 1, 1,
            1, 1, 1, 1
        });

    MoldRecipeManager.instance.addRecipe(FoundryItems.mold(ItemMold.SubItem.ROD), 1, 6, new int[]
        {
            1,
            1,
            1,
            1,
            1,
            1
        });

    MoldRecipeManager.instance.addRecipe(FoundryItems.mold(ItemMold.SubItem.GEAR), 5, 5, new int[]
        {
            1, 0, 1, 0, 1,
            0, 1, 1, 1, 0,
            1, 1, 1, 1, 1,
            0, 1, 1, 1, 0,
            1, 0, 1, 0, 1
        });

    MoldRecipeManager.instance.addRecipe(FoundryItems.mold(ItemMold.SubItem.BLOCK), 6, 6, new int[]
        {
            4, 4, 4, 4, 4, 4,
            4, 4, 4, 4, 4, 4,
            4, 4, 4, 4, 4, 4,
            4, 4, 4, 4, 4, 4,
            4, 4, 4, 4, 4, 4,
            4, 4, 4, 4, 4, 4
        });

    MoldRecipeManager.instance.addRecipe(FoundryItems.mold(ItemMold.SubItem.SLAB), 6, 6, new int[]
        {
            0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0,
            4, 4, 4, 4, 4, 4,
            4, 4, 4, 4, 4, 4,
            4, 4, 4, 4, 4, 4
        });

    MoldRecipeManager.instance.addRecipe(FoundryItems.mold(ItemMold.SubItem.STAIRS), 6, 6, new int[]
        {
            0, 0, 0, 4, 4, 4,
            0, 0, 0, 4, 4, 4,
            0, 0, 0, 4, 4, 4,
            4, 4, 4, 4, 4, 4,
            4, 4, 4, 4, 4, 4,
            4, 4, 4, 4, 4, 4
        });

    MoldRecipeManager.instance.addRecipe(FoundryItems.mold(ItemMold.SubItem.BULLET), 3, 3, new int[]
        {
            0, 1, 0,
            1, 2, 1,
            0, 1, 0,
        });

    MoldRecipeManager.instance.addRecipe(FoundryItems.mold(ItemMold.SubItem.BULLET_HOLLOW), 3, 3, new int[]
        {
            0, 2, 0,
            2, 1, 2,
            0, 2, 0,
        });

    MoldRecipeManager.instance.addRecipe(FoundryItems.mold(ItemMold.SubItem.ROUND_CASING), 4, 4, new int[]
        {
            0, 2, 2, 0,
            2, 1, 1, 2,
            2, 1, 1, 2,
            0, 1, 2, 0
        });

    MoldRecipeManager.instance.addRecipe(FoundryItems.mold(ItemMold.SubItem.PELLET), 1, 1, new int[]
        {
            1
        });

    MoldRecipeManager.instance.addRecipe(FoundryItems.mold(ItemMold.SubItem.SHELL_CASING), 5, 5, new int[]
        {
            0, 0, 3, 0, 0,
            0, 3, 1, 3, 0,
            3, 1, 1, 1, 3,
            0, 3, 1, 3, 0,
            0, 0, 3, 0, 0
        });

    MoldRecipeManager.instance.addRecipe(FoundryItems.mold(ItemMold.SubItem.GUN_BARREL), 3, 3, new int[]
        {
            0, 4, 0,
            4, 0, 4,
            0, 4, 0,
        });

    MoldRecipeManager.instance.addRecipe(FoundryItems.mold(ItemMold.SubItem.REVOLVER_DRUM), 5, 5, new int[]
        {
            0, 3, 3, 3, 0,
            3, 0, 3, 0, 3,
            3, 3, 3, 3, 3,
            3, 0, 3, 0, 3,
            0, 3, 3, 3, 0
        });

    MoldRecipeManager.instance.addRecipe(FoundryItems.mold(ItemMold.SubItem.REVOLVER_FRAME), 5, 6, new int[]
        {
            0, 2, 2, 0, 0,
            0, 2, 2, 2, 0,
            2, 2, 2, 2, 0,
            0, 2, 2, 2, 2,
            0, 0, 2, 2, 2,
            0, 0, 2, 2, 2
        });

    MoldRecipeManager.instance.addRecipe(FoundryItems.mold(ItemMold.SubItem.SHOTGUN_PUMP), 3, 3, new int[]
        {
            4, 0, 4,
            4, 0, 4,
            4, 4, 4,
        });

    MoldRecipeManager.instance.addRecipe(FoundryItems.mold(ItemMold.SubItem.SHOTGUN_FRAME), 6, 5, new int[]
        {
            0, 2, 2, 0, 0, 0,
            0, 2, 2, 2, 2, 0,
            0, 2, 2, 2, 2, 2,
            2, 2, 2, 2, 2, 2,
            0, 0, 0, 2, 2, 2,
        });

    AlloyFurnaceRecipeManager.instance.addRecipe(
        new ItemStack(FoundryBlocks.block_refractory_glass),
        new ItemStackMatcher(Blocks.SAND),
        new ItemStackMatcher(Items.CLAY_BALL));

    AlloyFurnaceRecipeManager.instance.addRecipe(
        FoundryMiscUtils.getModItemFromOreDictionary("substratum", "ingotBronze", 4),
        new IItemMatcher[] {
            new OreMatcher("ingotCopper", 3),
            new OreMatcher("dustCopper", 3) },
        new IItemMatcher[] {
            new OreMatcher("ingotTin", 1),
            new OreMatcher("dustTin", 1) }
        );
    
    AlloyFurnaceRecipeManager.instance.addRecipe(
        FoundryMiscUtils.getModItemFromOreDictionary("substratum", "ingotBrass", 4),
        new IItemMatcher[] {
            new OreMatcher("ingotCopper", 3),
            new OreMatcher("dustCopper", 3) },
        new IItemMatcher[] {
            new OreMatcher("ingotZinc", 1),
            new OreMatcher("dustZinc", 1) }
        );
    
    AlloyFurnaceRecipeManager.instance.addRecipe(
        FoundryMiscUtils.getModItemFromOreDictionary("substratum", "ingotInvar", 3),
        new IItemMatcher[] {
            new OreMatcher("ingotIron", 2),
            new OreMatcher("dustIron", 2) },
        new IItemMatcher[] {
            new OreMatcher("ingotNickel", 1),
            new OreMatcher("dustNickel", 1) }
        );

    AlloyFurnaceRecipeManager.instance.addRecipe(
        FoundryMiscUtils.getModItemFromOreDictionary("substratum", "ingotElectrum", 2),
        new IItemMatcher[] {
            new OreMatcher("ingotGold", 1),
            new OreMatcher("dustGold", 1) },
        new IItemMatcher[] {
            new OreMatcher("ingotSilver", 1),
            new OreMatcher("dustSilver", 1) }
        );
    
    AlloyFurnaceRecipeManager.instance.addRecipe(
        FoundryMiscUtils.getModItemFromOreDictionary("substratum", "ingotCupronickel", 2),
        new IItemMatcher[] {
            new OreMatcher("ingotCopper", 1),
            new OreMatcher("dustCopper", 1) },
        new IItemMatcher[] {
            new OreMatcher("ingotNickel", 1),
            new OreMatcher("dustNickel", 1) }
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

    AlloyMixerRecipeManager.instance.addRecipe(
        new FluidStack(liquid_signalum, 108),
        new FluidStack[] {
          new FluidStack(FoundryRecipes.liquid_copper, 81),
          new FluidStack(FoundryRecipes.liquid_silver, 27),
          new FluidStack(liquid_redstone, 250)
          });
    
    AlloyMixerRecipeManager.instance.addRecipe(
        new FluidStack(liquid_lumium, 108),
        new FluidStack[] {
          new FluidStack(FoundryRecipes.liquid_tin, 81),
          new FluidStack(FoundryRecipes.liquid_silver, 27),
          new FluidStack(liquid_glowstone, 250)
          });
    
    AlloyMixerRecipeManager.instance.addRecipe(
        new FluidStack(liquid_enderium, 108),
        new FluidStack[] {
          new FluidStack(FoundryRecipes.liquid_tin, 54),
          new FluidStack(FoundryRecipes.liquid_silver, 27),
          new FluidStack(liquid_platinum, 27),
          new FluidStack(liquid_enderpearl, 250)
          });


    ItemStack mold_ingot = FoundryItems.mold(ItemMold.SubItem.INGOT);
    ItemStack mold_slab = FoundryItems.mold(ItemMold.SubItem.SLAB);
    ItemStack mold_stairs = FoundryItems.mold(ItemMold.SubItem.STAIRS);
    ItemStack mold_plate = FoundryItems.mold(ItemMold.SubItem.PLATE);
    ItemStack mold_block = FoundryItems.mold(ItemMold.SubItem.BLOCK);
    ItemStack mold_gear = FoundryItems.mold(ItemMold.SubItem.GEAR);
    ItemStack mold_rod = FoundryItems.mold(ItemMold.SubItem.ROD);
    ItemStack extra_sticks1 = new ItemStack(Items.STICK,1);
    ItemStack extra_sticks2 = new ItemStack(Items.STICK,2);


    for(ItemMold.SubItem sub:ItemMold.SubItem.values())
    {
      CastingRecipeManager.instance.addMold(FoundryItems.mold(sub));
    }

    if(FoundryConfig.recipe_tools_armor)
    {
      ItemStack mold_chestplate = FoundryItems.mold(ItemMold.SubItem.CHESTPLATE);
      ItemStack mold_pickaxe = FoundryItems.mold(ItemMold.SubItem.PICKAXE);
      ItemStack mold_axe = FoundryItems.mold(ItemMold.SubItem.AXE);
      ItemStack mold_shovel = FoundryItems.mold(ItemMold.SubItem.SHOVEL);
      ItemStack mold_hoe = FoundryItems.mold(ItemMold.SubItem.HOE);
      ItemStack mold_sword = FoundryItems.mold(ItemMold.SubItem.SWORD);
      ItemStack mold_leggings = FoundryItems.mold(ItemMold.SubItem.LEGGINGS);
      ItemStack mold_helmet = FoundryItems.mold(ItemMold.SubItem.HELMET);
      ItemStack mold_boots = FoundryItems.mold(ItemMold.SubItem.BOOTS);

      MoldRecipeManager.instance.addRecipe(mold_helmet, 4, 3, new int[]
          {
              3, 3, 3, 3,
              3, 1, 1, 3,
              3, 1, 1, 3
          });

      MoldRecipeManager.instance.addRecipe(mold_chestplate, 6, 6, new int[]
          {
              3, 1, 0, 0, 1, 3,
              3, 1, 0, 0, 1, 3,
              3, 1, 1, 1, 1, 3,
              3, 1, 1, 1, 1, 3,
              3, 1, 1, 1, 1, 3,
              3, 1, 1, 1, 1, 3
          });
      MoldRecipeManager.instance.addRecipe(mold_leggings, 6, 6, new int[]
          {
              3, 1, 1, 1, 1, 3,
              3, 1, 1, 1, 1, 3,
              3, 1, 0, 0, 1, 3,
              3, 1, 0, 0, 1, 3,
              3, 1, 0, 0, 1, 3,
              3, 1, 0, 0, 1, 3
          });
      MoldRecipeManager.instance.addRecipe(mold_boots, 6, 6, new int[]
          {
              0, 0, 0, 0, 0, 0,
              0, 0, 0, 0, 0, 0,
              0, 0, 0, 0, 0, 0,
              2, 3, 0, 0, 3, 2,
              3, 3, 0, 0, 3, 3,
              3, 3, 0, 0, 3, 3
          });

      MoldRecipeManager.instance.addRecipe(mold_pickaxe, 5, 5, new int[]
          {
              0, 2, 2, 2, 0,
              1, 0, 1, 0, 1,
              0, 0, 1, 0, 0,
              0, 0, 1, 0, 0,
              0, 0, 1, 0, 0
          });

      MoldRecipeManager.instance.addRecipe(mold_axe, 3, 5, new int[]
          {
              1, 2, 2,
              1, 2, 1,
              1, 0, 1,
              0, 0, 1,
              0, 0, 1
          });

      MoldRecipeManager.instance.addRecipe(mold_shovel, 3, 6, new int[]
          {
              0, 1, 0,
              1, 1, 1,
              1, 1, 1,
              0, 1, 0,
              0, 1, 0,
              0, 1, 0
          });

      MoldRecipeManager.instance.addRecipe(mold_hoe, 3, 5, new int[]
          {
              0, 2, 2,
              1, 0, 1,
              0, 0, 1,
              0, 0, 1,
              0, 0, 1,
          });

      MoldRecipeManager.instance.addRecipe(mold_sword, 3, 6, new int[]
          {
              0, 1, 0,
              0, 1, 0,
              0, 1, 0,
              0, 1, 0,
              1, 2, 1,
              0, 1, 0,
          });


      CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(Items.IRON_CHESTPLATE), new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 8), mold_chestplate, null);
      CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(Items.GOLDEN_CHESTPLATE), new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 8), mold_chestplate, null);

      CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(Items.IRON_PICKAXE), new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_pickaxe, new ItemStackMatcher(extra_sticks2));
      CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(Items.GOLDEN_PICKAXE), new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_pickaxe, new ItemStackMatcher(extra_sticks2));

      CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(Items.IRON_AXE), new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_axe, new ItemStackMatcher(extra_sticks2));
      CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(Items.GOLDEN_AXE), new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 3), mold_axe, new ItemStackMatcher(extra_sticks2));

      CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(Items.IRON_SHOVEL), new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 1), mold_shovel, new ItemStackMatcher(extra_sticks2));
      CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(Items.GOLDEN_SHOVEL), new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 1), mold_shovel, new ItemStackMatcher(extra_sticks2));

      CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(Items.IRON_SWORD), new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_sword, new ItemStackMatcher(extra_sticks1));
      CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(Items.GOLDEN_SWORD), new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_sword, new ItemStackMatcher(extra_sticks1));

      CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(Items.IRON_HOE), new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_hoe, new ItemStackMatcher(extra_sticks2));
      CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(Items.GOLDEN_HOE), new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 2), mold_hoe, new ItemStackMatcher(extra_sticks2));

      CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(Items.IRON_LEGGINGS), new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 7), mold_leggings, null);
      CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(Items.GOLDEN_LEGGINGS), new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 7), mold_leggings, null);

      CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(Items.IRON_HELMET), new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 5), mold_helmet, null);
      CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(Items.GOLDEN_HELMET), new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 5), mold_helmet, null);

      CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(Items.IRON_BOOTS), new FluidStack(liquid_iron, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_boots, null);
      CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(Items.GOLDEN_BOOTS), new FluidStack(liquid_gold, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_boots, null);

    }
    
    //Base casting recipes.
    for(String name:LiquidMetalRegistry.instance.getFluidNames())
    {
      FluidLiquidMetal fluid = LiquidMetalRegistry.instance.getFluid(name);

      if(fluid.special)
      {
        continue;
      }
      
      // Ingot
      ItemStack ingot = FoundryMiscUtils.getModItemFromOreDictionary("substratum", "ingot" + name);
      if(ingot != null)
      {
        FluidStack fluid_stack = new FluidStack( fluid, FoundryAPI.FLUID_AMOUNT_INGOT);
        CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(ingot), fluid_stack, mold_ingot, null);
        CastingTableRecipeManager.instance.addRecipe(new ItemStackMatcher(ingot), fluid_stack, ICastingTableRecipe.TableType.INGOT);
      }

      // Block
      ItemStack block = FoundryMiscUtils.getModItemFromOreDictionary("substratum", "block" + name);
      if(block != null)
      {
        CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(block), new FluidStack(fluid, FoundryAPI.FLUID_AMOUNT_BLOCK), mold_block, null);
      }

      // Slab
      ItemStack slab = FoundryMiscUtils.getModItemFromOreDictionary("substratum", "slab" + name);
      if(slab != null)
      {
        FluidStack fluid_stack = new FluidStack(fluid, FoundryAPI.FLUID_AMOUNT_BLOCK / 2);

        CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(slab), fluid_stack, mold_slab, null);
        MeltingRecipeManager.instance.addRecipe(new ItemStackMatcher(slab), fluid_stack);
      }
      
      // Stairs
      ItemStack stairs = FoundryMiscUtils.getModItemFromOreDictionary("substratum", "stairs" + name);
      if(stairs != null)
      {
        FluidStack fluid_stack = new FluidStack(fluid, FoundryAPI.FLUID_AMOUNT_BLOCK * 3 / 4);
        CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(stairs), fluid_stack, mold_stairs, null);
        MeltingRecipeManager.instance.addRecipe(new ItemStackMatcher(stairs), fluid_stack);
      }

      // Dust
      ItemStack dust = FoundryMiscUtils.getModItemFromOreDictionary("substratum", "dust" + name);
      if(dust != null)
      {
        AtomizerRecipeManager.instance.addRecipe(new ItemStackMatcher(dust), new FluidStack(fluid, FoundryAPI.FLUID_AMOUNT_INGOT));
      }
      
      // Gear
      ItemStack gear = FoundryMiscUtils.getModItemFromOreDictionary("substratum", "gear" + name);
      if(gear != null)
      {
        FluidStack fluid_stack = new FluidStack(fluid, FoundryAPI.FLUID_AMOUNT_INGOT * 4);
        CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(gear), fluid_stack, mold_gear, null);
        MeltingRecipeManager.instance.addRecipe(new ItemStackMatcher(gear), fluid_stack);
      }

      // Plate
      ItemStack plate = FoundryMiscUtils.getModItemFromOreDictionary("substratum", "plate" + name);
      if(plate != null)
      {
        FluidStack fluid_stack = new FluidStack(fluid, FoundryAPI.FLUID_AMOUNT_INGOT);

        CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(plate), fluid_stack, mold_plate, null);
        CastingTableRecipeManager.instance.addRecipe(new ItemStackMatcher(plate), fluid_stack, ICastingTableRecipe.TableType.PLATE);
        MeltingRecipeManager.instance.addRecipe(new ItemStackMatcher(plate), fluid_stack);
      }

      // Rod
      ItemStack rod = FoundryMiscUtils.getModItemFromOreDictionary("substratum", "rod" + name);
      if(rod != null)
      {
        FluidStack fluid_stack = new FluidStack(fluid, FoundryAPI.FLUID_AMOUNT_INGOT / 2);

        CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(rod), fluid_stack, mold_rod, null);
        CastingTableRecipeManager.instance.addRecipe(new ItemStackMatcher(rod), fluid_stack, ICastingTableRecipe.TableType.ROD);
        MeltingRecipeManager.instance.addRecipe(new ItemStackMatcher(rod), fluid_stack);
      }
    }

    if(FoundryConfig.recipe_steel_enable)
    {
      InfuserRecipeManager.instance.addRecipe(new FluidStack(liquid_steel,36), new FluidStack(liquid_iron,36), new OreMatcher("dustCoal"), 160000);
      InfuserRecipeManager.instance.addRecipe(new FluidStack(liquid_steel,12), new FluidStack(liquid_iron,12), new OreMatcher("dustCharcoal"), 160000);
      InfuserRecipeManager.instance.addRecipe(new FluidStack(liquid_steel,9), new FluidStack(liquid_iron,9), new OreMatcher("dustSmallCoal"), 20000);
      InfuserRecipeManager.instance.addRecipe(new FluidStack(liquid_steel,3), new FluidStack(liquid_iron,3), new OreMatcher("dustSmallCharcoal"), 20000);
    }
    
    
    ItemStack bullet = FoundryItems.component(ItemComponent.SubItem.AMMO_BULLET);
    ItemStack bullet_hollow = FoundryItems.component(ItemComponent.SubItem.AMMO_BULLET_HOLLOW);
    ItemStack bullet_jacketed = FoundryItems.component(ItemComponent.SubItem.AMMO_BULLET_JACKETED);
    ItemStack bullet_casing = FoundryItems.component(ItemComponent.SubItem.AMMO_CASING);
    ItemStack pellet = FoundryItems.component(ItemComponent.SubItem.AMMO_PELLET);
    ItemStack shell_casing = FoundryItems.component(ItemComponent.SubItem.AMMO_CASING_SHELL);
    ItemStack gun_barrel = FoundryItems.component(ItemComponent.SubItem.GUN_BARREL);
    ItemStack revolver_drum = FoundryItems.component(ItemComponent.SubItem.REVOLVER_DRUM);
    ItemStack revolver_frame = FoundryItems.component(ItemComponent.SubItem.REVOLVER_FRAME);
    ItemStack shotgun_pump = FoundryItems.component(ItemComponent.SubItem.SHOTGUN_PUMP);
    ItemStack shotgun_frame = FoundryItems.component(ItemComponent.SubItem.SHOTGUN_FRAME);
    ItemStack bullet_steel = FoundryItems.component(ItemComponent.SubItem.AMMO_BULLET_STEEL);
    ItemStack pellet_steel = FoundryItems.component(ItemComponent.SubItem.AMMO_PELLET_STEEL);
    ItemStack bullet_lumium = FoundryItems.component(ItemComponent.SubItem.AMMO_BULLET_LUMIUM);
    ItemStack pellet_lumium = FoundryItems.component(ItemComponent.SubItem.AMMO_PELLET_LUMIUM);
    ItemStack mold_bullet = FoundryItems.mold(ItemMold.SubItem.BULLET);
    ItemStack mold_bullet_hollow = FoundryItems.mold(ItemMold.SubItem.BULLET_HOLLOW);
    ItemStack mold_bullet_casing = FoundryItems.mold(ItemMold.SubItem.ROUND_CASING);
    ItemStack mold_pellet = FoundryItems.mold(ItemMold.SubItem.PELLET);
    ItemStack mold_shell_casing = FoundryItems.mold(ItemMold.SubItem.SHELL_CASING);
    ItemStack mold_gun_barrel = FoundryItems.mold(ItemMold.SubItem.GUN_BARREL);
    ItemStack mold_revolver_drum = FoundryItems.mold(ItemMold.SubItem.REVOLVER_DRUM);
    ItemStack mold_revolver_frame = FoundryItems.mold(ItemMold.SubItem.REVOLVER_FRAME);
    ItemStack mold_shotgun_pump = FoundryItems.mold(ItemMold.SubItem.SHOTGUN_PUMP);
    ItemStack mold_shotgun_frame = FoundryItems.mold(ItemMold.SubItem.SHOTGUN_FRAME);

       
    MeltingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(bullet),
        new FluidStack(liquid_lead, FoundryAPI.FLUID_AMOUNT_NUGGET * 3));
    MeltingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(bullet_hollow),
        new FluidStack(liquid_lead, FoundryAPI.FLUID_AMOUNT_NUGGET * 3));
    MeltingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(bullet_jacketed),
        new FluidStack(liquid_copper, FoundryAPI.FLUID_AMOUNT_NUGGET));
    MeltingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(bullet_casing),
        new FluidStack(liquid_brass, FoundryAPI.FLUID_AMOUNT_NUGGET));
    MeltingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(pellet),
        new FluidStack(liquid_lead, FoundryAPI.FLUID_AMOUNT_NUGGET));
    MeltingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(shell_casing),
        new FluidStack(liquid_brass, FoundryAPI.FLUID_AMOUNT_NUGGET * 2));
    MeltingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(bullet_steel),
        new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_NUGGET * 3));
    MeltingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(pellet_steel),
        new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_NUGGET));
    MeltingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(bullet_lumium),
        new FluidStack(liquid_lumium, FoundryAPI.FLUID_AMOUNT_NUGGET * 3));
    MeltingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(pellet_lumium),
        new FluidStack(liquid_lumium, FoundryAPI.FLUID_AMOUNT_NUGGET));


    MeltingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(gun_barrel),
        new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT));
    MeltingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(revolver_drum),
        new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 3 / 4));
    MeltingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(revolver_frame),
        new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 3 / 2));
    MeltingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(shotgun_pump),
        new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT / 2));
    MeltingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(shotgun_frame),
        new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 3 / 2));


    CastingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(bullet),
        new FluidStack(liquid_lead, FoundryAPI.FLUID_AMOUNT_NUGGET * 3), mold_bullet, null);
    CastingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(bullet_hollow),
        new FluidStack(liquid_lead, FoundryAPI.FLUID_AMOUNT_NUGGET * 3), mold_bullet_hollow, null);
    CastingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(bullet_jacketed),
        new FluidStack(liquid_copper, FoundryAPI.FLUID_AMOUNT_NUGGET), mold_bullet, new ItemStackMatcher(bullet));
    CastingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(bullet_casing),
        new FluidStack(liquid_brass, FoundryAPI.FLUID_AMOUNT_NUGGET), mold_bullet_casing, null);
    CastingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(pellet),
        new FluidStack(liquid_lead, FoundryAPI.FLUID_AMOUNT_NUGGET), mold_pellet, null);
    CastingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(shell_casing),
        new FluidStack(liquid_brass, FoundryAPI.FLUID_AMOUNT_NUGGET * 2), mold_shell_casing, null);
    CastingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(bullet_steel),
        new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_NUGGET * 3), mold_bullet, null);
    CastingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(pellet_steel),
        new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_NUGGET), mold_pellet, null);
    CastingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(bullet_lumium),
        new FluidStack(liquid_lumium, FoundryAPI.FLUID_AMOUNT_NUGGET * 3), mold_bullet, null);
    CastingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(pellet_lumium),
        new FluidStack(liquid_lumium, FoundryAPI.FLUID_AMOUNT_NUGGET), mold_pellet, null);

    
    CastingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(gun_barrel),
        new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT), mold_gun_barrel, null);
    CastingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(revolver_drum),
        new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 3 / 4), mold_revolver_drum, null);
    CastingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(revolver_frame),
        new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 3 / 2), mold_revolver_frame, null);
    CastingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(shotgun_pump),
        new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT / 2), mold_shotgun_pump, null);
    CastingRecipeManager.instance.addRecipe(
        new ItemStackMatcher(shotgun_frame),
        new FluidStack(liquid_steel, FoundryAPI.FLUID_AMOUNT_INGOT * 3 / 2), mold_shotgun_frame, null);


    ItemStack redstone_stack = new ItemStack(Items.REDSTONE);
    ItemStack furnace_stack = new ItemStack(Blocks.FURNACE);
    ItemStack clay_stack = new ItemStack(Items.CLAY_BALL);
    ItemStack sand_stack = new ItemStack(Blocks.SAND,1,-1);
    ItemStack soulsand_stack = new ItemStack(Blocks.SOUL_SAND);
    ItemStack clayblock_stack = new ItemStack(Blocks.CLAY, 1, -1);
    ItemStack casing_stack = FoundryBlocks.block_component.asItemStack(BlockComponent.EnumVariant.REFCASING);
    ItemStack casing_inferno_stack = FoundryBlocks.block_component.asItemStack(BlockComponent.EnumVariant.INFCASING);
    ItemStack piston_stack = new ItemStack(Blocks.PISTON);
    ItemStack goldnugget_stack = new ItemStack(Items.GOLD_NUGGET);
    ItemStack cauldron_stack = new ItemStack(Items.CAULDRON);
    ItemStack chest_stack = new ItemStack(Blocks.CHEST);
    ItemStack paper_stack = new ItemStack(Items.PAPER);
    ItemStack refclay_stack = FoundryItems.component(ItemComponent.SubItem.REFRACTORYCLAY);
    ItemStack refclay_small_stack = FoundryItems.component(ItemComponent.SubItem.REFRACTORYCLAY_SMALL);
    ItemStack refractoryclay8_stack = FoundryItems.component(ItemComponent.SubItem.REFRACTORYCLAY,8);
    ItemStack refbrick_stack = FoundryItems.component(ItemComponent.SubItem.REFRACTORYBRICK);
    ItemStack refglass_stack = new ItemStack(FoundryBlocks.block_refractory_glass);
    ItemStack heatingcoil_stack = FoundryItems.component(ItemComponent.SubItem.HEATINGCOIL);
    ItemStack emptycontainer2_stack = FoundryItems.item_container.empty(2);
    ItemStack comparator_stack = new ItemStack(Items.COMPARATOR);
    ItemStack repeater_stack = new ItemStack(Items.REPEATER);
    ItemStack bucket_stack = new ItemStack(Items.BUCKET);
    ItemStack magmacream_stack = new ItemStack(Items.MAGMA_CREAM);
    ItemStack infernoclay8_stack = FoundryItems.component(ItemComponent.SubItem.INFERNOCLAY,8);
    ItemStack infbrick_stack = FoundryItems.component(ItemComponent.SubItem.INFERNOBRICK);

    GameRegistry.addRecipe(refractoryclay8_stack,
        "CCC",
        "CSC",
        "CCC",
        'C', clay_stack,
        'S', sand_stack);

    GameRegistry.addRecipe(FoundryBlocks.block_component.asItemStack(BlockComponent.EnumVariant.REFCLAYBLOCK),
        "CC",
        "CC",
        'C', refclay_stack);

    GameRegistry.addRecipe(refclay_stack,
        "CC",
        "CC",
        'C', refclay_small_stack);

    GameRegistry.addRecipe(new ShapedOreRecipe(infernoclay8_stack,
        "COC",
        "CSC",
        "CMC",
        'C', refclay_stack,
        'S', soulsand_stack,
        'M', magmacream_stack,
        'O', "dustObsidian"));

    GameRegistry.addShapelessRecipe(refractoryclay8_stack,clayblock_stack, clayblock_stack, sand_stack);

    GameRegistry.addSmelting(
        FoundryItems.component(ItemComponent.SubItem.REFRACTORYCLAY),
        refbrick_stack, 0.0f);

    GameRegistry.addSmelting(
        FoundryItems.component(ItemComponent.SubItem.INFERNOCLAY),
        infbrick_stack, 0.0f);

    GameRegistry.addRecipe(new ShapedOreRecipe(
        emptycontainer2_stack,
        " T ",
        "BGB",
        " T ",
        'T', "plateTin",
        'B', refbrick_stack,
        'G', refglass_stack));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        FoundryItems.component(ItemComponent.SubItem.HEATINGCOIL,2),
        "CCC",
        "CRC",
        "CCC",
        'C', "rodCupronickel",
        'G', goldnugget_stack,
        'R', redstone_stack));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        casing_stack,
        "IBI",
        "B B",
        "IBI",
        'I', "plateIron",
        'B', refbrick_stack));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        casing_inferno_stack,
        "IBI",
        "B B",
        "IBI",
        'I', "plateSteel",
        'B', infbrick_stack));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        new ItemStack(FoundryBlocks.block_refractory_tank),
        "BPB",
        "G G",
        "BPB",
        'G', refglass_stack,
        'P', "plateIron",
        'B', refbrick_stack));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        new ItemStack(FoundryBlocks.block_inferno_tank),
        "BPB",
        "G G",
        "BPB",
        'G', refglass_stack,
        'P', "plateSteel",
        'B', infbrick_stack));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        new ItemStack(FoundryBlocks.block_burner_heater),
        "I",
        "C",
        "F",
        'F', furnace_stack,
        'I', "plateCopper",
        'C', casing_stack));

    GameRegistry.addRecipe(
        FoundryBlocks.block_casting_table.asItemStack(EnumTable.INGOT),
        "BMB",
        " S ",
        'S', new ItemStack(Blocks.STONE_SLAB),
        'B', refbrick_stack,
        'M', mold_ingot);

    GameRegistry.addRecipe(
        FoundryBlocks.block_casting_table.asItemStack(EnumTable.PLATE),
        "BMB",
        " S ",
        'S', new ItemStack(Blocks.STONE_SLAB),
        'B', refbrick_stack,
        'M', mold_plate);

    GameRegistry.addRecipe(
        FoundryBlocks.block_casting_table.asItemStack(EnumTable.ROD),
        "BMB",
        " S ",
        'S', new ItemStack(Blocks.STONE_SLAB),
        'B', refbrick_stack,
        'M', mold_rod);

    GameRegistry.addRecipe(new ShapedOreRecipe(
        FoundryBlocks.block_refractory_spout,
        "RL",
        "BB",
        "R ",
        'R', "rodIron",
        'B', refbrick_stack,
        'L', new ItemStack(Blocks.LEVER)));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        FoundryBlocks.block_machine.asItemStack(EnumMachine.INDUCTIONHEATER),
        "HIH",
        "RCR",
        "HRH",
        'H', heatingcoil_stack,
        'R', redstone_stack,
        'I', "plateCopper",
        'C', casing_stack));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        FoundryBlocks.block_machine.asItemStack(EnumMachine.CRUCIBLE),
        "BAB",
        "BCB",
        "BIB",
        'B', refbrick_stack,
        'I', "plateCopper",
        'C', casing_stack,
        'A', cauldron_stack));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        FoundryBlocks.block_machine.asItemStack(EnumMachine.ADVCRUCIBLE),
        "BAB",
        "BCB",
        "BIB",
        'B', infbrick_stack,
        'I', "plateSilver",
        'C', casing_inferno_stack,
        'A', cauldron_stack));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        FoundryBlocks.block_machine.asItemStack(EnumMachine.INFUSER),
        " R ",
        "GCG",
        "HRH",
        'R', redstone_stack, 
        'B', refbrick_stack,
        'C', casing_stack,
        'G', "gearInvar",
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
        'B', Items.BUCKET, 
        'R', Items.REDSTONE,
        'C', casing_stack,
        'G', "gearBronze"));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        FoundryBlocks.block_machine.asItemStack(EnumMachine.CASTER),
        " H ",
        "RCR",
        "GPG",
        'H', chest_stack, 
        'G', "gearIron", 
        'P', piston_stack,
        'C', casing_stack,
        'R', redstone_stack));

    
    GameRegistry.addRecipe(new ShapedOreRecipe(
        FoundryBlocks.block_machine.asItemStack(EnumMachine.ALLOYMIXER),
        " P ",
        "GCG",
        " R ",
        'C', casing_stack,
        'R', redstone_stack,
        'G', "gearInvar",
        'P', "plateInvar"));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        FoundryBlocks.block_machine.asItemStack(EnumMachine.MATERIALROUTER),
        "GEG",
        "PRP",
        "GCG",
        'R', casing_stack,
        'P', "plateSignalum",
        'C', comparator_stack,
        'E', repeater_stack,
        'G', "gearBrass"));

    GameRegistry.addRecipe(
        new ItemStack(FoundryBlocks.block_mold_station),
        "BWB",
        "BSB",
        "BFB",
        'B', refbrick_stack,
        'W', new ItemStack(Blocks.CRAFTING_TABLE),
        'S', new ItemStack(Blocks.STONE_SLAB),
        'F', furnace_stack);

    GameRegistry.addRecipe(
        FoundryItems.item_revolver.empty(),
        "BD",
        " F",
        'B', FoundryItems.component(ItemComponent.SubItem.GUN_BARREL), 
        'D', FoundryItems.component(ItemComponent.SubItem.REVOLVER_DRUM),
        'F', FoundryItems.component(ItemComponent.SubItem.REVOLVER_FRAME));

    GameRegistry.addRecipe(
        FoundryItems.item_shotgun.empty(),
        "BB ",
        " PF",
        'B', FoundryItems.component(ItemComponent.SubItem.GUN_BARREL), 
        'P', FoundryItems.component(ItemComponent.SubItem.SHOTGUN_PUMP),
        'F', FoundryItems.component(ItemComponent.SubItem.SHOTGUN_FRAME));



    GameRegistry.addRecipe(new ShapedOreRecipe(
        FoundryItems.item_round,
        "B",
        "G",
        "C",
        'B', FoundryItems.component(ItemComponent.SubItem.AMMO_BULLET), 
        'G', "dustSmallGunpowder",
        'C', FoundryItems.component(ItemComponent.SubItem.AMMO_CASING)));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        FoundryItems.item_round_hollow,
        "B",
        "G",
        "C",
        'B', FoundryItems.component(ItemComponent.SubItem.AMMO_BULLET_HOLLOW), 
        'G', "dustSmallGunpowder",
        'C', FoundryItems.component(ItemComponent.SubItem.AMMO_CASING)));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        FoundryItems.item_round_jacketed,
        "B",
        "G",
        "C",
        'B', FoundryItems.component(ItemComponent.SubItem.AMMO_BULLET_JACKETED), 
        'G', "dustSmallGunpowder",
        'C', FoundryItems.component(ItemComponent.SubItem.AMMO_CASING)));

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
        'P', FoundryItems.component(ItemComponent.SubItem.AMMO_PELLET), 
        'A', paper_stack, 
        'G', "dustSmallGunpowder",
        'C', FoundryItems.component(ItemComponent.SubItem.AMMO_CASING_SHELL)));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        FoundryItems.item_round_ap,
        "B",
        "G",
        "C",
        'B', FoundryItems.component(ItemComponent.SubItem.AMMO_BULLET_STEEL), 
        'G', "dustSmallGunpowder",
        'C', FoundryItems.component(ItemComponent.SubItem.AMMO_CASING)));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        FoundryItems.item_round_lumium,
        "B",
        "G",
        "C",
        'B', FoundryItems.component(ItemComponent.SubItem.AMMO_BULLET_LUMIUM), 
        'G', "dustSmallGunpowder",
        'C', FoundryItems.component(ItemComponent.SubItem.AMMO_CASING)));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        FoundryItems.item_shell_ap,
        "PAP",
        "PGP",
        "PCP",
        'P', FoundryItems.component(ItemComponent.SubItem.AMMO_PELLET_STEEL), 
        'A', paper_stack, 
        'G', "dustSmallGunpowder",
        'C', FoundryItems.component(ItemComponent.SubItem.AMMO_CASING_SHELL)));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        FoundryItems.item_shell_lumium,
        "PAP",
        "PGP",
        "PCP",
        'P', FoundryItems.component(ItemComponent.SubItem.AMMO_PELLET_LUMIUM), 
        'A', paper_stack, 
        'G', "dustSmallGunpowder",
        'C', FoundryItems.component(ItemComponent.SubItem.AMMO_CASING_SHELL)));

    GameRegistry.addShapelessRecipe(
        new ItemStack(FoundryItems.item_round_poison,2),
        Items.SPIDER_EYE, 
        FoundryItems.item_round_hollow,
        FoundryItems.item_round_hollow);
  }

  static public void postInit()
  {
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
    
    for(Map.Entry<ItemStack, ItemStack> entry:FurnaceRecipes.instance().getSmeltingList().entrySet())
    {
      ItemStack stack = entry.getKey();
      
      if(stack != null && stack.getItem() != null && MeltingRecipeManager.instance.findRecipe(stack) == null)
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
          MeltingRecipeManager.instance.addRecipe(new ItemStackMatcher(stack), new FluidStack(liquid_metal, base_amount * result.stackSize),recipe.getMeltingPoint(),recipe.getMeltingSpeed());
        }
      }
    }
    
    ItemStack ingot_mold = FoundryItems.mold(ItemMold.SubItem.INGOT);
    ItemStack block_mold = FoundryItems.mold(ItemMold.SubItem.BLOCK);
    for(String name:LiquidMetalRegistry.instance.getFluidNames())
    {
      FluidLiquidMetal fluid = LiquidMetalRegistry.instance.getFluid(name);
      if(!fluid.special)
      {
        FluidStack fluidstack = new FluidStack(fluid, FoundryAPI.FLUID_AMOUNT_INGOT);
        List<ItemStack> ores = OreDictionary.getOres("ingot" + name);
        if(ores != null && ores.size() > 0)
        {
          if(CastingRecipeManager.instance.findRecipe(fluidstack, ingot_mold, null) == null)
          {
            CastingRecipeManager.instance.addRecipe(new OreMatcher("ingot" + name), fluidstack, ingot_mold, null);
          }
          CastingTableRecipeManager.instance.addRecipe(new OreMatcher("ingot" + name), fluidstack, ICastingTableRecipe.TableType.INGOT);
        }
        ores = OreDictionary.getOres("dust" + name);
        if(ores != null && ores.size() > 0)
        {
          if(AtomizerRecipeManager.instance.findRecipe(fluidstack) == null)
          {
            AtomizerRecipeManager.instance.addRecipe(new OreMatcher("dust" + name), fluidstack);
          }
        }

        ores = OreDictionary.getOres("block" + name);
        fluidstack = new FluidStack(LiquidMetalRegistry.instance.getFluid(name), FoundryAPI.FLUID_AMOUNT_BLOCK);
        if(ores != null && ores.size() > 0)
        {
          if(CastingRecipeManager.instance.findRecipe(fluidstack, block_mold, null) == null)
          {
            CastingRecipeManager.instance.addRecipe(new OreMatcher("block" + name), fluidstack, block_mold, null);
          }
        }
      }
    }
  }
}
