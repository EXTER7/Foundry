package exter.foundry.api.recipe;


import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface IAtomizerRecipe
{

  /**
   * Get the fluid required for atomizing.
   * @return FluidStack containing the required fluid.
   */
  public FluidStack getInput();

  /**
   * Get the item produced by atomizing.
   * @return the produced item can be an ItemStack or a String with an Ore Dictionary name.
   */
  public Object getOutput();

  public boolean matchesRecipe(FluidStack fluid_stack);
  
  /**
   * Get the actual item produced by atomizing.
   * @return ItemStack containing the item produced. Can be null if using an Ore Dictionary name with nothing registered with it.
   */
  public ItemStack getOutputItem();

}
