package exter.foundry.recipes;

import exter.foundry.api.FoundryUtils;
import exter.foundry.api.recipe.IMeltingRecipe;
import exter.foundry.api.recipe.matcher.IItemMatcher;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * Metal Smelter recipe manager
 */
public class MeltingRecipe implements IMeltingRecipe
{
  /**
   * Produced fluid and amount.
   */
  private final FluidStack fluid;

  /**
   * Item required.
   * It can be an {@link ItemStack} of the item or a @{link String} of it's Ore Dictionary name.
   */
  private final IItemMatcher solid;
  
  /**
   * Melting point of the item in K.
   */
  private final int melting_point;
  
  
  private final int melting_speed;
  
  
  public MeltingRecipe(IItemMatcher item,FluidStack fluid_stack, int melt, int speed)
  {
    solid = item;
    if(fluid_stack == null)
    {
      throw new IllegalArgumentException("Melting recipe fluid cannot be null.");
    }
    fluid = fluid_stack.copy();
    if(melt <= 295)
    {
      throw new IllegalArgumentException("Melting recipe melting point must be > 295.");
    }
    melting_point = melt;
    if(speed < 1)
    {
      throw new IllegalArgumentException("Melting recipe speed must be > 0.");
    }
    melting_speed = speed;
  }
  
  public IItemMatcher getInput()
  {
    return solid;
  }
  
  public FluidStack getOutput()
  {
    return fluid.copy();
  }

  @Override
  public int getMeltingPoint()
  {
    return melting_point;
  }
  
  @Override
  public boolean matchesRecipe(ItemStack item)
  {
    return solid.apply(item);
  }

  @Override
  public int getMeltingSpeed()
  {
    return melting_speed;
  }
}
