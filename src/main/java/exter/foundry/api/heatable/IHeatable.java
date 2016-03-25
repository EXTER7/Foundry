package exter.foundry.api.heatable;

import net.minecraft.util.EnumFacing;

public interface IHeatable
{
  public void receiveHeat(EnumFacing side, int heat);
  
  public int getMaxHeatReceive(EnumFacing side);
}
