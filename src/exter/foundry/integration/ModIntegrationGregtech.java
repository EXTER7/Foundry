package exter.foundry.integration;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import exter.foundry.api.recipe.FoundryRecipes;
import exter.foundry.block.BlockFoundryMachine;
import exter.foundry.block.FoundryBlocks;
import exter.foundry.config.FoundryConfig;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemFoundryComponent;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.manager.AlloyRecipeManager;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.registry.LiquidMetalRegistry;
import exter.foundry.util.FoundryMiscUtils;
import forestry.api.core.ItemInterface;

public class ModIntegrationGregtech extends ModIntegration
{
  
  public ModIntegrationGregtech(String mod_name)
  {
    super(mod_name);
  }

  @Override
  public void OnPreInit(Configuration config)
  {
    LiquidMetalRegistry.instance.RegisterLiquidMetal(config, "Cupronickel", 1750, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal(config, "StainlessSteel", 1900, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal(config, "Kanthal", 1900, 15);
    LiquidMetalRegistry.instance.RegisterLiquidMetal(config, "Nichrome", 1950, 15);
  }

  @Override
  public void OnInit()
  {
    if(!Loader.isModLoaded("gregtech_addon"))
    {
      is_loaded = false;
      return;
    }
    ItemStack iron_stack = new ItemStack(Item.ingotIron);
    ItemStack redstone_stack = new ItemStack(Item.redstone);
    ItemStack furnace_stack = new ItemStack(Block.furnaceIdle);
    ItemStack crucible_stack = new ItemStack(FoundryBlocks.block_foundry_crucible);
    ItemStack piston_stack = new ItemStack(Block.pistonBase);
    ItemStack foundrybrick_stack = new ItemStack(FoundryItems.item_component,1,ItemFoundryComponent.COMPONENT_FOUNDRYBRICK);
    ItemStack glasspane_stack = new ItemStack(Block.thinGlass);
    ItemStack emptycontainer2_stack = FoundryItems.item_container.EmptyContainer(2);

    GameRegistry.addRecipe(new ShapedOreRecipe(
        new ItemStack(FoundryBlocks.block_machine,1,BlockFoundryMachine.MACHINE_ICF),
        "IFI",
        "HCH",
        "HRH",
        'F', furnace_stack, 
        'I', "ingotCopper", 
        'C', crucible_stack,
        'R', redstone_stack,
        'H', "craftingHeatingCoilTier00"));

    GameRegistry.addRecipe(new ShapedOreRecipe(
        crucible_stack, 
        "IBI",
        "B B",
        "IBI",
        'I', "plateSteel",
        'B', foundrybrick_stack));
    
    GameRegistry.addRecipe(
        new ItemStack(FoundryBlocks.block_machine,1,BlockFoundryMachine.MACHINE_CASTER),
        " R ",
        "ICI",
        "IPI",
        'I', iron_stack, 
        'P', piston_stack,
        'C', crucible_stack,
        'R', redstone_stack);

    GameRegistry.addRecipe(new ShapedOreRecipe(
        new ItemStack(FoundryBlocks.block_machine,1,BlockFoundryMachine.MACHINE_INFUSER),
        "IRI",
        "GCG",
        "HRH",
        'I', iron_stack, 
        'R', redstone_stack, 
        'B', foundrybrick_stack,
        'C', crucible_stack,
        'G', "gearStone",
        'H', "craftingHeatingCoilTier00"));

    GameRegistry.addRecipe(new ShapedOreRecipe(emptycontainer2_stack,
        " T ",
        "BGB",
        " T ",
        'T', "plateTin", 
        'B', foundrybrick_stack,
        'G', glasspane_stack));
    
    Fluid liquid_copper = LiquidMetalRegistry.instance.GetFluid("Copper");
    Fluid liquid_nickel = LiquidMetalRegistry.instance.GetFluid("Nickel");
    Fluid liquid_iron = LiquidMetalRegistry.instance.GetFluid("Iron");
    Fluid liquid_manganese = LiquidMetalRegistry.instance.GetFluid("Manganese");
    Fluid liquid_chromium = LiquidMetalRegistry.instance.GetFluid("Chromium");
    Fluid liquid_aluminum = LiquidMetalRegistry.instance.GetFluid("Aluminum");
    
    Fluid liquid_stainless_steel = LiquidMetalRegistry.instance.GetFluid("StainlessSteel");
    Fluid liquid_cupronickel = LiquidMetalRegistry.instance.GetFluid("Cupronickel");
    Fluid liquid_kanthal = LiquidMetalRegistry.instance.GetFluid("Kanthal");
    Fluid liquid_nichrome = LiquidMetalRegistry.instance.GetFluid("Nichrome");

    AlloyRecipeManager.instance.AddRecipe(new FluidStack(liquid_stainless_steel,18),
        new FluidStack[] {
          new FluidStack(liquid_iron,12),
          new FluidStack(liquid_nickel,2),
          new FluidStack(liquid_manganese,2),
          new FluidStack(liquid_chromium,2)
    });

    AlloyRecipeManager.instance.AddRecipe(new FluidStack(liquid_cupronickel,12),
        new FluidStack[] {
          new FluidStack(liquid_copper,6),
          new FluidStack(liquid_nickel,6)
    });

    AlloyRecipeManager.instance.AddRecipe(new FluidStack(liquid_kanthal,12),
        new FluidStack[] {
          new FluidStack(liquid_iron,4),
          new FluidStack(liquid_aluminum,4),
          new FluidStack(liquid_chromium,4)
    });

    AlloyRecipeManager.instance.AddRecipe(new FluidStack(liquid_nichrome,15),
        new FluidStack[] {
          new FluidStack(liquid_nickel,12),
          new FluidStack(liquid_chromium,3)
    });
    
    ItemStack ingot_mold = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_INGOT);
    ItemStack block_mold = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_BLOCK);
    
    CastingRecipeManager.instance.AddRecipe("ingotStainlessSteel", new FluidStack(liquid_stainless_steel,FoundryRecipes.FLUID_AMOUNT_INGOT), ingot_mold, null);
    CastingRecipeManager.instance.AddRecipe("ingotCupronickel", new FluidStack(liquid_cupronickel,FoundryRecipes.FLUID_AMOUNT_INGOT), ingot_mold, null);
    CastingRecipeManager.instance.AddRecipe("ingotKanthal", new FluidStack(liquid_kanthal,FoundryRecipes.FLUID_AMOUNT_INGOT), ingot_mold, null);
    CastingRecipeManager.instance.AddRecipe("ingotNichrome", new FluidStack(liquid_nichrome,FoundryRecipes.FLUID_AMOUNT_INGOT), ingot_mold, null);

    CastingRecipeManager.instance.AddRecipe("blockStainlessSteel", new FluidStack(liquid_stainless_steel,FoundryRecipes.FLUID_AMOUNT_BLOCK), block_mold, null);
    CastingRecipeManager.instance.AddRecipe("blockCupronickel", new FluidStack(liquid_cupronickel,FoundryRecipes.FLUID_AMOUNT_BLOCK), block_mold, null);
    CastingRecipeManager.instance.AddRecipe("blockKanthal", new FluidStack(liquid_kanthal,FoundryRecipes.FLUID_AMOUNT_BLOCK), block_mold, null);
    CastingRecipeManager.instance.AddRecipe("blockNichrome", new FluidStack(liquid_nichrome,FoundryRecipes.FLUID_AMOUNT_BLOCK), block_mold, null);
  }
  
  @Override
  public void OnPostInit()
  {
    for(String name:LiquidMetalRegistry.instance.GetFluidNames())
    {
      Fluid fluid = LiquidMetalRegistry.instance.GetFluid(name);
      RegisterMetalRecipes(name, fluid);
    }
    RegisterMetalRecipes("Chrome",LiquidMetalRegistry.instance.GetFluid("Chromium"));
  }

  private void RegisterMetalRecipes(String partial_name, Fluid fluid)
  {
    ItemStack plate_mold = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_PLATE_IC2);
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PLATE_IC2_CLAY, "plate" + partial_name);
    MeltingRecipeManager.instance.AddRecipe("plate" + partial_name, new FluidStack(fluid,FoundryRecipes.FLUID_AMOUNT_INGOT));
    MeltingRecipeManager.instance.AddRecipe("dustSmall" + partial_name, new FluidStack(fluid,FoundryRecipes.FLUID_AMOUNT_INGOT / 4));
    MeltingRecipeManager.instance.AddRecipe("dustTiny" + partial_name, new FluidStack(fluid,FoundryRecipes.FLUID_AMOUNT_INGOT / 9));
    CastingRecipeManager.instance.AddRecipe("plate" + partial_name, new FluidStack(fluid,FoundryRecipes.FLUID_AMOUNT_INGOT), plate_mold, null);
  }
}
