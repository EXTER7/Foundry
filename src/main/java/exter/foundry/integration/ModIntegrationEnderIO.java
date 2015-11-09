package exter.foundry.integration;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import exter.foundry.api.FoundryAPI;
import exter.foundry.api.FoundryUtils;
import exter.foundry.api.substance.InfuserSubstance;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.FoundryRecipes;
import exter.foundry.recipes.manager.AlloyMixerRecipeManager;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.InfuserRecipeManager;
import exter.foundry.registry.LiquidMetalRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class ModIntegrationEnderIO extends ModIntegration
{
  private Fluid liquid_redstone_alloy;
  private Fluid liquid_energetic_alloy;
  private Fluid liquid_vibrant_alloy;
  private Fluid liquid_dark_steel;
  
  public ModIntegrationEnderIO(String mod_name)
  {
    super(mod_name);
  }

  @Override
  public void OnPreInit(Configuration config)
  {
    liquid_redstone_alloy = LiquidMetalRegistry.instance.RegisterLiquidMetal( "RedstoneAlloy", 1000, 14);    
    liquid_energetic_alloy = LiquidMetalRegistry.instance.RegisterLiquidMetal( "EnergeticAlloy", 2500, 15);    
    liquid_vibrant_alloy = LiquidMetalRegistry.instance.RegisterLiquidMetal( "VibrantAlloy", 2500, 15);    
    liquid_dark_steel = LiquidMetalRegistry.instance.RegisterLiquidMetal( "DarkSteel", 1850, 12);    
    
    FoundryUtils.RegisterBasicMeltingRecipes( "RedstoneAlloy", liquid_redstone_alloy);    
    FoundryUtils.RegisterBasicMeltingRecipes( "EnergeticAlloy", liquid_energetic_alloy);    
    FoundryUtils.RegisterBasicMeltingRecipes( "VibrantAlloy", liquid_vibrant_alloy);    
    FoundryUtils.RegisterBasicMeltingRecipes( "PhasedGold", liquid_vibrant_alloy);    
    FoundryUtils.RegisterBasicMeltingRecipes( "DarkSteel", liquid_dark_steel);    
  }

  @Override
  public void OnInit()
  {
    
  }

  @Override
  public void OnPostInit()
  {
    if(!Loader.isModLoaded("EnderIO"))
    {
      is_loaded = false;
      return;
    }
    Fluid liquid_redstone = LiquidMetalRegistry.instance.GetFluid("Redstone");
    Fluid destabilized_redstone = FluidRegistry.getFluid("redstone");
    Fluid liquid_ender = FluidRegistry.getFluid("ender");
    Fluid liquid_glowstone = FluidRegistry.getFluid("glowstone");
    
    InfuserRecipeManager.instance.AddSubstanceRecipe(
        new InfuserSubstance("silicon",36),
        new ItemStack(GameRegistry.findItem("EnderIO","itemMaterial"),1,0), 2400);
    InfuserRecipeManager.instance.AddSubstanceRecipe(
        new InfuserSubstance("silicon",36),
        new ItemStack(Blocks.sand,1,0), 240000);

    InfuserRecipeManager.instance.AddRecipe(
        new FluidStack(liquid_redstone_alloy,3),
        new FluidStack(liquid_redstone,3),
        new InfuserSubstance("silicon", 1));

    if(destabilized_redstone != null)
    {
      InfuserRecipeManager.instance.AddRecipe(
          new FluidStack(liquid_redstone_alloy,27),
          new FluidStack(destabilized_redstone,25),
          new InfuserSubstance("silicon", 9));
    }
    
    if(liquid_glowstone != null)
    {
      AlloyMixerRecipeManager.instance.AddRecipe(
          new FluidStack(liquid_energetic_alloy, 54),
          new FluidStack[] {
            new FluidStack(FoundryRecipes.liquid_gold, 54),
            new FluidStack(liquid_redstone, 54),
            new FluidStack(liquid_glowstone, 125)
          });
      
      if(destabilized_redstone != null)
      {
        AlloyMixerRecipeManager.instance.AddRecipe(
            new FluidStack(liquid_energetic_alloy, 54),
            new FluidStack[] {
              new FluidStack(FoundryRecipes.liquid_gold, 54),
              new FluidStack(destabilized_redstone, 50),
              new FluidStack(liquid_glowstone, 125)
            });
      }
    }

    if(liquid_ender != null)
    {
      AlloyMixerRecipeManager.instance.AddRecipe(
          new FluidStack(liquid_vibrant_alloy, 54),
          new FluidStack[] {
            new FluidStack(liquid_energetic_alloy, 54),
            new FluidStack(liquid_ender, 125)
          });
    }

    AlloyMixerRecipeManager.instance.AddRecipe(
        new FluidStack(liquid_dark_steel, 27),
        new FluidStack[] {
          new FluidStack(FoundryRecipes.liquid_steel, 27),
          new FluidStack(FluidRegistry.LAVA, 250),
        });
    
    CastingRecipeManager.instance.AddRecipe(
        "ingotPhasedGold",
        new FluidStack(liquid_vibrant_alloy,FoundryAPI.FLUID_AMOUNT_INGOT), FoundryItems.Mold(ItemMold.MOLD_INGOT), null);
    CastingRecipeManager.instance.AddRecipe(
        "blockPhasedGold",
        new FluidStack(liquid_vibrant_alloy,FoundryAPI.FLUID_AMOUNT_BLOCK), FoundryItems.Mold(ItemMold.MOLD_BLOCK), null);
  }
}
