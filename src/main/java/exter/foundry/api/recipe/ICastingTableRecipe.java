package exter.foundry.api.recipe;

import exter.foundry.api.recipe.matcher.IItemMatcher;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface ICastingTableRecipe
{
  public enum TableType
  {
    INGOT,
    PLATE,
    ROD,
    BLOCK
  }

  /**
   * Get the fluid required for casting.
   * @return FluidStack containing the required fluid.
   */
  public FluidStack getInput();

  /**
   * Get the Casting Table type.
   */
  public TableType getTableType();
  
  /**
   * Get the actual item produced by casting.
   * @return ItemStack containing the item produced. Can be null if using an Ore Dictionary name with nothing registered with it.
   */
  public ItemStack getOutput();

  /**
   * Get the output's matcher.
   */
  public IItemMatcher getOutputMatcher();
}
