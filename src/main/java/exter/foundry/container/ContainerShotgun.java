package exter.foundry.container;


import exter.foundry.container.slot.SlotLocked;
import exter.foundry.container.slot.SlotFirearmAmmo;
import exter.foundry.inventory.InventoryFirearm;
import exter.foundry.inventory.InventoryShotgun;
import exter.foundry.item.firearm.ItemShotgun;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerShotgun extends Container
{
  IInventory inventory;
  private ItemStack shotgun;
  InventoryFirearm shotgun_inv;
  // Slot numbers
  private static final int SLOTS_SHOTGUN = 0;
  private static final int SLOTS_INVENTORY = SLOTS_SHOTGUN + 5;
  private static final int SLOTS_HOTBAR = SLOTS_INVENTORY + 3 * 9;

  public ContainerShotgun(ItemStack revolver_item,InventoryPlayer inventory_player)
  {
    shotgun = revolver_item;
    inventory = inventory_player;


    shotgun_inv = new InventoryShotgun(shotgun,inventory_player,5);
    int i,j;
    for(i = 0; i < 5; i++)
    {
      addSlotToContainer(new SlotFirearmAmmo(shotgun_inv, i, 44 + 18 * i, 40, ItemShotgun.AMMO_TYPE));
    }


    // Player inventory
    for(i = 0; i < 3; ++i)
    {
      for(j = 0; j < 9; ++j)
      {
        int s = j + i * 9 + 9;
        if(s == inventory_player.currentItem)
        {
          addSlotToContainer(new SlotLocked(inventory_player, s, 8 + j * 18, 91 + i * 18));
        } else
        {
          addSlotToContainer(new Slot(inventory_player, s, 8 + j * 18, 91 + i * 18));
        }
      }
    }

    // Player hotbar
    for(i = 0; i < 9; ++i)
    {
      if(i == inventory_player.currentItem)
      {
        addSlotToContainer(new SlotLocked(inventory_player, i, 8 + i * 18, 149));
      } else
      {
        addSlotToContainer(new Slot(inventory_player, i, 8 + i * 18, 149));
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
    return player.inventory.hasItemStack(shotgun);
  }
  
  @Override
  public void onContainerClosed(EntityPlayer entityPlayer)
  {
    super.onContainerClosed(entityPlayer);
    shotgun_inv.closeInventory(entityPlayer);
    if (!entityPlayer.worldObj.isRemote)
    {
      shotgun_inv.save();
    }
  }
}