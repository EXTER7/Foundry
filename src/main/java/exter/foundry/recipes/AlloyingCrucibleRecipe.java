package exter.foundry.recipes;

import exter.foundry.api.recipe.IAlloyingCrucibleRecipe;
import net.minecraftforge.fluids.FluidStack;

/*
 * Alloying Crucible recipe manager
 */
public class AlloyingCrucibleRecipe implements IAlloyingCrucibleRecipe
{
  
  public FluidStack input_a;
  public FluidStack input_b;
  public FluidStack output;

  @Override
  public FluidStack getInputA()
  {
    return input_a.copy();
  }

  @Override
  public FluidStack getInputB()
  {
    return input_b.copy();
  }

  @Override
  public FluidStack getOutput()
  {
    return output.copy();
  }
  
  public AlloyingCrucibleRecipe(FluidStack out,FluidStack in_a,FluidStack in_b)
  {
    if(out == null)
    {
      throw new IllegalArgumentException("Alloy crucble recipe output cannot be null");
    }
    if(in_a == null)
    {
      throw new IllegalArgumentException("Alloying crucble recipe input A cannot be null");
    }
    if(in_b == null)
    {
      throw new IllegalArgumentException("Alloying crucble recipe input B cannot be null");
    }
    output = out.copy();
    input_a = in_a.copy();
    input_b = in_b.copy();
  }

  @Override
  public boolean matchesRecipe(FluidStack in_a,FluidStack in_b)
  {
    return in_a != null && in_b != null && in_a.containsFluid(input_a) && in_b.containsFluid(input_b);
  }
}
