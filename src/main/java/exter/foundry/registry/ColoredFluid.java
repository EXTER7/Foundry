package exter.foundry.registry;

import net.minecraftforge.fluids.Fluid;

public class ColoredFluid extends Fluid
{
  
  private int color;

  public ColoredFluid(String fluidName)
  {
    super(fluidName);
    color = 0xFFFFFF;
  }

  public ColoredFluid SetColor(int fluid_color)
  {
    color = fluid_color;
    return this;
  }
  
  @Override
  public int getColor()
  {
    return color;
  }
}
