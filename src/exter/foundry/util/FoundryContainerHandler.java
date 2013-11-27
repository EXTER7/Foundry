package exter.foundry.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import exter.foundry.api.container.IRefractoryFluidContainerHandler;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemRefractoryFluidContainer;

/**
 * Utilities to manipulate foundry container item.
 */
@Deprecated
public class FoundryContainerHandler implements IRefractoryFluidContainerHandler
{
  
  static public FoundryContainerHandler instance = new FoundryContainerHandler();

  private FoundryContainerHandler()
  {
    
  }

  @Override
  public FluidStack GetFluidStack(ItemStack stack)
  {
    if(!IsItemContainer(stack))
    {
      return null;
    }
    return ((ItemRefractoryFluidContainer)stack.getItem()).getFluid(stack);
  }

  @Override
  public ItemStack FromFluidStack(FluidStack fluid)
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
    ((ItemRefractoryFluidContainer)stack.getItem()).fill(stack, fluid, true);
    return stack;
  }

  @Override
  public int Fill(ItemStack stack, FluidStack fluid, boolean do_fill)
  {
    if(!IsItemContainer(stack))
    {
      return 0;
    }
    return ((ItemRefractoryFluidContainer)stack.getItem()).fill(stack, fluid, do_fill);
  }

  @Override
  public FluidStack Drain(ItemStack stack, int amount, boolean do_drain)
  {
    if(!IsItemContainer(stack))
    {
      return null;
    }
    return ((ItemRefractoryFluidContainer)stack.getItem()).drain(stack, amount, do_drain);
  }

  @Override
  public boolean IsItemContainer(ItemStack stack)
  {
    return stack.itemID == FoundryItems.item_container.itemID;
  }
}
