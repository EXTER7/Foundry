package exter.foundry.recipes;

import exter.foundry.api.recipe.ICastingRecipe;
import exter.foundry.api.recipe.matcher.IItemMatcher;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * Metal Caster recipe manager
 */
public class CastingRecipe implements ICastingRecipe
{
  private final FluidStack fluid;
  private final ItemStack mold;
  private final IItemMatcher extra;
  
  private final IItemMatcher output;
  
  private final int speed;
  
  @Override
  public FluidStack getInput()
  {
    return fluid.copy();
  }

  @Override
  public ItemStack getMold()
  {
    return mold.copy();
  }
  
  @Override
  public boolean containsExtra(ItemStack stack)
  {
    if(stack == null)
    {
      return extra == null;
    }
    return extra.apply(stack);
  }
  
  @Override
  public boolean requiresExtra()
  {
    return extra != null;
  }

  @Override
  public IItemMatcher getInputExtra()
  {
    return extra;
  }

  @Override
  public ItemStack getOutput()
  {
    return output.getItem();
  }

  public CastingRecipe(IItemMatcher result,FluidStack in_fluid,ItemStack in_mold,IItemMatcher in_extra,int cast_speed)
  {
    output = result;
    if(in_fluid == null)
    {
      throw new IllegalArgumentException("Casting recipe fluid cannot be null.");
    }
    fluid = in_fluid.copy();
    if(in_mold == null)
    {
      throw new IllegalArgumentException("Casting recipe mold cannot be null.");
    }
    mold = in_mold.copy();
    extra = in_extra;
    if(cast_speed < 1)
    {
      throw new IllegalArgumentException("Casting recipe speed must be > 0.");
    }
    speed = cast_speed;
  }
  
  @Override
  public boolean matchesRecipe(ItemStack mold_stack,FluidStack fluid_stack,ItemStack in_extra)
  {
    if(getOutput() == null)
    {
      return false;
    }
    return fluid_stack != null && fluid_stack.containsFluid(fluid) && mold_stack != null && mold.isItemEqual(mold_stack) && ItemStack.areItemStackTagsEqual(mold, mold_stack)
        && (extra == null || extra.apply(in_extra));
  }

  @Override
  public int getCastingSpeed()
  {
    return speed;
  }
}
