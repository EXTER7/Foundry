package exter.foundry.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import exter.foundry.slot.SlotOutput;
import exter.foundry.tileentity.TileEntityMaterialRouter;

public class ContainerMaterialRouter extends Container
{

  private TileEntityMaterialRouter te_router;
  
  // Slot numbers
  private static final int SLOTS_TE = 0;
  //private static final int SLOTS_TE_SIZE = 3;
  private static final int SLOTS_INVENTORY = 9;
  private static final int SLOTS_HOTBAR = 9 + 3 * 9;

  private static final int SLOT_INVENTORY_X = 32;
  private static final int SLOT_INVENTORY_Y = 147;

  private static final int SLOT_HOTBAR_X = 32;
  private static final int SLOT_HOTBAR_Y = 205;

  public ContainerMaterialRouter(TileEntityMaterialRouter router, IInventory player_inventory)
  {
    te_router = router;
    te_router.openInventory();
    int i,j;

    for(i = 0; i < TileEntityMaterialRouter.SLOT_OUTPUT; i++)
    {
      addSlotToContainer(new Slot(router, i, 7, 19 + i * 18));
    }
    for(i = 0; i < 6; i++)
    {
      addSlotToContainer(new SlotOutput(router, i + TileEntityMaterialRouter.SLOT_OUTPUT, 7, 90 + i * 18));
    }

    //Player Inventory
    for(i = 0; i < 3; ++i)
    {
      for(j = 0; j < 9; ++j)
      {
        addSlotToContainer(new Slot(player_inventory, j + i * 9 + 9, SLOT_INVENTORY_X + j * 18, SLOT_INVENTORY_Y + i * 18));
      }
    }
    for(i = 0; i < 9; ++i)
    {
      addSlotToContainer(new Slot(player_inventory, i, SLOT_HOTBAR_X + i * 18, SLOT_HOTBAR_Y));
    }
  }

  public boolean canInteractWith(EntityPlayer par1EntityPlayer)
  {
    return te_router.isUseableByPlayer(par1EntityPlayer);
  }

  public ItemStack transferStackInSlot(EntityPlayer player, int slot_index)
  {
    ItemStack slot_stack = null;
    Slot slot = (Slot) inventorySlots.get(slot_index);

    if (slot != null && slot.getHasStack())
    {
      ItemStack stack = slot.getStack();
      slot_stack = stack.copy();

      if (slot_index >= SLOTS_INVENTORY && slot_index < SLOTS_HOTBAR)
      {
        if (!mergeItemStack(stack, SLOTS_TE, SLOTS_TE + 1, false))
        {
          return null;
        }
      } else if (slot_index >= SLOTS_HOTBAR && slot_index < SLOTS_HOTBAR + 9)
      {
        if (!mergeItemStack(stack, SLOTS_INVENTORY, SLOTS_INVENTORY + 3 * 9, false))
        {
          return null;
        }
      } else if (!mergeItemStack(stack, SLOTS_INVENTORY, SLOTS_HOTBAR + 9, false))
      {
        return null;
      }

      if (stack.stackSize == 0)
      {
        slot.putStack((ItemStack) null);
      } else
      {
        slot.onSlotChanged();
      }

      if (stack.stackSize == slot_stack.stackSize)
      {
        return null;
      }

      slot.onPickupFromSlot(player, stack);
    }

    return slot_stack;
  }

  @Override
  public void onContainerClosed(EntityPlayer par1EntityPlayer)
  {
    super.onContainerClosed(par1EntityPlayer);
    te_router.closeInventory();
  }
}