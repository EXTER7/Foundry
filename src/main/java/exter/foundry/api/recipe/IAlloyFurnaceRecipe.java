package exter.foundry.api.recipe;

import net.minecraft.item.ItemStack;

public interface IAlloyFurnaceRecipe
{
  /**
   * Get the recipe's input A by index.
   * @return Recipe's input A.
   */
  public Object GetInputA();

  /**
   * Get the recipe's input B by index.
   * @return Recipe's input B.
   */
  public Object GetInputB();

  /**
   * Get the recipe's output.
   * @return ItemStack containing recipe's output.
   */
  public ItemStack GetOutput();

  /**
   * Check if the items matches this recipe.
   * @param input_a item to compare.
   * @param input_b item to compare.
   * @return true if the items matches, false otherwise.
   */
  public boolean MatchesRecipe(ItemStack input_a,ItemStack input_b);
}
