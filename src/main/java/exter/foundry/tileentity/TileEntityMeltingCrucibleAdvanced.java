package exter.foundry.tileentity;

import exter.foundry.api.FoundryAPI;

public class TileEntityMeltingCrucibleAdvanced extends TileEntityMeltingCrucible
{
  @Override
  public int getMaxTemperature()
  {
    return FoundryAPI.CRUCIBLE_ADVANCED_MAX_TEMP;
  }
  
  @Override
  protected int getTemperatureLossRate()
  {
    return FoundryAPI.CRUCIBLE_ADVANCED_TEMP_LOSS_RATE;
  }
}
