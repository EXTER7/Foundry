package exter.foundry.api.container;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

/**
 * API for Manipulating Refractory Fluid Containers
 * Deprecated, use {@link net.minecraftforge.fluids.IFluidContainerItem} to interface with refractory fluid containers
 */
@Deprecated
public interface IRefractoryFluidContainerHandler
{
  /**
   * Get the contents of a container
   * @param stack Container item.
   * @return Contents of the container.
   */
  public FluidStack GetFluidStack(ItemStack stack);


  /**
   * Create a Container from a fluid stack.
   * Note: if the fluid amount is greater than 1000mB, the returned container will contain 1000mB of fluid.
   * @param fluid FluidStack to use.
   * @return ItemStack representing the container.
   */
  public ItemStack FromFluidStack(FluidStack fluid);

  /**
   * Fill a container.
   * @param stack Container item.
   * @param fluid fluid to fill.
   * @param do_fill true to actually fill the container, false to simulate.
   * @return Amount of fluid filled.
   */
  public int Fill(ItemStack stack, FluidStack fluid, boolean do_fill);

  /**
   * Drain a container.
   * @param stack Container item.
   * @param amount amount to drain
   * @param do_drain true to actually drain the container, false to simulate.
   * @return Drained fluid.
   */
  public FluidStack Drain(ItemStack stack, int amount, boolean do_drain);

  /**
   * Check if an item is a container.
   * @return true if the item is a container, false otherwise.
   */
  public boolean IsItemContainer(ItemStack stack);
}
