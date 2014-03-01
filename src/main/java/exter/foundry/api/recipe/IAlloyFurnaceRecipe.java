package exter.foundry.api.recipe;

import net.minecraft.item.ItemStack;

public interface IAlloyFurnaceRecipe
{
  /**
   * Get the one of the recipe's inputs by index.
   * @param in index of the input. Can be 0 or 1.
   * @return Recipe's input.
   */
  public Object GetInputA();

  public Object GetInputB();

  /**
   * Get the recipe's output.
   * @return FluidStack containing Recipe's produced fluid and amount.
   */
  public ItemStack GetOutput();

  /**
   * Check if a list of fluid stacks matches this recipe.
   * @param in list of fluid stack to compare.
   * @param order [Output] Order in which the input fluids are matched.
   * @return true if the fluids matches, false otherwise.
   */
  public boolean MatchesRecipe(ItemStack input_a,ItemStack input_b);
}
