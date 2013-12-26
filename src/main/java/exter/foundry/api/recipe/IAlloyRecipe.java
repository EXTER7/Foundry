package exter.foundry.api.recipe;

import net.minecraftforge.fluids.FluidStack;

public interface IAlloyRecipe
{
  /**
   * Get the one of the recipe's inputs by index.
   * @param in index of the input.
   * @return Amount of recipe's inputs ranging from 1 to 4.
   */
  public FluidStack GetInput(int in);
  
  /**
   * Get the amount of recipe's inputs.
   * @return Amount of recipe's inputs ranging from 1 to 4.
   */
  public int GetInputCount();

  /**
   * Get the recipe's output.
   * @return FluidStack containing Recipe's produced fluid and amount.
   */
  public FluidStack GetOutput();

  /**
   * Check if a list of fluid stacks matches this recipe.
   * @param in list of fluid stack to compare.
   * @param [Output] order in which the input fluids are matched.
   * @return true if the fluids matches, false otherwise.
   */
  public boolean MatchesRecipe(FluidStack[] inputs,int[] order);
}
