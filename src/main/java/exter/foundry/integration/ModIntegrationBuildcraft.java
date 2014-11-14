package exter.foundry.integration;

import exter.foundry.api.FoundryAPI;
import exter.foundry.config.FoundryConfig;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.registry.LiquidMetalRegistry;
import exter.foundry.util.FoundryMiscUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class ModIntegrationBuildcraft extends ModIntegration
{
  static public final int ITEM_IRON_GEAR = 0;
  static public final int ITEM_GOLD_GEAR = 1;
  
  public ModIntegrationBuildcraft(String mod_name)
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
    items = new ItemStack[2];
    items[ITEM_IRON_GEAR] = FoundryMiscUtils.GetModItemFromOreDictionary("BuildCraft|Core", "gearIron");
    items[ITEM_GOLD_GEAR] = FoundryMiscUtils.GetModItemFromOreDictionary("BuildCraft|Core", "gearGold");
    VerifyItems();

    if(is_loaded)
    {
      if(!FoundryConfig.recipe_gear_useoredict)
      {
        Fluid liquid_iron = LiquidMetalRegistry.instance.GetFluid("Iron");
        Fluid liquid_gold = LiquidMetalRegistry.instance.GetFluid("Gold");
        ItemStack mold_gear = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_GEAR);
        MeltingRecipeManager.instance.AddRecipe(items[ITEM_IRON_GEAR], new FluidStack(liquid_iron,FoundryAPI.FLUID_AMOUNT_INGOT * 4));
        MeltingRecipeManager.instance.AddRecipe(items[ITEM_GOLD_GEAR], new FluidStack(liquid_gold,FoundryAPI.FLUID_AMOUNT_INGOT * 4));

        CastingRecipeManager.instance.AddRecipe(items[ITEM_IRON_GEAR], new FluidStack(liquid_iron,FoundryAPI.FLUID_AMOUNT_INGOT * 4),mold_gear,null);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_GOLD_GEAR], new FluidStack(liquid_gold,FoundryAPI.FLUID_AMOUNT_INGOT * 4),mold_gear,null);
      }
    }
  }
}
