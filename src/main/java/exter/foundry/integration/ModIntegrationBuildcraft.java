package exter.foundry.integration;

import cpw.mods.fml.common.Loader;
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

public class ModIntegrationBuildcraft extends ModIntegration
{
 
  public boolean gear_recipes;
  
  public ModIntegrationBuildcraft(String mod_name)
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
    if(!Loader.isModLoaded("BuildCraft|Core"))
    {
      is_loaded = false;
      return;
    }
    ItemStack iron_gear = FoundryMiscUtils.GetModItemFromOreDictionary("BuildCraft|Core", "gearIron");
    ItemStack gold_gear = FoundryMiscUtils.GetModItemFromOreDictionary("BuildCraft|Core", "gearGold");

    if(is_loaded)
    {
      if(!FoundryConfig.recipe_gear_useoredict && gear_recipes)
      {
        ItemStack mold_gear = FoundryItems.Mold(ItemMold.MOLD_GEAR);
        MeltingRecipeManager.instance.AddRecipe(iron_gear, new FluidStack(FoundryRecipes.liquid_iron,FoundryAPI.FLUID_AMOUNT_INGOT * 4));
        MeltingRecipeManager.instance.AddRecipe(gold_gear, new FluidStack(FoundryRecipes.liquid_gold,FoundryAPI.FLUID_AMOUNT_INGOT * 4));

        CastingRecipeManager.instance.AddRecipe(iron_gear, new FluidStack(FoundryRecipes.liquid_iron,FoundryAPI.FLUID_AMOUNT_INGOT * 4),mold_gear,null);
        CastingRecipeManager.instance.AddRecipe(gold_gear, new FluidStack(FoundryRecipes.liquid_gold,FoundryAPI.FLUID_AMOUNT_INGOT * 4),mold_gear,null);
      }
    }
  }
}
