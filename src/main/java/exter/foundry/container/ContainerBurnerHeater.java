package exter.foundry.container;

import exter.foundry.tileentity.TileEntityBurnerHeater;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

public class ContainerBurnerHeater extends Container
{
  

  private TileEntityBurnerHeater te_burner;
  
  // Slot numbers
  public static final int SLOTS_TE = 0;
  public static final int SLOTS_TE_SIZE = 4;
  
  public static final int SLOTS_INVENTORY = 4;
  public static final int SLOTS_HOTBAR = 4 + 3 * 9;

  private static final int SLOT_INVENTORY_X = 8;
  private static final int SLOT_INVENTORY_Y = 84;

  private static final int SLOT_HOTBAR_X = 8;
  private static final int SLOT_HOTBAR_Y = 142;

  public ContainerBurnerHeater(TileEntityBurnerHeater burner, EntityPlayer player)
  {
    te_burner = burner;
    te_burner.openInventory(player);
    int i,j;

    
    for(i = 0; i < 2; ++i)
    {
      for(j = 0; j < 2; ++j)
      {
        addSlotToContainer(new Slot(te_burner,i * 2 + j,71 + i * 18,34 + j * 18));
      }
    }

    //Player Inventory
    for(i = 0; i < 3; ++i)
    {
      for(j = 0; j < 9; ++j)
      {
        addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, SLOT_INVENTORY_X + j * 18, SLOT_INVENTORY_Y + i * 18));
      }
    }
    for(i = 0; i < 9; ++i)
    {
      addSlotToContainer(new Slot(player.inventory, i, SLOT_HOTBAR_X + i * 18, SLOT_HOTBAR_Y));
    }
  }

  public boolean canInteractWith(EntityPlayer par1EntityPlayer)
  {
    return te_burner.isUseableByPlayer(par1EntityPlayer);
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
        if(TileEntityFurnace.isItemFuel(stack))
        {
          if(!mergeItemStack(stack, SLOTS_TE, SLOTS_TE + 4, false))
          {
            return null;
          } 
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
  public void onContainerClosed(EntityPlayer player)
  {
    super.onContainerClosed(player);
    te_burner.closeInventory(player);
  }
}