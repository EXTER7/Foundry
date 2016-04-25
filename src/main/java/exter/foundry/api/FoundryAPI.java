package exter.foundry.api;

import exter.foundry.api.recipe.manager.IAlloyFurnaceRecipeManager;
import exter.foundry.api.recipe.manager.IAlloyMixerRecipeManager;
import exter.foundry.api.recipe.manager.IAtomizerRecipeManager;
import exter.foundry.api.recipe.manager.ICastingRecipeManager;
import exter.foundry.api.recipe.manager.IInfuserRecipeManager;
import exter.foundry.api.recipe.manager.IMeltingRecipeManager;
import exter.foundry.api.recipe.manager.IMoldRecipeManager;
import exter.foundry.api.registry.IFluidRegistry;

/**
 * API for recipes of Foundry machines.
 */
public class FoundryAPI
{
  static public final int FLUID_AMOUNT_BLOCK = 972;
  static public final int FLUID_AMOUNT_INGOT = 108;
  static public final int FLUID_AMOUNT_PLATE = 108;
  static public final int FLUID_AMOUNT_ROD = 54;
  static public final int FLUID_AMOUNT_NUGGET = 12;
  static public final int FLUID_AMOUNT_ORE = 216;

  /**
   * Maximum amount of substance a metal infuser can store.
   */
  static public final int INFUSER_SUBSTANCE_AMOUNT_MAX = 1000;

  /**
   * Tank capacity for machines.
   */
  static public final int CRUCIBLE_TANK_CAPACITY = 6000;
  static public final int CASTER_TANK_CAPACITY = 6000;
  static public final int INFUSER_TANK_CAPACITY = 5000;
  static public final int ALLOYMIXER_TANK_CAPACITY = 2000;
  static public final int ATOMIZER_TANK_CAPACITY = 6000;
  static public final int ATOMIZER_WATER_TANK_CAPACITY = 6000;

  //These fields are set by Foundry during it's preInit phase.
  //If foundry is not installed they become null.
  static public IMeltingRecipeManager recipes_melting;
  static public ICastingRecipeManager recipes_casting;
  static public IAlloyMixerRecipeManager recipes_alloymixer;
  static public IInfuserRecipeManager recipes_infuser;
  static public IAlloyFurnaceRecipeManager recipes_alloyfurnace;
  static public IAtomizerRecipeManager recipes_atomizer;
  static public IMoldRecipeManager recipes_mold;
  
  static public IFluidRegistry fluids;
}
