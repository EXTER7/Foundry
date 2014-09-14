package exter.foundry.api;

import exter.foundry.api.recipe.manager.IAlloyFurnaceRecipeManager;
import exter.foundry.api.recipe.manager.IAlloyMixerRecipeManager;
import exter.foundry.api.recipe.manager.ICastingRecipeManager;
import exter.foundry.api.recipe.manager.IInfuserRecipeManager;
import exter.foundry.api.recipe.manager.IMeltingRecipeManager;
import exter.foundry.api.registry.IFluidRegistry;
import exter.foundry.api.registry.IItemRegistry;

/**
 * API for recipes of Foundry machines.
 */
public class FoundryAPI
{
  static public final int FLUID_AMOUNT_BLOCK = 1296;
  static public final int FLUID_AMOUNT_INGOT = 144;
  static public final int FLUID_AMOUNT_NUGGET = 16;
  static public final int FLUID_AMOUNT_ORE = 288;

  /**
   * Maximum amount of substance a metal infuser can store.
   */
  static public final int INFUSER_SUBSTANCE_AMOUNT_MAX = 2000;

  /**
   * Tank capacity for machines.
   */
  static public final int ICF_TANK_CAPACITY = 10000;
  static public final int CASTER_TANK_CAPACITY = 10000;
  static public final int INFUSER_TANK_CAPACITY = 10000;
  static public final int ALLOYMIXER_TANK_CAPACITY = 4000;

  //These fields are set by Foundry during it's preInit phase.
  //If foundry is not installed they become null.
  static public IMeltingRecipeManager recipes_melting;
  static public ICastingRecipeManager recipes_casting;
  static public IAlloyMixerRecipeManager recipes_alloymixer;
  static public IInfuserRecipeManager recipes_infuser;
  static public IAlloyFurnaceRecipeManager recipes_alloyfurnace;
  
  static public IItemRegistry items;
  static public IFluidRegistry fluids;
}
