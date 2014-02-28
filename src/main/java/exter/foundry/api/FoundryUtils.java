package exter.foundry.api;

import exter.foundry.api.recipe.FoundryRecipes;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class FoundryUtils
{

  /**
   * Maximum amount of substance a metal infuser can store.
   */
  static public final int INFUSER_SUBSTANCE_AMOUNT_MAX = 1000;

  /**
   * Tank capacity for machines.
   */
  static public final int ICF_TANK_CAPACITY = 6000;
  static public final int CASTER_TANK_CAPACITY = 6000;
  static public final int INFUSER_TANK_CAPACITY = 5000;
  static public final int ALLOYMIXER_TANK_CAPACITY = 2000;

  /**
   * Helper method for registering basic melting recipes for a given metal.
   * @param partial_name The partial ore dictionary name e.g. "Copper" for "ingotCopper","oreCopper", etc.
   * @param fluid The liquid created by the smelter.
   */
  static public void RegisterBasicMeltingRecipes(String partial_name,Fluid fluid)
  {
    if(FoundryRecipes.melting != null)
    {
      FoundryRecipes.melting.AddRecipe("ingot" + partial_name, new FluidStack(fluid, FoundryRecipes.FLUID_AMOUNT_INGOT));
      FoundryRecipes.melting.AddRecipe("block" + partial_name, new FluidStack(fluid, FoundryRecipes.FLUID_AMOUNT_BLOCK));
      FoundryRecipes.melting.AddRecipe("nugget" + partial_name, new FluidStack(fluid, FoundryRecipes.FLUID_AMOUNT_NUGGET));
      FoundryRecipes.melting.AddRecipe("dust" + partial_name, new FluidStack(fluid, FoundryRecipes.FLUID_AMOUNT_INGOT));
      FoundryRecipes.melting.AddRecipe("ore" + partial_name, new FluidStack(fluid, FoundryRecipes.FLUID_AMOUNT_ORE));
    }
  }

}
