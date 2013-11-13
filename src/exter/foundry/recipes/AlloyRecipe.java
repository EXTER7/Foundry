package exter.foundry.recipes;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.fluids.FluidStack;

/*
 * Alloy Mixer recipe manager
 */
public class AlloyRecipe
{
  
  public FluidStack input_a;
  public FluidStack input_b;
  public FluidStack output;

  /**
   * Get the recipe's first input.
   * @return FluidStack containing Recipe's first input fluid and amount required.
   */
  public FluidStack GetInputA()
  {
    return input_a.copy();
  }
  
  /**
   * Get the recipe's second input.
   * @return FluidStack containing Recipe's second input fluid and amount required.
   */
  public FluidStack GetInputB()
  {
    return input_b.copy();
  }

  /**
   * Get the recipe's output.
   * @return FluidStack containing Recipe's produced fluid and amount.
   */
  public FluidStack GetOutput()
  {
    return output.copy();
  }
  
  public AlloyRecipe(FluidStack out,FluidStack in_a,FluidStack in_b)
  {
    output = out;
    input_a = in_a;
    input_b = in_b;
  }
  
}
