package exter.foundry.integration;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import exter.foundry.api.recipe.FoundryRecipes;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.manager.AlloyRecipeManager;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.registry.LiquidMetalRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;


public class ModIntegrationRedstoneArsenal extends ModIntegration
{
  
  static public final int ITEM_ELECTRUMFLUX_INGOT = 0;
  static public final int ITEM_ELECTRUMFLUX_BLOCK = 1;
  
  public ModIntegrationRedstoneArsenal(String mod_name)
  {
    super(mod_name);
  }


  @Override
  public void OnPreInit(Configuration config)
  {
    LiquidMetalRegistry.instance.RegisterLiquidMetal(config, "ElectrumFlux", 1500, 14);
  }


  @Override
  public void OnInit()
  {
    ModIntegration te3 = GetIntegration("te3");
    if(!Loader.isModLoaded("Redstone Arsenal") || te3 == null || !te3.is_loaded)
    {
      is_loaded = false;
      return;
    }

    items = new ItemStack[2];

    items[ITEM_ELECTRUMFLUX_INGOT] = GameRegistry.findItemStack("Redstone Arsenal", "ingotElectrumFlux", 1);
    items[ITEM_ELECTRUMFLUX_BLOCK] = GameRegistry.findItemStack("Redstone Arsenal", "blockElectrumFlux", 1);

    VerifyItems();

    if(is_loaded)
    {
      Fluid liquid_redstone = FluidRegistry.getFluid("redstone");
      Fluid liquid_gold = LiquidMetalRegistry.instance.GetFluid("Gold");
      Fluid liquid_silver = LiquidMetalRegistry.instance.GetFluid("Silver");
      Fluid liquid_electrum = LiquidMetalRegistry.instance.GetFluid("Electrum");
      Fluid liquid_electrumflux = LiquidMetalRegistry.instance.GetFluid("ElectrumFlux");

      AlloyRecipeManager.instance.AddRecipe(new FluidStack(liquid_electrumflux, 27), new FluidStack[] { new FluidStack(liquid_electrum, 27), new FluidStack(liquid_redstone, 50) });

      AlloyRecipeManager.instance.AddRecipe(new FluidStack(liquid_electrumflux, 54), new FluidStack[] { new FluidStack(liquid_gold, 27), new FluidStack(liquid_silver, 27), new FluidStack(liquid_redstone, 100) });

    
      ItemStack mold_ingot = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_INGOT);
      ItemStack mold_block = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_BLOCK);
      
      CastingRecipeManager.instance.AddRecipe(items[ITEM_ELECTRUMFLUX_INGOT], new FluidStack(liquid_electrumflux, FoundryRecipes.FLUID_AMOUNT_INGOT), mold_ingot, null);
      CastingRecipeManager.instance.AddRecipe(items[ITEM_ELECTRUMFLUX_BLOCK], new FluidStack(liquid_electrumflux, FoundryRecipes.FLUID_AMOUNT_BLOCK), mold_block, null);

    }
  }


  @Override
  public void OnPostInit()
  {

  }
}