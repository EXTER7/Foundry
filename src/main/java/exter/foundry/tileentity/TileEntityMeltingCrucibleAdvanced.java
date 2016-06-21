package exter.foundry.tileentity;

public class TileEntityMeltingCrucibleAdvanced extends TileEntityMeltingCrucible
{
  static public final int TEMP_LOSS_RATE = 900;

  @Override
  public int getMaxTemperature()
  {
    return 400000;
  }
  
  @Override
  protected int getTemperatureLossRate()
  {
    return TEMP_LOSS_RATE;
  }
}
