package exter.foundry.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import exter.foundry.slot.SlotOutput;
import exter.foundry.tileentity.TileEntityAlloyFurnace;

public class ContainerAlloyFurnace extends Container
{
  

  private TileEntityAlloyFurnace te_alloyfurnace;
  
  // Slot numbers
  private static final int SLOTS_TE = 0;
  //private static final int SLOTS_TE_SIZE = 4;
  
  private static final int SLOTS_INVENTORY = 4;
  private static final int SLOTS_HOTBAR = 4 + 3 * 9;

  private static final int SLOT_INVENTORY_X = 8;
  private static final int SLOT_INVENTORY_Y = 84;

  private static final int SLOT_HOTBAR_X = 8;
  private static final int SLOT_HOTBAR_Y = 142;

  public ContainerAlloyFurnace(TileEntityAlloyFurnace furnace, IInventory player_inventory)
  {
    te_alloyfurnace = furnace;
    te_alloyfurnace.openInventory();
    int i,j;

    
    addSlotToContainer(new Slot(te_alloyfurnace,TileEntityAlloyFurnace.SLOT_INPUT_A,38,17));
    addSlotToContainer(new Slot(te_alloyfurnace,TileEntityAlloyFurnace.SLOT_INPUT_B,56,17));
    addSlotToContainer(new SlotOutput(te_alloyfurnace,TileEntityAlloyFurnace.SLOT_OUTPUT,116,35));
    addSlotToContainer(new Slot(te_alloyfurnace,TileEntityAlloyFurnace.SLOT_FUEL,48,53));

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
    return te_alloyfurnace.isUseableByPlayer(par1EntityPlayer);
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
          int s = SLOTS_TE + TileEntityAlloyFurnace.SLOT_FUEL;
          if(!mergeItemStack(stack, s, s + 1, false))
          {
            return null;
          } 
        } else if(!mergeItemStack(stack, SLOTS_TE, SLOTS_TE + TileEntityAlloyFurnace.SLOT_INPUT_B + 1, false))
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
    te_alloyfurnace.closeInventory();
  }
}