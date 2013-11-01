package exter.foundry.container;

import exter.foundry.tileentity.TileEntityMetalInfuser;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerMetalInfuser extends Container
{
  

  private TileEntityMetalInfuser te_infuser;
  
  // Slot numbers
  private static final int SLOTS_SUBSTANCE_ITEM = 0;
  private static final int SLOTS_INVENTORY = 1;
  private static final int SLOTS_HOTBAR = 1 + 3 * 9;

  private static final int SLOT_SUBSTANCE_ITEM_X = 19;
  private static final int SLOT_SUBSTANCE_ITEM_Y = 59;

  private static final int SLOT_INVENTORY_X = 8;
  private static final int SLOT_INVENTORY_Y = 84;

  private static final int SLOT_HOTBAR_X = 8;
  private static final int SLOT_HOTBAR_Y = 142;

  public ContainerMetalInfuser(TileEntityMetalInfuser infuser, IInventory player_inventory)
  {
    te_infuser = infuser;
    te_infuser.openChest();
    int i,j;

    addSlotToContainer(new Slot(te_infuser, 0, SLOT_SUBSTANCE_ITEM_X, SLOT_SUBSTANCE_ITEM_Y));

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
    return te_infuser.isUseableByPlayer(par1EntityPlayer);
  }

  public ItemStack transferStackInSlot(EntityPlayer player, int slot_index)
  {
    ItemStack slot_stack = null;
    Slot slot = (Slot) inventorySlots.get(slot_index);

    if (slot != null && slot.getHasStack())
    {
      ItemStack stack = slot.getStack();
      slot_stack = stack.copy();

      if (slot_index == SLOTS_SUBSTANCE_ITEM)
      {
        if (!mergeItemStack(stack, SLOTS_INVENTORY, SLOTS_INVENTORY + 3 * 9, false))
        {
          return null;
        }
      } else if (slot_index >= SLOTS_INVENTORY && slot_index < SLOTS_HOTBAR)
      {
        if (!mergeItemStack(stack, SLOTS_SUBSTANCE_ITEM, SLOTS_SUBSTANCE_ITEM, false))
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
    te_infuser.closeChest();
  }

  @Override
  public void detectAndSendChanges()
  {
    super.detectAndSendChanges();

    for(int i = 0; i < crafters.size(); i++)
    {
      te_infuser.SendGUINetworkData(this, (ICrafting) crafters.get(i));
    }
  }
  
  @Override
  public void updateProgressBar(int i, int j)
  {
    te_infuser.GetGUINetworkData(i, j);
  }

}
