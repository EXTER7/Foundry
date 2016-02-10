package exter.foundry.recipes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import exter.foundry.api.recipe.IAlloyMixerRecipe;
import net.minecraftforge.fluids.FluidStack;

/*
 * Alloy Mixer recipe manager
 */
public class AlloyMixerRecipe implements IAlloyMixerRecipe
{
  
  public List<FluidStack> inputs;
  public FluidStack output;

  @Override
  public FluidStack getOutput()
  {
    return output.copy();
  }
  
  public AlloyMixerRecipe(FluidStack out,FluidStack[] in)
  {
    output = out.copy();
    if(in == null)
    {
      throw new IllegalArgumentException("Alloy mixer recipe inputs cannot be null");
    }
    if(in.length > 4)
    {
      throw new IllegalArgumentException("Alloy mixer recipe cannot have more the 4 inputs");
    }
    inputs = new ArrayList<FluidStack>();
    int i;
    for(i = 0; i < in.length; i++)
    {
      if(in[i] == null)
      {
        throw new IllegalArgumentException("Alloy mixer recipe input cannot be null");
      }
      inputs.add(in[i].copy());
    }
    inputs = Collections.unmodifiableList(inputs);
  }

  @Override
  public List<FluidStack> getInputs()
  {
    return inputs;
  }
  
  static private final boolean[] matched = new boolean[4];
  
  @Override
  public boolean matchesRecipe(FluidStack[] in,int[] order)
  {
    int matches = 0;
    int i;
    if(order != null && order.length < inputs.size())
    {
      order = null;
    }
    
    if(in.length < inputs.size())
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
        for(j = 0; j < inputs.size(); j++)
        {
          if(!matched[j] && in[i].containsFluid(inputs.get(j)))
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
    return matches == inputs.size();
  }


}
