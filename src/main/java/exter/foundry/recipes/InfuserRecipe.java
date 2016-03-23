package exter.foundry.recipes;

import exter.foundry.api.FoundryUtils;
import exter.foundry.api.recipe.IInfuserRecipe;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
   * Item required.
   */
  public final Object item;

  /**
   * Amount of energy needed to extract.
   */
  public final int extract_energy;
  

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
  public Object getInput()
  {
    if(item instanceof ItemStack)
    {
      return ((ItemStack)item).copy();
    }
    return item;
  }
  
  @Override
  public FluidStack getOutput()
  {
    return output.copy();
  }

  public InfuserRecipe(FluidStack result,FluidStack in_fluid,Object in_item,int energy)
  {
    if(!(in_item instanceof ItemStack) && !(in_item instanceof String) && !(in_item instanceof Item) && !(in_item instanceof Block))
    {
      throw new IllegalArgumentException("Infuser substance recipe item is not of a valid class.");
    }
    if(energy < 1)
    {
      throw new IllegalArgumentException("Infuser substance recipe energy nust be > 0.");
    }
    if(in_fluid == null)
    {
      throw new IllegalArgumentException("Infuser recipe input cannot be null");
    }
    if(result == null)
    {
      throw new IllegalArgumentException("Infuser recipe output cannot be null");
    }
    if(in_item instanceof ItemStack)
    {
      item = ((ItemStack) in_item).copy();
    } else
    {
      item = in_item;
    }
    fluid = in_fluid.copy();
    extract_energy = energy;
    output = result.copy();
  }
  
  @Override
  public boolean matchesRecipe(FluidStack in_fluid,ItemStack item_stack)
  {
    return FoundryUtils.isItemMatch(item_stack, item) && in_fluid.containsFluid(fluid);
  }
  
  @Override
  public int getEnergyNeeded()
  {
    return extract_energy;
  }
  
}
