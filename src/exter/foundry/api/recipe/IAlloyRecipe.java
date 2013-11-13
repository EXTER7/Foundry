package exter.foundry.api.recipe;

import net.minecraftforge.fluids.FluidStack;

public interface IAlloyRecipe
{
  /**
   * Get the recipe's first input.
   * @return FluidStack containing Recipe's first input fluid and amount required.
   */
  public FluidStack GetInputA();
  
  /**
   * Get the recipe's second input.
   * @return FluidStack containing Recipe's second input fluid and amount required.
   */
  public FluidStack GetInputB();

  /**
   * Get the recipe's output.
   * @return FluidStack containing Recipe's produced fluid and amount.
   */
  public FluidStack GetOutput();

}
