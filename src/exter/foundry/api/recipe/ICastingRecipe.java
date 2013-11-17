package exter.foundry.api.recipe;

import java.util.List;

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


}
