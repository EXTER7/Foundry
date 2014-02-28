package exter.foundry.api.recipe;

import exter.foundry.api.recipe.manager.IAlloyMixerRecipeManager;
import exter.foundry.api.recipe.manager.ICastingRecipeManager;
import exter.foundry.api.recipe.manager.IInfuserRecipeManager;
import exter.foundry.api.recipe.manager.IMeltingRecipeManager;

/**
 * API for recipes of Foundry machines.
 */
public class FoundryRecipes
{
  static public final int FLUID_AMOUNT_BLOCK = 972;
  static public final int FLUID_AMOUNT_INGOT = 108;
  static public final int FLUID_AMOUNT_NUGGET = 12;
  static public final int FLUID_AMOUNT_ORE = 216;

  //These fields are set by Foundry during it's preInit phase.
  //If foundry is not installed they become null.
  static public IMeltingRecipeManager melting;
  static public ICastingRecipeManager casting;
  static public IAlloyMixerRecipeManager alloymixer;
  static public IInfuserRecipeManager infuser;
}
