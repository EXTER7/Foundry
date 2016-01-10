package exter.foundry.api.recipe;

import java.util.List;

import net.minecraftforge.fluids.FluidStack;

public interface IAlloyMixerRecipe
{
  /**
   * Deprecated, use getInputs() instead.
   * Get the one of the recipe's inputs by index.
   * @param in index of the input.
   * @return Recipe's input.
   */
  @Deprecated
  public FluidStack getInput(int in);
  
  /**
   * Deprecated, use getInputs() instead.
   * Get the amount of recipe's inputs.
   * @return Amount of recipe's inputs ranging from 1 to 4.
   */
  @Deprecated
  public int getInputCount();

  /**
   * Get the recipe's inputs.
   * @return Recipe's inputs.
   */
  public List<FluidStack> getInputs();

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
