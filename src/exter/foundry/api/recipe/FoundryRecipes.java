package exter.foundry.api.recipe;

import exter.foundry.api.recipe.manager.IAlloyRecipeManager;
import exter.foundry.api.recipe.manager.ICastingRecipeManager;
import exter.foundry.api.recipe.manager.IInfuserRecipeManager;
import exter.foundry.api.recipe.manager.IMeltingRecipeManager;

/**
 * API for recipes of Foundry machines.
 */
public class FoundryRecipes
{
  static public IMeltingRecipeManager melting;

  static public ICastingRecipeManager casting;
  
  static public IAlloyRecipeManager alloy;
  
  static public IInfuserRecipeManager infuser;
}
