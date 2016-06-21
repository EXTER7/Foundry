package exter.foundry.api;

import exter.foundry.api.firearms.IFirearmRound;
import exter.foundry.api.heatable.IHeatProvider;
import exter.foundry.api.recipe.manager.IAlloyFurnaceRecipeManager;
import exter.foundry.api.recipe.manager.IAlloyMixerRecipeManager;
import exter.foundry.api.recipe.manager.IAtomizerRecipeManager;
import exter.foundry.api.recipe.manager.IBurnerHeaterFuelManager;
import exter.foundry.api.recipe.manager.ICastingRecipeManager;
import exter.foundry.api.recipe.manager.ICastingTableRecipeManager;
import exter.foundry.api.recipe.manager.IInfuserRecipeManager;
import exter.foundry.api.recipe.manager.IMeltingRecipeManager;
import exter.foundry.api.recipe.manager.IMoldRecipeManager;
import exter.foundry.api.registry.IFluidRegistry;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

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
   * Tank capacity for machines.
   */
  static public final int CRUCIBLE_TANK_CAPACITY = 6000;
  static public final int CASTER_TANK_CAPACITY = 6000;
  static public final int INFUSER_TANK_CAPACITY = 5000;
  static public final int ALLOYMIXER_TANK_CAPACITY = 2000;
  static public final int ATOMIZER_TANK_CAPACITY = 6000;
  static public final int ATOMIZER_WATER_TANK_CAPACITY = 6000;

  //Heat loss rates for crucibles.
  static public final int CRUCIBLE_TEMP_LOSS_RATE = 750;
  static public final int CRUCIBLE_ADVANCED_TEMP_LOSS_RATE = 900;
  
  //Max temperatures for crucibles.
  static public final int CRUCIBLE_MAX_TEMP = 200000;
  static public final int CRUCIBLE_ADVANCED_MAX_TEMP = 400000;

  
  //These fields are set by Foundry during it's preInit phase.
  static public IMeltingRecipeManager recipes_melting;
  static public ICastingRecipeManager recipes_casting;
  static public ICastingTableRecipeManager recipes_casting_table;
  static public IAlloyMixerRecipeManager recipes_alloymixer;
  static public IInfuserRecipeManager recipes_infuser;
  static public IAlloyFurnaceRecipeManager recipes_alloyfurnace;
  static public IAtomizerRecipeManager recipes_atomizer;
  static public IMoldRecipeManager recipes_mold;
  static public IBurnerHeaterFuelManager burnerheater_fuel;
  
  
  static public IFluidRegistry fluids;
  
  @CapabilityInject(IHeatProvider.class)
  static public Capability<IHeatProvider> capability_heatprovider;
  
  @CapabilityInject(IFirearmRound.class)
  static public Capability<IFirearmRound> capability_firearmround;
}
