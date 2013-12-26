package exter.foundry.recipes;

import exter.foundry.api.recipe.IInfuserRecipe;
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
  public String GetInputSubstanceType()
  {
    return substance.type;
  }
  
  @Override
  public int GetInputSubstanceAmount()
  {
    return substance.amount;
  }
  
  @Override
  public FluidStack GetOutput()
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
  public boolean MatchesRecipe(FluidStack in_fluid,String substance_type,int substance_amount)
  {
    if(substance_type == null || substance_amount <= 0)
    {
      return false;
    }
    return in_fluid.containsFluid(fluid) && substance_type.equals(substance.type) && substance_amount >= substance.amount;
  }
}
