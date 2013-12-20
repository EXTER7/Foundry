package exter.foundry.api.recipe;

import net.minecraft.item.ItemStack;

public interface IInfuserSubstanceRecipe
{
  /**
   * Get the required item.
   * @return If the recipe used the Ore Dictionary, a {@link String} of it's name, an {@link ItemStack} of the required item otherwise.
   */
  public Object GetInputItem();
  
  /**
   * Get the substance type produced.
   * @return The substance type.
   */
  public String GetOutputSubstanceType();
  
  /**
   * Get the substance amount produced.
   * @return The substance amount.
   */
  public int GetOutputSubstanceAmount();
  
  /**
   * Get the amount of energy needed.
   * @return The amount of energy needed in (100 Energy = 1 MJ, 10 Energy = 1 RF, 40 Energy = 1 EU).
   */
  public int GetEnergyNeeded();
  
  /**
   * Check if an item matches a substance recipe.
   * @param item_stack The item to check.
   * @return true if the item matches, false otherwise.
   */
  public boolean MatchesRecipe(ItemStack item_stack);
}
