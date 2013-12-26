package exter.foundry.recipes;

import java.util.ArrayList;
import java.util.List;

import exter.foundry.ModFoundry;
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
    for(i = 0; i < in.length; i++)
    {
      if(in[i] != null)
      {
        int j;
        for(j = 0; j < inputs.length; j++)
        {
          if(in[i].containsFluid(inputs[j]))
          {
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
