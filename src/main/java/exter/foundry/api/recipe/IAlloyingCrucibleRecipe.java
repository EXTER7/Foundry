package exter.foundry.api.recipe;


import net.minecraftforge.fluids.FluidStack;

public interface IAlloyingCrucibleRecipe
{
  /**
   * Get the recipe's input A.
   */
  public FluidStack getInputA();

  /**
   * Get the recipe's input B.
   */
  public FluidStack getInputB();

  /**
   * Get the recipe's output.
   */
  public FluidStack getOutput();

  /**
   * Check if the fluids matches this recipe.
   * @param input_a fluid to compare.
   * @param input_b fluid to compare.
   * @return true if the fluids matches, false otherwise.
   */
  public boolean matchesRecipe(FluidStack input_a,FluidStack input_b);
}
