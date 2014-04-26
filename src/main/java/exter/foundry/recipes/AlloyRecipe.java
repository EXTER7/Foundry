package exter.foundry.recipes;

import exter.foundry.api.recipe.IAlloyRecipe;
import net.minecraftforge.fluids.FluidStack;

/*
 * Alloy Mixer recipe manager
 */
public class AlloyRecipe implements IAlloyRecipe
{
  
  public FluidStack[] inputs;
  public FluidStack output;

  @Override
  public FluidStack GetInput(int in)
  {
    return inputs[in].copy();
  }
  
  @Override
  public int GetInputCount()
  {
    return inputs.length;
  }


  @Override
  public FluidStack GetOutput()
  {
    return output.copy();
  }
  
  public AlloyRecipe(FluidStack out,FluidStack[] in)
  {
    output = out.copy();
    int i;
    if(in.length > 4)
    {
      throw new IllegalArgumentException("Alloy recipe cannot have more the 4 inputs");
    }
    inputs = new FluidStack[in.length];
    for(i = 0; i < in.length; i++)
    {
      inputs[i] = in[i].copy();
    }
  }

  static private final boolean[] matched = new boolean[4];
  
  @Override
  public boolean MatchesRecipe(FluidStack[] in,int[] order)
  {
    int matches = 0;
    int i;
    if(order != null && order.length < inputs.length)
    {
      order = null;
    }
    
    if(in.length < inputs.length)
    {
      return false;
    }
    
    for(i = 0; i < 4; i++)
    {
      matched[i] = false;
    }
    
    for(i = 0; i < in.length; i++)
    {
      if(in[i] != null)
      {
        int j;
        for(j = 0; j < inputs.length; j++)
        {
          if(!matched[j] && in[i].containsFluid(inputs[j]))
          {
            matched[j] = true;
            matches++;
            if(order != null)
            {
              order[j] = i;
            }
            break;
          }
        }
      }
    }
    return matches == inputs.length;
  }
}
