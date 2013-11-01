package exter.foundry.container;

import exter.foundry.slot.SlotCasterMold;
import exter.foundry.slot.SlotCasterOutput;
import exter.foundry.tileentity.TileEntityMetalCaster;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerMetalCaster extends Container
{
  

  private TileEntityMetalCaster te_caster;
  
  // Slot numbers
  private static final int SLOTS_OUTPUT = 0;
  private static final int SLOTS_MOLD = 1;
  private static final int SLOTS_EXTRA = 1;
  private static final int SLOTS_INVENTORY = 3;
  private static final int SLOTS_HOTBAR = 3 + 3 * 9;
  
  private static final int SLOT_OUTPUT_X = 94;
  private static final int SLOT_OUTPUT_Y = 51;

  private static final int SLOT_MOLD_X = 66;
  private static final int SLOT_MOLD_Y = 21;
  
  private static final int SLOT_EXTRA_X = 87;
  private static final int SLOT_EXTRA_Y = 21;

  private static final int SLOT_INVENTORY_X = 8;
  private static final int SLOT_INVENTORY_Y = 84;

  private static final int SLOT_HOTBAR_X = 8;
  private static final int SLOT_HOTBAR_Y = 142;

  public ContainerMetalCaster(TileEntityMetalCaster caster, IInventory player_inventory)
  {
    te_caster = caster;
    te_caster.openChest();
    int i,j;

    addSlotToContainer(new SlotCasterOutput(te_caster, 0, SLOT_OUTPUT_X, SLOT_OUTPUT_Y));
    addSlotToContainer(new SlotCasterMold(te_caster, 1, SLOT_MOLD_X, SLOT_MOLD_Y));
    addSlotToContainer(new Slot(te_caster, 2, SLOT_EXTRA_X, SLOT_EXTRA_Y));

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
    return te_caster.isUseableByPlayer(par1EntityPlayer);
  }

  public ItemStack transferStackInSlot(EntityPlayer player, int slot_index)
  {
    ItemStack slot_stack = null;
    Slot slot = (Slot) inventorySlots.get(slot_index);

    if (slot != null && slot.getHasStack())
    {
      ItemStack stack = slot.getStack();
      slot_stack = stack.copy();

      if (slot_index == SLOTS_OUTPUT)
      {
        if (!mergeItemStack(stack, SLOTS_INVENTORY, SLOTS_INVENTORY + 3 * 9, false))
        {
          return null;
        }
      } else if (slot_index >= SLOTS_INVENTORY && slot_index < SLOTS_HOTBAR)
      {
        if (!mergeItemStack(stack, SLOTS_MOLD, SLOTS_EXTRA, false))
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
    te_caster.closeChest();
  }

  @Override
  public void detectAndSendChanges()
  {
    super.detectAndSendChanges();

    for(int i = 0; i < crafters.size(); i++)
    {
      te_caster.SendGUINetworkData(this, (ICrafting) crafters.get(i));
    }
  }
  
  @Override
  public void updateProgressBar(int i, int j)
  {
    te_caster.GetGUINetworkData(i, j);
  }

}
