package exter.foundry.tileentity;

import exter.foundry.api.FoundryAPI;

public class TileEntityMeltingCrucibleStandard extends TileEntityMeltingCrucibleBasic
{
  @Override
  public int getMaxTemperature()
  {
    return FoundryAPI.CRUCIBLE_STANDARD_MAX_TEMP;
  }
  
  @Override
  protected int getTemperatureLossRate()
  {
    return FoundryAPI.CRUCIBLE_STANDARD_TEMP_LOSS_RATE;
  }
}
