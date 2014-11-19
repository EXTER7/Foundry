package exter.foundry.integration;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import exter.foundry.api.FoundryAPI;
import exter.foundry.config.FoundryConfig;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.registry.LiquidMetalRegistry;
import exter.foundry.util.FoundryMiscUtils;

public class ModIntegrationForestry extends ModIntegration
{
  
  public boolean gear_recipes;
  
  public ModIntegrationForestry(String mod_name)
  {
    super(mod_name);
  }

  @Override
  public void OnPreInit(Configuration config)
  {
    gear_recipes = config.get("integration", Name + ".gears", true).getBoolean(true);
  }

  @Override
  public void OnInit()
  {
  }

  @Override
  public void OnPostInit()
  {
    if(!Loader.isModLoaded("Forestry"))
    {
      is_loaded = false;
      return;
    }

    ItemStack copper_gear = new ItemStack(GameRegistry.findItem("Forestry","gearCopper"));
    ItemStack tin_gear = new ItemStack(GameRegistry.findItem("Forestry","gearTin"));
    ItemStack bronze_gear = new ItemStack(GameRegistry.findItem("Forestry","gearBronze"));

    if(is_loaded)
    {
      FoundryMiscUtils.RegisterInOreDictionary("gearCopper",copper_gear);
      FoundryMiscUtils.RegisterInOreDictionary("gearTin",tin_gear);
      FoundryMiscUtils.RegisterInOreDictionary("gearBronze",bronze_gear);
      if(!FoundryConfig.recipe_gear_useoredict && gear_recipes)
      {
        Fluid liquid_copper = LiquidMetalRegistry.instance.GetFluid("Copper");
        Fluid liquid_tin = LiquidMetalRegistry.instance.GetFluid("Tin");
        Fluid liquid_bronze = LiquidMetalRegistry.instance.GetFluid("Bronze");

        ItemStack mold_gear = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_GEAR);
        MeltingRecipeManager.instance.AddRecipe(copper_gear, new FluidStack(liquid_copper,FoundryAPI.FLUID_AMOUNT_INGOT * 4));
        MeltingRecipeManager.instance.AddRecipe(tin_gear, new FluidStack(liquid_tin,FoundryAPI.FLUID_AMOUNT_INGOT * 4));
        MeltingRecipeManager.instance.AddRecipe(bronze_gear, new FluidStack(liquid_bronze,FoundryAPI.FLUID_AMOUNT_INGOT * 4));

        CastingRecipeManager.instance.AddRecipe(copper_gear, new FluidStack(liquid_copper,FoundryAPI.FLUID_AMOUNT_INGOT * 4),mold_gear,null);
        CastingRecipeManager.instance.AddRecipe(tin_gear, new FluidStack(liquid_tin,FoundryAPI.FLUID_AMOUNT_INGOT * 4),mold_gear,null);
        CastingRecipeManager.instance.AddRecipe(bronze_gear, new FluidStack(liquid_bronze,FoundryAPI.FLUID_AMOUNT_INGOT * 4),mold_gear,null);
      }
    }
  }
}
