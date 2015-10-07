package exter.foundry.api.recipe;

import exter.foundry.api.substance.InfuserSubstance;
import net.minecraft.item.ItemStack;

public interface IInfuserSubstanceRecipe
{
  /**
   * Get the required item.
   * @return If the recipe used the Ore Dictionary, a {@link String} of it's name, an {@link ItemStack} of the required item otherwise.
   */
  public Object getInput();
  
  /**
   * Get the substance produced.
   * @return The substance produced.
   */
  public InfuserSubstance getOutput();
  
  /**
   * Get the amount of energy needed.
   * @return The amount of energy needed in (10 Energy = 1 RF, 40 Energy = 1 EU).
   */
  public int getEnergyNeeded();
  
  /**
   * Check if an item matches a substance recipe.
   * @param item_stack The item to check.
   * @return true if the item matches, false otherwise.
   */
  public boolean matchesRecipe(ItemStack item_stack);
}
