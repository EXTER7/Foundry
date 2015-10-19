package exter.foundry.integration;

import exter.foundry.api.FoundryAPI;
import exter.foundry.config.FoundryConfig;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.FoundryRecipes;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;

public class ModIntegrationBuildcraft extends ModIntegration
{
 
  public boolean gear_recipes;
  
  public ModIntegrationBuildcraft(String mod_name)
  {
    super(mod_name);
  }

  @Override
  public void onPreInit(Configuration config)
  {
    gear_recipes = config.get("integration", Name + ".gears", true).getBoolean(true);
  }

  @Override
  public void onInit()
  {
  }

  @Override
  public void onPostInit()
  {
    if(!Loader.isModLoaded("BuildCraft|Core"))
    {
      is_loaded = false;
      return;
    }
    ItemStack iron_gear = FoundryMiscUtils.getModItemFromOreDictionary("BuildCraft|Core", "gearIron");
    ItemStack gold_gear = FoundryMiscUtils.getModItemFromOreDictionary("BuildCraft|Core", "gearGold");

    if(is_loaded)
    {
      if(!FoundryConfig.recipe_gear_useoredict && gear_recipes)
      {
        ItemStack mold_gear = FoundryItems.mold(ItemMold.MOLD_GEAR);
        MeltingRecipeManager.instance.addRecipe(iron_gear, new FluidStack(FoundryRecipes.liquid_iron,FoundryAPI.FLUID_AMOUNT_INGOT * 4));
        MeltingRecipeManager.instance.addRecipe(gold_gear, new FluidStack(FoundryRecipes.liquid_gold,FoundryAPI.FLUID_AMOUNT_INGOT * 4));

        CastingRecipeManager.instance.addRecipe(iron_gear, new FluidStack(FoundryRecipes.liquid_iron,FoundryAPI.FLUID_AMOUNT_INGOT * 4),mold_gear,null);
        CastingRecipeManager.instance.addRecipe(gold_gear, new FluidStack(FoundryRecipes.liquid_gold,FoundryAPI.FLUID_AMOUNT_INGOT * 4),mold_gear,null);
      }
    }
  }
}
