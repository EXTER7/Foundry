package exter.foundry.integration;

import cpw.mods.fml.common.Loader;
import exter.foundry.api.FoundryUtils;
import exter.foundry.recipes.FoundryRecipes;
import exter.foundry.recipes.manager.AlloyMixerRecipeManager;
import exter.foundry.registry.LiquidMetalRegistry;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class ModIntegrationMekanism extends ModIntegration
{
  private Fluid liquid_osmium;
  private Fluid liquid_refined_glowstone;
  
  public ModIntegrationMekanism(String mod_name)
  {
    super(mod_name);
  }

  @Override
  public void OnPreInit(Configuration config)
  {
    liquid_osmium = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Osmium", 3200, 15);
    liquid_refined_glowstone = LiquidMetalRegistry.instance.RegisterLiquidMetal( "RefinedGlowstone", 3200, 15);
    FoundryUtils.RegisterBasicMeltingRecipes("Osmium", liquid_osmium);
    FoundryUtils.RegisterBasicMeltingRecipes("RefinedGlowstone", liquid_refined_glowstone);
  }

  @Override
  public void OnInit()
  {

  }

  @Override
  public void OnPostInit()
  {
    if(!Loader.isModLoaded("Mekanism"))
    {
      is_loaded = false;
      return;
    }
    
    Fluid liquid_glowstone = FluidRegistry.getFluid("glowstone");
    if(liquid_glowstone != null)
    {
      AlloyMixerRecipeManager.instance.AddRecipe(
          new FluidStack(liquid_refined_glowstone, 54),
          new FluidStack[] {
            new FluidStack(liquid_osmium, 54),
            new FluidStack(liquid_glowstone, 125)
          });
    }
  }

}
