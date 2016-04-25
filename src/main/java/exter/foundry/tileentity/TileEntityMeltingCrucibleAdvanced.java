package exter.foundry.tileentity;

public class TileEntityMeltingCrucibleAdvanced extends TileEntityMeltingCrucible
{
  static public final int TEMP_LOSS_RATE = 900;
  
  static public int getMaxHeatRecieve(int max_heat)
  {
    return (max_heat - TEMP_MIN) / TEMP_LOSS_RATE;
  }
  
  public int getMaxTemperature()
  {
    return 400000;
  }
  
  protected int getTemperatureLossRate()
  {
    return TEMP_LOSS_RATE;
  }
}
