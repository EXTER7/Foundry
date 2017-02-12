package exter.foundry.fluid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class FluidLiquidMetal extends Fluid
{
  
  private int color;
  
  public final boolean special;

  public FluidLiquidMetal(String fluidName,ResourceLocation still, ResourceLocation flowing, int color, boolean special, int temperature,int luminosity)
  {
    super(fluidName.replaceAll("_",""),still, flowing);
    this.color = color | 0xFF000000;
    this.special = special;
    setUnlocalizedName("foundry." + fluidName);
    setTemperature(temperature);
    setLuminosity(luminosity);
    setDensity(2000);
  }

  public FluidLiquidMetal setColor(int fluid_color)
  {
    color = fluid_color | 0xFF000000;
    return this;
  }
  
  @Override
  public int getColor()
  {
    return color;
  }
}
