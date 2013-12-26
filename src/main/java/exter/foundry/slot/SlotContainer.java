package exter.foundry.slot;

import exter.foundry.item.FoundryItems;
import exter.foundry.recipes.CastingRecipe;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.IFluidContainerItem;

public class SlotContainer extends Slot
{
  public SlotContainer(IInventory inventory, int par2, int par3, int par4)
  {
    super(inventory, par2, par3, par4);
  }
  
  @Override
  public boolean isItemValid(ItemStack stack)
  {
    ItemStack slot_stack = getStack();
    return (stack.getItem() instanceof IFluidContainerItem) && (slot_stack == null || slot_stack.stackSize == 0);
  }

  @Override
  public int getSlotStackLimit()
  {
    return 1;
  }
}
