package exter.foundry.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import exter.foundry.item.FoundryItems;


/**
 * Utilities to manipulate foundry container item.
 */
public class FoundryContainer
{
  /**
   * Get the fluid stack in the container.
   * @param stack Container item.
   * @return FluidStack representing the container's content.
   */
  public static FluidStack GetFluidStack(ItemStack stack)
  {
    if(stack.itemID != FoundryItems.item_container.itemID || stack.stackTagCompound == null)
    {
      return null;
    }
    return FluidStack.loadFluidStackFromNBT(stack.stackTagCompound);
  }

  private static void SetFluidNBT(ItemStack is, FluidStack fluid)
  {
    is.stackTagCompound = new NBTTagCompound();
    if(fluid != null)
    {
      fluid.writeToNBT(is.stackTagCompound);
    }
  }

  /**
   * Create a Container from a fluid stack.
   * Note: if the fluid amount is greater than 1000mB, the returned container will contain 1000mB of fluid.
   * @param fluid FluidStack to use.
   * @return ItemStack representing the container.
   */
  public static ItemStack FromFluidStack(FluidStack fluid)
  {
    ItemStack stack = new ItemStack(FoundryItems.item_container.itemID, 1, 0);
    if(fluid == null)
    {
      return stack;
    }
    if(fluid.amount > FluidContainerRegistry.BUCKET_VOLUME)
    {
      fluid = new FluidStack(fluid, FluidContainerRegistry.BUCKET_VOLUME);
    }
    SetFluidNBT(stack, fluid);
    return stack;
  }

  /**
   * Fill a container.
   * @param stack Container item.
   * @param fluid fluid to fill.
   * @param do_fill true to actually fill the container, false to simulate.
   * @return Amount of fluid filled.
   */
  public static int Fill(ItemStack stack, FluidStack fluid, boolean do_fill)
  {
    if(stack.itemID != FoundryItems.item_container.itemID || fluid == null)
    {
      return 0;
    }

    FluidStack container_fluid = GetFluidStack(stack);

    if(!do_fill)
    {
      if(container_fluid == null)
      {
        return Math.min(FluidContainerRegistry.BUCKET_VOLUME, fluid.amount);
      }

      if(!container_fluid.isFluidEqual(fluid))
      {
        return 0;
      }

      return Math.min(FluidContainerRegistry.BUCKET_VOLUME - container_fluid.amount, fluid.amount);
    }

    if(container_fluid == null)
    {
      container_fluid = new FluidStack(fluid, Math.min(FluidContainerRegistry.BUCKET_VOLUME, fluid.amount));

      SetFluidNBT(stack, container_fluid);
      return container_fluid.amount;
    }

    if(!container_fluid.isFluidEqual(fluid))
    {
      return 0;
    }
    int filled = FluidContainerRegistry.BUCKET_VOLUME - container_fluid.amount;

    if(fluid.amount < filled)
    {
      container_fluid.amount += fluid.amount;
      filled = fluid.amount;
    } else
    {
      container_fluid.amount = FluidContainerRegistry.BUCKET_VOLUME;
    }
    SetFluidNBT(stack, container_fluid);
    return filled;
  }

  /**
   * Drain a container.
   * @param stack Container item.
   * @param amount amount to drain
   * @param do_drain true to actually drain the container, false to simulate.
   * @return Drained fluid.
   */
  public static FluidStack Drain(ItemStack stack, int amount, boolean do_drain)
  {
    if(stack.itemID != FoundryItems.item_container.itemID)
    {
      return null;
    }
    FluidStack fluid = GetFluidStack(stack);

    if(fluid == null)
    {
      return null;
    }

    int drained = amount;
    if(fluid.amount < drained)
    {
      drained = fluid.amount;
    }

    FluidStack drain_fluid = new FluidStack(fluid, drained);
    if(do_drain)
    {
      fluid.amount -= drained;
      if(fluid.amount <= 0)
      {
        fluid = null;
      }
      SetFluidNBT(stack, fluid);

    }
    return drain_fluid;
  }
}
