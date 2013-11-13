package exter.foundry.api;

import exter.foundry.api.recipe.FoundryRecipes;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class FoundryUtils
{
  static public final int FLUID_AMOUNT_BLOCK = 972;
  static public final int FLUID_AMOUNT_INGOT = 108;
  static public final int FLUID_AMOUNT_NUGGET = 12;
  static public final int FLUID_AMOUNT_ORE = 216;

  /**
   * Maximum amount of substance a metal infuser can store.
   */
  static public final int INFUSER_SUBSTANCE_AMOUNT_MAX = 1000;

  /**
   * Helper method for registering basic melting recipes for a given metal.
   * @param partial_name The partial ore dictionary name e.g. "Copper" for "ingotCopper","oreCopper", etc.
   * @param fluid The liquid created by the smelter.
   */
  static public void RegisterBasicMeltingRecipes(String partial_name,Fluid fluid)
  {
    if(FoundryRecipes.melting != null)
    {
      FoundryRecipes.melting.AddRecipe("ingot" + partial_name, new FluidStack(fluid, FLUID_AMOUNT_INGOT));
      FoundryRecipes.melting.AddRecipe("block" + partial_name, new FluidStack(fluid, FLUID_AMOUNT_BLOCK));
      FoundryRecipes.melting.AddRecipe("nugget" + partial_name, new FluidStack(fluid, FLUID_AMOUNT_NUGGET));
      FoundryRecipes.melting.AddRecipe("dust" + partial_name, new FluidStack(fluid, FLUID_AMOUNT_INGOT));
      FoundryRecipes.melting.AddRecipe("ore" + partial_name, new FluidStack(fluid, FLUID_AMOUNT_ORE));
    }
  }

}
