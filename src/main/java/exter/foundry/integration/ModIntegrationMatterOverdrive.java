package exter.foundry.integration;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import exter.foundry.api.FoundryAPI;
import exter.foundry.api.FoundryUtils;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemMold;
import exter.foundry.material.MaterialRegistry;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.registry.LiquidMetalRegistry;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class ModIntegrationMatterOverdrive extends ModIntegration
{
  private Fluid liquid_tritanium;
  
  public ModIntegrationMatterOverdrive(String mod_name)
  {
    super(mod_name);
  }

  @Override
  public void OnPreInit(Configuration config)
  {
    liquid_tritanium = LiquidMetalRegistry.instance.RegisterLiquidMetal( "Tritanium", 4000, 14);    
    FoundryUtils.RegisterBasicMeltingRecipes("Tritanium", liquid_tritanium);
  }

  @Override
  public void OnInit()
  {

  }

  @Override
  public void OnPostInit()
  {
    if(!Loader.isModLoaded("mo"))
    {
      is_loaded = false;
      return;
    }
    
    ItemStack plate = new ItemStack(GameRegistry.findItem("mo","tritanium_plate"));
    ItemStack plate_mold = FoundryItems.Mold(ItemMold.MOLD_PLATE);
    FluidStack plate_fluid = new FluidStack(liquid_tritanium,FoundryAPI.FLUID_AMOUNT_INGOT * 3);
    
    MeltingRecipeManager.instance.AddRecipe(plate, plate_fluid);
    CastingRecipeManager.instance.AddRecipe(plate, plate_fluid, plate_mold, null);
    FoundryMiscUtils.RegisterMoldRecipe(ItemMold.MOLD_PLATE_SOFT, plate);
    
    MaterialRegistry.instance.RegisterItem(plate, "Tritanium", "Plate");
  }
}
