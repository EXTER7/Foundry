package exter.foundry.api.recipe;


import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface ICastingRecipe
{

  /**
   * Get the fluid required for casting.
   * @return FluidStack containing the required fluid.
   */
  public FluidStack GetInputFluid();

  /**
   * Get the mold required for casting.
   * @return ItemStack containing the required mold.
   */

  public ItemStack GetInputMold();

  /**
   * Get the extra item required for casting.
   * @return Can be an {@link ItemStack} containing the required extra item, a {@link String} of the Ore Dictionary name of the item, or null if no extra item is required.
   */
  public Object GetInputExtra();

  
  /**
   * Get the amount of the extra item required for casting.
   * @return The amount of the required extra item.
   */
  public int GetInputExtraAmount();

  /**
   * Get the item produced by casting.
   * @return ItemStack containing the required extra item, or null if no extra item is required.
   */
  public Object GetOutput();

  /**
   * Check if a fluid stack and mold matches this recipe.
   * @param mold_stack mold to check.
   * @param fluid_stack fluid to check (must contain the fluid in the recipe).
   * @return true if the stack and mold matches, false otherwise.
   */
  public boolean MatchesRecipe(ItemStack mold_stack,FluidStack fluid_stack);
  
  public boolean ContainsExtra(ItemStack stack);
  
  public boolean RequiresExtra();

  /**
   * Get the actual item produced by casting.
   * @return ItemStack containing the item produced. Can be null if using an Ore Dictionary name with nothing registered with it.
   */
  public ItemStack GetOutputItem();

}
