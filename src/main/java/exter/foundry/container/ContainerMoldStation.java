package exter.foundry.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import exter.foundry.block.BlockComponent;
import exter.foundry.block.FoundryBlocks;
import exter.foundry.container.slot.SlotFiltered;
import exter.foundry.container.slot.SlotOutput;
import exter.foundry.tileentity.TileEntityMoldStation;

public class ContainerMoldStation extends Container
{
  

  private TileEntityMoldStation te_station;
  
  // Slot numbers
  public static final int SLOTS_TE = 0;
  public static final int SLOTS_TE_SIZE = 4;
  
  public static final int SLOTS_INVENTORY = 4;
  public static final int SLOTS_HOTBAR = 4 + 3 * 9;

  private static final int SLOT_INVENTORY_X = 8;
  private static final int SLOT_INVENTORY_Y = 108;

  private static final int SLOT_HOTBAR_X = 8;
  private static final int SLOT_HOTBAR_Y = 166;

  public ContainerMoldStation(TileEntityMoldStation station, EntityPlayer player)
  {
    te_station = station;
    te_station.openInventory(player);
    int i,j;

    
    addSlotToContainer(new SlotFiltered(te_station,TileEntityMoldStation.SLOT_BLOCK,8,16,FoundryBlocks.block_component.asItemStack(BlockComponent.EnumVariant.REFCLAYBLOCK)));
    addSlotToContainer(new SlotOutput(te_station,TileEntityMoldStation.SLOT_CLAY,8,76));
    addSlotToContainer(new SlotOutput(te_station,TileEntityMoldStation.SLOT_OUTPUT,147,38));
    addSlotToContainer(new Slot(te_station,TileEntityMoldStation.SLOT_FUEL,119,76));

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
    return te_station.isUseableByPlayer(par1EntityPlayer);
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
          int s = SLOTS_TE + TileEntityMoldStation.SLOT_FUEL;
          if(!mergeItemStack(stack, s, s + 1, false))
          {
            return null;
          } 
        } else if(!mergeItemStack(stack, SLOTS_TE, SLOTS_TE + TileEntityMoldStation.SLOT_BLOCK, false))
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
  public void onContainerClosed(EntityPlayer player)
  {
    super.onContainerClosed(player);
    te_station.closeInventory(player);
  }
}