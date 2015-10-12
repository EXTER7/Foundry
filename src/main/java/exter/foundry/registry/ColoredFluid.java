package exter.foundry.registry;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class ColoredFluid extends Fluid
{
  
  private int color;

  public ColoredFluid(String fluidName,ResourceLocation still,ResourceLocation flowing)
  {
    super(fluidName,still, flowing);
    color = 0xFFFFFF;
  }

  public ColoredFluid setColor(int fluid_color)
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
