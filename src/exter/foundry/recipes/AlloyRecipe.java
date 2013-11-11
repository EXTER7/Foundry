package exter.foundry.recipes;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.fluids.FluidStack;

/*
 * Alloy Mixer recipe manager
 */
public class AlloyRecipe
{
  static private List<AlloyRecipe> recipes = new ArrayList<AlloyRecipe>();
  
  private FluidStack input_a;
  private FluidStack input_b;
  private FluidStack output;

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
  
  private AlloyRecipe(FluidStack out,FluidStack in_a,FluidStack in_b)
  {
    output = out;
    input_a = in_a;
    input_b = in_b;
  }
  
  /**
   * Register an Alloy Mixer recipe.
   * @param out Output (fluid type and amount).
   * @param in_a First input (fluid type and amount required).
   * @param in_b Second input (fluid type and amount required).
   */
  static public void RegisterRecipe(FluidStack out,FluidStack in_a,FluidStack in_b)
  {
    recipes.add(new AlloyRecipe(out,in_a,in_b));
  }
  
  /**
   * Find a valid recipe that contains the given inputs
   * A recipe is found if the recipe's inputs contains the fluid in the parameters.
   * @param in_a FluidStack for the first input.
   * @param in_b FluidStack for the second input.
   * @return
   */
  static public AlloyRecipe FindRecipe(FluidStack in_a,FluidStack in_b)
  {
    for(AlloyRecipe r:recipes)
    {
      if(in_a.containsFluid(r.input_a) && in_b.containsFluid(r.input_b))
      {
        return r;
      }
    }
    return null;
  }
  
}
