package exter.foundry.api.recipe;

import exter.foundry.api.substance.InfuserSubstance;
import net.minecraftforge.fluids.FluidStack;

public interface IInfuserRecipe
{
  /**
   * Get the fluid required.
   * @return FluidStack containing the required fluid.
   */
  public FluidStack GetInputFluid();
  
  /**
   * Get the substance required.
   * @return The substance required.
   */
  public InfuserSubstance GetInputSubstance();
  
  /**
   * Get the produced fluid.
   * @return The fluid that the recipe produces.
   */
  public FluidStack GetOutput();
  
  /**
   * Check if a fluid stack and substance stack matches this recipe
   * @param in_fluid Fluid to check.
   * @param substance_type Substance type to check.
   * @param substance_amount Substance amount to check.
   * @return true if the fluid and substance matches, false otherwise.
   */
  public boolean MatchesRecipe(FluidStack in_fluid,InfuserSubstance in_substance);
}
