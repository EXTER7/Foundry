package exter.foundry.recipes;

import exter.foundry.api.recipe.IInfuserRecipe;
import exter.foundry.api.substance.InfuserSubstance;
import net.minecraftforge.fluids.FluidStack;

/**
 * Metal Infuser recipe manager
 */
public class InfuserRecipe implements IInfuserRecipe
{
 
  /**
   * Required fluid.
   */
  public final FluidStack fluid;

  /**
   * Substance required for the recipe.
   */
  public final InfuserSubstance substance;

  /**
   * Fluid produced.
   */
  public final FluidStack output;
  
  @Override
  public FluidStack GetInputFluid()
  {
    return fluid.copy();
  }
  
  @Override
  public InfuserSubstance GetInputSubstance()
  {
    return substance;
  }
  
  @Override
  public FluidStack GetOutput()
  {
    return output.copy();
  }

  public InfuserRecipe(FluidStack result,FluidStack in_fluid,InfuserSubstance in_substance)
  {
    if(in_fluid == null)
    {
      throw new IllegalArgumentException("Infuser recipe input cannot be null");
    }
    if(in_substance == null)
    {
      throw new IllegalArgumentException("Infuser recipe substance cannot be null");
    }
    if(result == null)
    {
      throw new IllegalArgumentException("Infuser recipe output cannot be null");
    }
    fluid = in_fluid.copy();
    substance = new InfuserSubstance(in_substance);
    output = result.copy();
  }
  
  @Override
  public boolean MatchesRecipe(FluidStack in_fluid,InfuserSubstance in_substance)
  {
    if(substance == null || substance.amount <= 0)
    {
      return false;
    }
    return in_fluid.containsFluid(fluid) && in_substance.Contains(substance);
  }
}
