package exter.foundry.api.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface ICastingTableRecipe
{
  public enum TableType
  {
    INGOT,
    PLATE,
    ROD
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

}
