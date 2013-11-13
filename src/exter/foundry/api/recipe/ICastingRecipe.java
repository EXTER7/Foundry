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
   * @return ItemStack containing the required extra item, or null if no extra item is required.
   */

  public ItemStack GetInputExtra();

  public Object GetOutput();


}
