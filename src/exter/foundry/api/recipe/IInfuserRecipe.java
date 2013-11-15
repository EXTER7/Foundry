package exter.foundry.api.recipe;

import net.minecraftforge.fluids.FluidStack;

public interface IInfuserRecipe
{
  /**
   * Get the fluid required.
   * @return FluidStack containing the required fluid.
   */
  public FluidStack GetInputFluid();
  
  /**
   * Get the substance type required.
   * @return The substance type.
   */
  public String GetInputSubstanceType();
  
  /**
   * Get the substance amount required.
   * @return The substance amount.
   */
  public int GetInputSubstanceAmount();

  /**
   * Get the produced fluid.
   * @return The fluid that the recipe produces.
   */
  public FluidStack GetOutput();
}
