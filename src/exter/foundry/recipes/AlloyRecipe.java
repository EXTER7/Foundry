package exter.foundry.recipes;

import java.util.ArrayList;
import java.util.List;

import exter.foundry.api.recipe.IAlloyRecipe;
import net.minecraftforge.fluids.FluidStack;

/*
 * Alloy Mixer recipe manager
 */
public class AlloyRecipe implements IAlloyRecipe
{
  
  public FluidStack input_a;
  public FluidStack input_b;
  public FluidStack output;

  @Override
  public FluidStack GetInputA()
  {
    return input_a.copy();
  }
  
  @Override
  public FluidStack GetInputB()
  {
    return input_b.copy();
  }

  @Override
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
