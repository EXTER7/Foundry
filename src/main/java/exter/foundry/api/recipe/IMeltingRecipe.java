package exter.foundry.api.recipe;

import exter.foundry.api.recipe.matcher.IItemMatcher;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface IMeltingRecipe
{
  /**
   * Get the required item.
   * @return If the recipe used the Ore Dictionary, a {@link String} of it's name, an {@link ItemStack} of the required item otherwise.
   */
  public IItemMatcher getInput();

  /**
   * Get the produced fluid.
   * @return The fluid that the recipe produces.
   */
  public FluidStack getOutput();
  
  /**
   * Get the melting temperature of the item.
   * @return Melting temperature in K.
   */
  public int getMeltingPoint();
  
  /**
   * Check if an item matches this recipe.
   * @param item The item to check.
   * @return true, if the item matches, false otherwise.
   */
  public boolean matchesRecipe(ItemStack item);
  
  /**
   * Get the melting speed.
   * @return The melting speed.
   */
  public int getMeltingSpeed();
}
