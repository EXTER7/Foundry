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
  }

  @Override
  public void OnPostInit()
  {
    ItemStack plate_mold = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_PLATE_IC2);
    for(String name:LiquidMetalRegistry.instance.GetFluidNames())
    {
      FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PLATE_IC2_CLAY, "plate" + name);
      
      Fluid fluid = LiquidMetalRegistry.instance.GetFluid(name);
      MeltingRecipeManager.instance.AddRecipe("plate" + name, new FluidStack(fluid,FoundryRecipes.FLUID_AMOUNT_INGOT));
      MeltingRecipeManager.instance.AddRecipe("dustSmall" + name, new FluidStack(fluid,FoundryRecipes.FLUID_AMOUNT_INGOT / 4));
      MeltingRecipeManager.instance.AddRecipe("dustTiny" + name, new FluidStack(fluid,FoundryRecipes.FLUID_AMOUNT_INGOT / 9));
      
      CastingRecipeManager.instance.AddRecipe("plate" + name, new FluidStack(fluid,FoundryRecipes.FLUID_AMOUNT_INGOT), plate_mold, null);
    }
  }
}
