package exter.foundry.api.recipe;

import exter.foundry.api.recipe.matcher.IItemMatcher;
import net.minecraft.item.ItemStack;

public interface IAlloyFurnaceRecipe
{
  /**
   * Get the recipe's input A by index.
   * @return Recipe's input A.
   */
  public IItemMatcher getInputA();

  /**
   * Get the recipe's input B by index.
   * @return Recipe's input B.
   */
  public IItemMatcher getInputB();

  /**
   * Get the recipe's output.
   * @return ItemStack containing recipe's output.
   */
  public ItemStack getOutput();

  /**
   * Check if the items matches this recipe.
   * @param input_a item to compare.
   * @param input_b item to compare.
   * @return true if the items matches, false otherwise.
   */
  public boolean matchesRecipe(ItemStack input_a,ItemStack input_b);
}
