package exter.foundry.api.recipe;

import net.minecraftforge.fluids.FluidStack;

public interface IAlloyMixerRecipe
{
  /**
   * Get the one of the recipe's inputs by index.
   * @param in index of the input.
   * @return Recipe's input.
   */
  public FluidStack getInput(int in);
  
  /**
   * Get the amount of recipe's inputs.
   * @return Amount of recipe's inputs ranging from 1 to 4.
   */
  public int getInputCount();

  /**
   * Get the recipe's output.
   * @return FluidStack containing Recipe's produced fluid and amount.
   */
  public FluidStack getOutput();

  /**
   * Check if a list of fluid stacks matches this recipe.
   * @param in list of fluid stack to compare.
   * @param order [Output] Order in which the input fluids are matched.
   * @return true if the fluids matches, false otherwise.
   */
  public boolean matchesRecipe(FluidStack[] inputs,int[] order);
}
