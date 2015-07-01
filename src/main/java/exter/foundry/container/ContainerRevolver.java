package exter.foundry.container;


import exter.foundry.inventory.InventoryRevolver;
import exter.foundry.slot.SlotLocked;
import exter.foundry.slot.SlotRevolverAmmo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerRevolver extends Container
{
  IInventory inventory;
  private ItemStack revolver;
  InventoryRevolver rev_inv;
  // Slot numbers
  private static final int SLOTS_REVOLVER = 0;
  private static final int SLOTS_INVENTORY = SLOTS_REVOLVER + 8;
  private static final int SLOTS_HOTBAR = SLOTS_INVENTORY + 3 * 9;

  public ContainerRevolver(ItemStack revolver_item,InventoryPlayer inventory_player)
  {
    revolver = revolver_item;
    inventory = inventory_player;


    rev_inv = new InventoryRevolver(revolver,inventory_player);
    int position = revolver.getTagCompound().getInteger("position");
    addSlotToContainer(new SlotRevolverAmmo(rev_inv, position,            80,  30));
    addSlotToContainer(new SlotRevolverAmmo(rev_inv, (position + 1) % 8, 106,  39));
    addSlotToContainer(new SlotRevolverAmmo(rev_inv, (position + 2) % 8, 115,  65));
    addSlotToContainer(new SlotRevolverAmmo(rev_inv, (position + 3) % 8, 106,  91));
    addSlotToContainer(new SlotRevolverAmmo(rev_inv, (position + 4) % 8,  80, 100));
    addSlotToContainer(new SlotRevolverAmmo(rev_inv, (position + 5) % 8,  54,  91));
    addSlotToContainer(new SlotRevolverAmmo(rev_inv, (position + 6) % 8,  45,  65));
    addSlotToContainer(new SlotRevolverAmmo(rev_inv, (position + 7) % 8,  54,  39));

    int i,j;

    // Player inventory
    for(i = 0; i < 3; ++i)
    {
      for(j = 0; j < 9; ++j)
      {
        int s = j + i * 9 + 9;
        if(s == inventory_player.currentItem)
        {
          addSlotToContainer(new SlotLocked(inventory_player, s, 8 + j * 18, 140 + i * 18));
        } else
        {
          addSlotToContainer(new Slot(inventory_player, s, 8 + j * 18, 140 + i * 18));
        }
      }
    }

    // Player hotbar
    for(i = 0; i < 9; ++i)
    {
      if(i == inventory_player.currentItem)
      {
        addSlotToContainer(new SlotLocked(inventory_player, i, 8 + i * 18, 198));
      } else
      {
        addSlotToContainer(new Slot(inventory_player, i, 8 + i * 18, 198));
      }
    }
  }

  public ItemStack transferStackInSlot(EntityPlayer player, int slot_index)
  {
    ItemStack slot_stack = null;
    Slot slot = (Slot) inventorySlots.get(slot_index);

    if(slot != null && slot.getHasStack())
    {
      ItemStack stack = slot.getStack();
      slot_stack = stack.copy();

      if(slot_index >= SLOTS_INVENTORY && slot_index < SLOTS_HOTBAR)
      {
        if(!mergeItemStack(stack, SLOTS_HOTBAR, SLOTS_HOTBAR + 9, false))
        {
          return null;
        }
      } else if(slot_index >= SLOTS_HOTBAR && slot_index < SLOTS_HOTBAR + 9)
      {
        if(!mergeItemStack(stack, SLOTS_INVENTORY, SLOTS_INVENTORY + 3 * 9, false))
        {
          return null;
        }
      } else if(slot_index < SLOTS_INVENTORY)
      {
        if(!mergeItemStack(stack, SLOTS_INVENTORY, SLOTS_INVENTORY + 3 * 9, false))
        {
          return null;
        }
      } else
      {
        return null;
      }

      if(stack.stackSize == 0)
      {
        slot.putStack((ItemStack) null);
      } else
      {
        slot.onSlotChanged();
      }

      if(stack.stackSize == slot_stack.stackSize)
      {
        return null;
      }

      slot.onPickupFromSlot(player, stack);
    }

    return slot_stack;
  }

  @Override
  public boolean canInteractWith(EntityPlayer player)
  {
    return player.inventory.hasItemStack(revolver);
  }
  
  @Override
  public void onContainerClosed(EntityPlayer entityPlayer)
  {
    super.onContainerClosed(entityPlayer);
    if (!entityPlayer.worldObj.isRemote)
    {
      rev_inv.Save();
    }
  }
}