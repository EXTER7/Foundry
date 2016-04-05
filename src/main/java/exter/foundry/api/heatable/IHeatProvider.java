package exter.foundry.api.heatable;

import net.minecraft.util.EnumFacing;

public interface IHeatProvider
{
  public int provideHeat(EnumFacing side, int max_heat);
}
