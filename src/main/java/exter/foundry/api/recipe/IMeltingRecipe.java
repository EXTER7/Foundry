package exter.foundry.api.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface IMeltingRecipe
{
  /**
   * Get the required item.
   * @return If the recipe used the Ore Dictionary, a {@link String} of it's name, an {@link ItemStack} of the required item otherwise.
   */
  public Object GetInput();

  /**
   * Get the produced fluid.
   * @return The fluid that the recipe produces.
   */
  public FluidStack GetOutput();
  
  /**
   * Get the melting temperature of the item.
   * @return Melting temperature in K.
   */
  public int GetMeltingPoint();
  
  /**
   * Check if an item matches this recipe.
   * @param item The item to check.
   * @return true, if the item matches, false otherwise.
   */
  public boolean MatchesRecipe(ItemStack item);
  
  public int GetMeltingSpeed();
}
