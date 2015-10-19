package exter.foundry.api.recipe;


import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface ICastingRecipe
{

  /**
   * Get the fluid required for casting.
   * @return FluidStack containing the required fluid.
   */
  public FluidStack getInput();

  /**
   * Get the mold required for casting.
   * @return ItemStack containing the required mold.
   */

  public ItemStack getMold();

  /**
   * Get the extra item required for casting.
   * @return Can be an {@link ItemStack} containing the required extra item, a {@link OreStack}, or null if no extra item is required.
   */
  public Object getInputExtra();

  /**
   * Get the item produced by casting.
   * @return the produced item can be an ItemStack or a String with an Ore Dictionary name.
   */
  public Object getOutput();

  /**
   * Get the casting speed.
   * @return The casting speed.
   */
  public int getCastingSpeed();

  /**
   * Check if a fluid stack and mold matches this recipe.
   * @param mold_stack mold to check.
   * @param fluid_stack fluid to check (must contain the fluid in the recipe).
   * @return true if the stack and mold matches, false otherwise.
   */
  public boolean matchesRecipe(ItemStack mold_stack,FluidStack fluid_stack,ItemStack extra);
  
  /**
   * Check if the item stack contains the necessary extra items for this recipe.
   * @param stack the stack to check.
   * @return true if the stack contains the recipe's extra item requirement.
   */
  public boolean containsExtra(ItemStack stack);
  
  /**
   * Return true if the recipe requires an extra item.
   */
  public boolean requiresExtra();

  /**
   * Get the actual item produced by casting.
   * @return ItemStack containing the item produced. Can be null if using an Ore Dictionary name with nothing registered with it.
   */
  public ItemStack getOutputItem();

}
