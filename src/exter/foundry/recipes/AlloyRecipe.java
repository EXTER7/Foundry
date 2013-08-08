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

  public FluidStack GetInputA()
  {
    return input_a.copy();
  }
  
  public FluidStack GetInputB()
  {
    return input_b.copy();
  }

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
  
  static public void RegisterRecipe(FluidStack out,FluidStack in_a,FluidStack in_b)
  {
    recipes.add(new AlloyRecipe(out,in_a,in_b));
  }
  
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
