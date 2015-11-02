package exter.foundry.integration;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import exter.foundry.api.FoundryAPI;
import exter.foundry.config.FoundryConfig;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.FoundryRecipes;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.util.FoundryMiscUtils;

public class ModIntegrationForestry implements IModIntegration
{
  
  public boolean gear_recipes;

  @Override
  public void onPreInit(Configuration config)
  {
    gear_recipes = config.get("integration", getName() + ".gears", true).getBoolean(true);
  }

  @Override
  public void onInit()
  {
  }

  @Override
  public void onPostInit()
  {
    if(!Loader.isModLoaded("Forestry"))
    {
      return;
    }

    ItemStack copper_gear = new ItemStack(GameRegistry.findItem("Forestry", "gearCopper"));
    ItemStack tin_gear = new ItemStack(GameRegistry.findItem("Forestry", "gearTin"));
    ItemStack bronze_gear = new ItemStack(GameRegistry.findItem("Forestry", "gearBronze"));

    FoundryMiscUtils.registerInOreDictionary("gearCopper", copper_gear);
    FoundryMiscUtils.registerInOreDictionary("gearTin", tin_gear);
    FoundryMiscUtils.registerInOreDictionary("gearBronze", bronze_gear);
    if(!FoundryConfig.recipe_gear_useoredict && gear_recipes)
    {
      ItemStack mold_gear = FoundryItems.mold(ItemMold.MOLD_GEAR);
      MeltingRecipeManager.instance.addRecipe(copper_gear, new FluidStack(FoundryRecipes.liquid_copper, FoundryAPI.FLUID_AMOUNT_INGOT * 4));
      MeltingRecipeManager.instance.addRecipe(tin_gear, new FluidStack(FoundryRecipes.liquid_tin, FoundryAPI.FLUID_AMOUNT_INGOT * 4));
      MeltingRecipeManager.instance.addRecipe(bronze_gear, new FluidStack(FoundryRecipes.liquid_bronze, FoundryAPI.FLUID_AMOUNT_INGOT * 4));

      CastingRecipeManager.instance.addRecipe(copper_gear, new FluidStack(FoundryRecipes.liquid_copper, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
      CastingRecipeManager.instance.addRecipe(tin_gear, new FluidStack(FoundryRecipes.liquid_tin, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
      CastingRecipeManager.instance.addRecipe(bronze_gear, new FluidStack(FoundryRecipes.liquid_bronze, FoundryAPI.FLUID_AMOUNT_INGOT * 4), mold_gear, null);
    }
  }

  @Override
  public String getName()
  {
    return "forestry";
  }

  @Override
  public void onAfterPostInit()
  {
    
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void onClientPreInit()
  {
    
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void onClientInit()
  {
    
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void onClientPostInit()
  {
    
  }
}
