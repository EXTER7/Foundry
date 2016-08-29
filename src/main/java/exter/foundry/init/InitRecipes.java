package exter.foundry.init;

import java.util.List;
import java.util.Map;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
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
import exter.foundry.api.recipe.matcher.ItemStackMatcher;
import exter.foundry.api.recipe.matcher.OreMatcher;
import exter.foundry.block.FoundryBlocks;
import exter.foundry.block.BlockCastingTable.EnumTable;
import exter.foundry.block.BlockComponent;
import exter.foundry.block.BlockFoundryMachine.EnumMachine;
import exter.foundry.config.FoundryConfig;
import exter.foundry.fluid.FluidLiquidMetal;
import exter.foundry.fluid.FoundryFluids;
import exter.foundry.fluid.LiquidMetalRegistry;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemComponent;
import exter.foundry.item.ItemMold;
import exter.foundry.material.MaterialRegistry;
import exter.foundry.material.OreDictMaterial;
import exter.foundry.material.OreDictType;
import exter.foundry.recipes.manager.AtomizerRecipeManager;
import exter.foundry.recipes.manager.BurnerHeaterFuelManager;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.CastingTableRecipeManager;
import exter.foundry.recipes.manager.InfuserRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.recipes.manager.MoldRecipeManager;
import exter.foundry.util.FoundryMiscUtils;

public class InitRecipes
{
  static public void preInit()
  {
    for(String name:LiquidMetalRegistry.instance.getFluidNames())
    {
      FluidLiquidMetal fluid = LiquidMetalRegistry.instance.getFluid(name);
      if(!fluid.special)
      {
        FoundryUtils.registerBasicMeltingRecipes(name,fluid);
      }
    }
    FoundryUtils.registerBasicMeltingRecipes("Chromium",LiquidMetalRegistry.instance.getFluid("Chrome"));
    FoundryUtils.registerBasicMeltingRecipes("Aluminum",LiquidMetalRegistry.instance.getFluid("Aluminium"));

    if(FoundryConfig.recipe_glass)
    {
      final String[] oredict_names = { "dyeSmallBlack", "dyeSmallRed", "dyeSmallGreen", "dyeSmallBrown", "dyeSmallBlue", "dyeSmallPurple", "dyeSmallCyan", "dyeSmallLightGray", "dyeSmallGray", "dyeSmallPink", "dyeSmallLime", "dyeSmallYellow", "dyeSmallLightBlue", "dyeSmallMagenta", "dyeSmallOrange", "dyeSmallWhite" };

      int temp = 1550;
      Fluid liquid_glass = LiquidMetalRegistry.instance.registerSpecialLiquidMetal("Glass", temp, 12, new ItemStack(Blocks.GLASS));
      MeltingRecipeManager.instance.addRecipe(new ItemStackMatcher(Blocks.SAND), new FluidStack(liquid_glass,1000),temp,250);
      MeltingRecipeManager.instance.addRecipe(new ItemStackMatcher(Blocks.GLASS), new FluidStack(liquid_glass,1000),temp,250);
      MeltingRecipeManager.instance.addRecipe(new ItemStackMatcher(Blocks.GLASS_PANE), new FluidStack(liquid_glass,375),temp,250);
      CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(Blocks.GLASS), new FluidStack(liquid_glass,1000),FoundryItems.mold(ItemMold.SubItem.BLOCK),null,400);
      CastingTableRecipeManager.instance.addRecipe(new ItemStackMatcher(Blocks.GLASS), new FluidStack(liquid_glass,1000),ICastingTableRecipe.TableType.BLOCK);
      for(EnumDyeColor dye:EnumDyeColor.values())
      {
        String name = dye.getName();

        int color = ItemDye.DYE_COLORS[dye.getDyeDamage()];
        int c1 = 63 + (color & 0xFF) * 3 / 4;
        int c2 = 63 + ((color >> 8 ) & 0xFF) * 3 / 4;
        int c3 = 63 + ((color >> 16) & 0xFF) * 3 / 4;
        int fluid_color = c1 | (c2 << 8) | (c3 << 16);
        
        int meta = dye.getMetadata();
        ItemStack stained_glass = new ItemStack(Blocks.STAINED_GLASS,1,meta);

        Fluid liquid_glass_colored = LiquidMetalRegistry.instance.registerSpecialLiquidMetal("Glass." + name, temp, 12, "liquidGlass", fluid_color, stained_glass);

        MeltingRecipeManager.instance.addRecipe(new ItemStackMatcher(stained_glass), new FluidStack(liquid_glass_colored,1000),temp,250);
        MeltingRecipeManager.instance.addRecipe(new ItemStackMatcher(new ItemStack(Blocks.STAINED_GLASS_PANE,1,meta)), new FluidStack(liquid_glass_colored,375),temp,250);
        CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(stained_glass), new FluidStack(liquid_glass_colored,1000),FoundryItems.mold(ItemMold.SubItem.BLOCK),null,400);
        CastingTableRecipeManager.instance.addRecipe(new ItemStackMatcher(stained_glass), new FluidStack(liquid_glass_colored,1000),ICastingTableRecipe.TableType.BLOCK);

        InfuserRecipeManager.instance.addRecipe(new FluidStack(liquid_glass_colored,2000),new FluidStack(liquid_glass,2000),new OreMatcher(oredict_names[dye.getDyeDamage()]),5000);
      }
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

    InitAlloyRecipes.init();

    ItemStack mold_ingot = FoundryItems.mold(ItemMold.SubItem.INGOT);
    ItemStack mold_slab = FoundryItems.mold(ItemMold.SubItem.SLAB);
    ItemStack mold_stairs = FoundryItems.mold(ItemMold.SubItem.STAIRS);
    ItemStack mold_plate = FoundryItems.mold(ItemMold.SubItem.PLATE);
    ItemStack mold_block = FoundryItems.mold(ItemMold.SubItem.BLOCK);
    ItemStack mold_gear = FoundryItems.mold(ItemMold.SubItem.GEAR);
    ItemStack mold_rod = FoundryItems.mold(ItemMold.SubItem.ROD);


    for(ItemMold.SubItem sub:ItemMold.SubItem.values())
    {
      CastingRecipeManager.instance.addMold(FoundryItems.mold(sub));
    }

    if(FoundryConfig.recipe_equipment)
    {
      InitToolRecipes.init();
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
        FluidStack fluid_stack = new FluidStack(fluid, FoundryAPI.FLUID_AMOUNT_BLOCK);
        CastingRecipeManager.instance.addRecipe(new ItemStackMatcher(block), fluid_stack, mold_block, null);
        CastingTableRecipeManager.instance.addRecipe(new ItemStackMatcher(block), fluid_stack, ICastingTableRecipe.TableType.BLOCK);
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

    if(FoundryConfig.recipe_alumina_melts_to_aluminium)
    {
      MeltingRecipeManager.instance.addRecipe(new OreMatcher("ingotAlumina"), new FluidStack(FoundryFluids.liquid_aluminium, FoundryAPI.FLUID_AMOUNT_INGOT),2100);
      MeltingRecipeManager.instance.addRecipe(new OreMatcher("nuggetAlumina"), new FluidStack(FoundryFluids.liquid_aluminium, FoundryAPI.FLUID_AMOUNT_NUGGET),2100);
      MeltingRecipeManager.instance.addRecipe(new OreMatcher("dustAlumina"), new FluidStack(FoundryFluids.liquid_aluminium, FoundryAPI.FLUID_AMOUNT_INGOT),2100);
      MeltingRecipeManager.instance.addRecipe(new OreMatcher("oreAlumina"), new FluidStack(FoundryFluids.liquid_aluminium, FoundryAPI.FLUID_AMOUNT_ORE),2100);
    } else
    {
      InfuserRecipeManager.instance.addRecipe(new FluidStack(FoundryFluids.liquid_aluminium,216), new FluidStack(FoundryFluids.liquid_alumina,216), new OreMatcher("dustCoal"), 240000);
      InfuserRecipeManager.instance.addRecipe(new FluidStack(FoundryFluids.liquid_aluminium,216), new FluidStack(FoundryFluids.liquid_alumina,216), new OreMatcher("dustCharcoal"), 240000);
      InfuserRecipeManager.instance.addRecipe(new FluidStack(FoundryFluids.liquid_aluminium,54), new FluidStack(FoundryFluids.liquid_alumina,54), new OreMatcher("dustSmallCoal"), 60000);
      InfuserRecipeManager.instance.addRecipe(new FluidStack(FoundryFluids.liquid_aluminium,54), new FluidStack(FoundryFluids.liquid_iron,54), new OreMatcher("dustSmallCharcoal"), 60000);
    }
    
    BurnerHeaterFuelManager.instance.addFuel(
        new ItemStackMatcher(new ItemStack(Items.COAL,1,0)),// Coal
        1600,
        BurnerHeaterFuelManager.instance.getHeatNeeded(187000, FoundryAPI.CRUCIBLE_BASIC_TEMP_LOSS_RATE));

    BurnerHeaterFuelManager.instance.addFuel(
        new ItemStackMatcher(new ItemStack(Items.COAL,1,1)),// Charcoal
        1200,
        BurnerHeaterFuelManager.instance.getHeatNeeded(187000, FoundryAPI.CRUCIBLE_BASIC_TEMP_LOSS_RATE));

    BurnerHeaterFuelManager.instance.addFuel(
        new OreMatcher("itemCoalCoke"),
        3200,
        BurnerHeaterFuelManager.instance.getHeatNeeded(215000, FoundryAPI.CRUCIBLE_BASIC_TEMP_LOSS_RATE));

    BurnerHeaterFuelManager.instance.addFuel(
        new OreMatcher("dustCoal"),
        800,
        BurnerHeaterFuelManager.instance.getHeatNeeded(195000, FoundryAPI.CRUCIBLE_BASIC_TEMP_LOSS_RATE));
    BurnerHeaterFuelManager.instance.addFuel(
        new OreMatcher("dustCharcoal"),
        800,
        BurnerHeaterFuelManager.instance.getHeatNeeded(192000, FoundryAPI.CRUCIBLE_BASIC_TEMP_LOSS_RATE));
    BurnerHeaterFuelManager.instance.addFuel(
        new OreMatcher("dustSmallCoal"),
        200,
        BurnerHeaterFuelManager.instance.getHeatNeeded(195000, FoundryAPI.CRUCIBLE_BASIC_TEMP_LOSS_RATE));
    BurnerHeaterFuelManager.instance.addFuel(
        new OreMatcher("dustSmallCharcoal"),
        200,
        BurnerHeaterFuelManager.instance.getHeatNeeded(192000, FoundryAPI.CRUCIBLE_BASIC_TEMP_LOSS_RATE));

    BurnerHeaterFuelManager.instance.addFuel(
        new ItemStackMatcher(Items.BLAZE_ROD),
        2000,
        BurnerHeaterFuelManager.instance.getHeatNeeded(220000, FoundryAPI.CRUCIBLE_BASIC_TEMP_LOSS_RATE));

    ItemStack redstone_stack = new ItemStack(Items.REDSTONE);
    ItemStack furnace_stack = new ItemStack(Blocks.FURNACE);
    ItemStack clay_stack = new ItemStack(Items.CLAY_BALL);
    ItemStack sand_stack = new ItemStack(Blocks.SAND,1,-1);
    ItemStack soulsand_stack = new ItemStack(Blocks.SOUL_SAND);
    ItemStack clayblock_stack = new ItemStack(Blocks.CLAY, 1, -1);
    ItemStack casing_basic_stack = FoundryBlocks.block_component.asItemStack(BlockComponent.EnumVariant.CASING_BASIC);
    ItemStack casing_stack = FoundryBlocks.block_component.asItemStack(BlockComponent.EnumVariant.CASING_STANDARD);
    ItemStack casing_inferno_stack = FoundryBlocks.block_component.asItemStack(BlockComponent.EnumVariant.CASING_ADVANCED);
    ItemStack piston_stack = new ItemStack(Blocks.PISTON);
    ItemStack goldnugget_stack = new ItemStack(Items.GOLD_NUGGET);
    ItemStack bronze_cauldron_stack = new ItemStack(FoundryBlocks.block_cauldron_bronze);
    ItemStack cauldron_stack = new ItemStack(Items.CAULDRON);
    ItemStack chest_stack = new ItemStack(Blocks.CHEST);
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
        casing_basic_stack,
        "IBI",
        "B B",
        "IBI",
        'I', "plateBronze",
        'B', refbrick_stack));

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
        new ItemStack(FoundryBlocks.block_refractory_tank_basic),
        "BPB",
        "G G",
        "BPB",
        'G', refglass_stack,
        'P', "plateBronze",
        'B', refbrick_stack));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        new ItemStack(FoundryBlocks.block_refractory_tank_standard),
        "BPB",
        "G G",
        "BPB",
        'G', refglass_stack,
        'P', "plateIron",
        'B', refbrick_stack));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        new ItemStack(FoundryBlocks.block_refractory_tank_advanced),
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
        'C', casing_basic_stack));

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

    GameRegistry.addRecipe(
        FoundryBlocks.block_casting_table.asItemStack(EnumTable.BLOCK),
        "BMB",
        " S ",
        'S', new ItemStack(Blocks.STONE_SLAB),
        'B', refbrick_stack,
        'M', mold_block);

    GameRegistry.addRecipe(new ShapedOreRecipe(
        FoundryBlocks.block_refractory_spout,
        "RL",
        "BB",
        "R ",
        'R', "rodBronze",
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
        FoundryBlocks.block_machine.asItemStack(EnumMachine.CRUCIBLE_BASIC),
        "BAB",
        "BCB",
        "BIB",
        'B', refbrick_stack,
        'I', "plateCopper",
        'C', casing_basic_stack,
        'A', bronze_cauldron_stack));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        FoundryBlocks.block_machine.asItemStack(EnumMachine.CRUCIBLE_STANDARD),
        "BAB",
        "BCB",
        "BIB",
        'B', refbrick_stack,
        'I', "plateCopper",
        'C', casing_stack,
        'A', cauldron_stack));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        FoundryBlocks.block_machine.asItemStack(EnumMachine.CRUCIBLE_ADVANCED),
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
        FoundryBlocks.block_machine.asItemStack(EnumMachine.ALLOYING_CRUCIBLE),
        "HRH",
        "BCB",
        "BBB",
        'H', new ItemStack(FoundryBlocks.block_refractory_hopper), 
        'B', Items.BUCKET, 
        'R', cauldron_stack,
        'C', casing_stack));

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

    GameRegistry.addRecipe(new ShapedOreRecipe(
        new ItemStack(FoundryBlocks.block_cauldron_bronze),
        "I I",
        "I I",
        "III",
        'I', "ingotBronze"));

    if(FoundryConfig.block_cokeoven)
    {
      GameRegistry.addRecipe(new ShapedOreRecipe(
          new ItemStack(FoundryBlocks.block_coke_oven),
          "BFB",
          "BCB",
          "BIB",
          'B', refbrick_stack,
          'F', furnace_stack,
          'I', "plateCopper",
          'C', casing_stack));
    }
    
    InitFirearmRecipes.init();
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
        if(material.suffix_alias != null)
        {
          od_name = type.prefix + material.suffix_alias;
          for(ItemStack item:OreDictionary.getOres(od_name))
          {
            MaterialRegistry.instance.registerItem(item, material.suffix, type.name);
          }
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
          if(CastingTableRecipeManager.instance.findRecipe(fluidstack,ICastingTableRecipe.TableType.INGOT) == null)
          {
            CastingTableRecipeManager.instance.addRecipe(new OreMatcher("ingot" + name), fluidstack, ICastingTableRecipe.TableType.INGOT);
          }
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
          if(CastingTableRecipeManager.instance.findRecipe(fluidstack,ICastingTableRecipe.TableType.BLOCK) == null)
          {
            CastingTableRecipeManager.instance.addRecipe(new OreMatcher("block" + name), fluidstack, ICastingTableRecipe.TableType.BLOCK);
          }
        }
      }
    }
      
    InitHardCore.init();
  }
}
