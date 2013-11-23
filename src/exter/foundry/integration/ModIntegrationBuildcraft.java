package exter.foundry.integration;

import exter.foundry.api.recipe.FoundryRecipes;
import exter.foundry.config.FoundryConfig;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemMold;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.recipes.manager.MeltingRecipeManager;
import exter.foundry.registry.LiquidMetalRegistry;
import exter.foundry.util.FoundryMiscUtils;
import exter.foundry.ModFoundry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class ModIntegrationBuildcraft extends ModIntegration
{
  static public final int ITEM_WOOD_GEAR = 0;
  static public final int ITEM_STONE_GEAR = 1;
  static public final int ITEM_IRON_GEAR = 2;
  static public final int ITEM_GOLD_GEAR = 3;
  static public final int ITEM_DIAMOND_GEAR = 4;
  
  public ModIntegrationBuildcraft(String mod_name)
  {
    super(mod_name);
    try
    {
      items = new ItemStack[5];
      Class BuildCraftCore = Class.forName("buildcraft.BuildCraftCore");
      items[ITEM_WOOD_GEAR] = GetItemFromField(BuildCraftCore,"woodenGearItem");
      items[ITEM_STONE_GEAR] = GetItemFromField(BuildCraftCore,"stoneGearItem");
      items[ITEM_IRON_GEAR] = GetItemFromField(BuildCraftCore,"ironGearItem");
      items[ITEM_GOLD_GEAR] = GetItemFromField(BuildCraftCore,"goldGearItem");
      items[ITEM_DIAMOND_GEAR] = GetItemFromField(BuildCraftCore,"diamondGearItem");
      VerifyItems();
    } catch(ClassNotFoundException e)
    {
      ModFoundry.log.info("[ModIntegration ("+ mod_name +")] Cannot find buildcraft.BuildCraftCore class");
      is_loaded = false;
      return;
    }

    if(is_loaded)
    {
      if(!FoundryConfig.recipe_gear_useoredict)
      {
        Fluid liquid_iron = LiquidMetalRegistry.instance.GetFluid("Iron");
        Fluid liquid_gold = LiquidMetalRegistry.instance.GetFluid("Gold");
        ItemStack mold_gear = new ItemStack(FoundryItems.item_mold,1,ItemMold.MOLD_GEAR);
        MeltingRecipeManager.instance.AddRecipe(items[ITEM_IRON_GEAR], new FluidStack(liquid_iron,FoundryRecipes.FLUID_AMOUNT_INGOT * 4));
        MeltingRecipeManager.instance.AddRecipe(items[ITEM_GOLD_GEAR], new FluidStack(liquid_gold,FoundryRecipes.FLUID_AMOUNT_INGOT * 4));

        CastingRecipeManager.instance.AddRecipe(items[ITEM_IRON_GEAR], new FluidStack(liquid_iron,FoundryRecipes.FLUID_AMOUNT_INGOT * 4),mold_gear,null);
        CastingRecipeManager.instance.AddRecipe(items[ITEM_GOLD_GEAR], new FluidStack(liquid_gold,FoundryRecipes.FLUID_AMOUNT_INGOT * 4),mold_gear,null);
      }
      FoundryMiscUtils.RegisterInOreDictionary("gearWood",items[ITEM_WOOD_GEAR]);
      FoundryMiscUtils.RegisterInOreDictionary("gearStone",items[ITEM_STONE_GEAR]);
      FoundryMiscUtils.RegisterInOreDictionary("gearIron",items[ITEM_IRON_GEAR]);
      FoundryMiscUtils.RegisterInOreDictionary("gearGold",items[ITEM_GOLD_GEAR]);
      FoundryMiscUtils.RegisterInOreDictionary("gearDiamond",items[ITEM_DIAMOND_GEAR]);
    }
  }
}
