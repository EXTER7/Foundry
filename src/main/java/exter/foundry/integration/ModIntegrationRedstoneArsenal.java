package exter.foundry.integration;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import exter.foundry.api.FoundryAPI;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.manager.AlloyMixerRecipeManager;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.registry.LiquidMetalRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;


public class ModIntegrationRedstoneArsenal extends ModIntegration
{
  public ModIntegrationRedstoneArsenal(String mod_name)
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
  }


  @Override
  public void OnPostInit()
  {
    ModIntegration mod_tf = GetIntegration("tf");
    if(!Loader.isModLoaded("RedstoneArsenal") || mod_tf == null || !mod_tf.is_loaded)
    {
      is_loaded = false;
      return;
    }


    ItemStack electrumflux_ingot = GameRegistry.findItemStack("RedstoneArsenal", "ingotElectrumFlux", 1);
    ItemStack electrumflux_block = GameRegistry.findItemStack("RedstoneArsenal", "blockElectrumFlux", 1);

    if(is_loaded)
    {
      Fluid destabilized_redstone = FluidRegistry.getFluid("redstone");
      Fluid liquid_redstone = LiquidMetalRegistry.instance.GetFluid("Redstone");
      Fluid liquid_gold = LiquidMetalRegistry.instance.GetFluid("Gold");
      Fluid liquid_silver = LiquidMetalRegistry.instance.GetFluid("Silver");
      Fluid liquid_electrum = LiquidMetalRegistry.instance.GetFluid("Electrum");
      Fluid liquid_electrumflux = LiquidMetalRegistry.instance.GetFluid("ElectrumFlux");

      AlloyMixerRecipeManager.instance.AddRecipe(
          new FluidStack(liquid_electrumflux, 27),
          new FluidStack[] {
            new FluidStack(liquid_electrum, 27),
            new FluidStack(destabilized_redstone, 50) });

      AlloyMixerRecipeManager.instance.AddRecipe(
          new FluidStack(liquid_electrumflux,5),
          new FluidStack[] {
            new FluidStack(liquid_electrum,5),
            new FluidStack(liquid_redstone,4)});

      
      AlloyMixerRecipeManager.instance.AddRecipe(
          new FluidStack(liquid_electrumflux, 54),
          new FluidStack[] {
            new FluidStack(liquid_gold, 27),
            new FluidStack(liquid_silver, 27),
            new FluidStack(destabilized_redstone, 100) });

      
      AlloyMixerRecipeManager.instance.AddRecipe(
          new FluidStack(liquid_electrumflux, 10),
          new FluidStack[] {
            new FluidStack(liquid_gold, 5),
            new FluidStack(liquid_silver, 5),
            new FluidStack(liquid_redstone, 8) });
    
      ItemStack mold_ingot = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_INGOT);
      ItemStack mold_block = new ItemStack(FoundryItems.item_mold, 1, ItemMold.MOLD_BLOCK);
      
      CastingRecipeManager.instance.AddRecipe(electrumflux_ingot, new FluidStack(liquid_electrumflux, FoundryAPI.FLUID_AMOUNT_INGOT), mold_ingot, null);
      CastingRecipeManager.instance.AddRecipe(electrumflux_block, new FluidStack(liquid_electrumflux, FoundryAPI.FLUID_AMOUNT_BLOCK), mold_block, null);

    }
  }
}