package exter.foundry.recipes;

import exter.foundry.api.FoundryUtils;
import exter.foundry.api.recipe.IMeltingRecipe;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
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
  private final Object solid;
  
  /**
   * Melting point of the item in K.
   */
  private final int melting_point;
  
  
  private final int melting_speed;
  
  
  public MeltingRecipe(Object item,FluidStack fluid_stack, int melt, int speed)
  {
    if(!(item instanceof ItemStack) && !(item instanceof String) && !(item instanceof Item) && !(item instanceof Block))
    {
      throw new IllegalArgumentException("Melting recipe item is not of a valid class.");
    }
    if(item instanceof ItemStack)
    {
      solid = ((ItemStack)item).copy();
    } else
    {
      solid = item;
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
  
  public Object getInput()
  {
    if(solid instanceof ItemStack)
    {
      return ((ItemStack)solid).copy();
    }
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
    return FoundryUtils.isItemMatch(item, solid);
  }

  @Override
  public int getMeltingSpeed()
  {
    return melting_speed;
  }
}
