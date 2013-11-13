package exter.foundry.api.recipe;

import net.minecraftforge.fluids.FluidStack;

public interface IInfuserRecipe
{
  /**
   * Get the fluid required.
   * @return FluidStack containing the required fluid.
   */
  public FluidStack GetInputFluid();
  
  public String GetInputSubstanceType();
  
  public int GetInputSubstanceAmount();

  /**
   * Get the produced fluid.
   * @return The fluid that the recipe produces.
   */
  public FluidStack GetOutput();
}
