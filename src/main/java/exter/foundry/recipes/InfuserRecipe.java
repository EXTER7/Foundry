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
  public FluidStack getInputFluid()
  {
    return fluid.copy();
  }
  
  @Override
  public InfuserSubstance getInputSubstance()
  {
    return substance;
  }
  
  @Override
  public FluidStack getOutput()
  {
    return output.copy();
  }

  public InfuserRecipe(FluidStack result,FluidStack in_fluid,InfuserSubstance in_substance)
  {
    fluid = in_fluid.copy();
    substance = new InfuserSubstance(in_substance);
    output = result.copy();
  }
  
  @Override
  public boolean matchesRecipe(FluidStack in_fluid,InfuserSubstance in_substance)
  {
    if(substance == null || substance.amount <= 0)
    {
      return false;
    }
    return in_fluid.containsFluid(fluid) && in_substance.Contains(substance);
  }
}
