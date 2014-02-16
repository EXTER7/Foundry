package exter.foundry.container;

import exter.foundry.slot.SlotContainer;
import exter.foundry.tileentity.TileEntityAlloyMixer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerAlloyMixer extends Container
{
  

  private TileEntityAlloyMixer te_alloymixer;
  
  // Slot numbers
  private static final int SLOTS_TE = 0;
  private static final int SLOTS_TE_SIZE = 10;
  
  private static final int SLOTS_INVENTORY = 10;
  private static final int SLOTS_HOTBAR = 10 + 3 * 9;

  private static final int SLOT_INVENTORY_X = 8;
  private static final int SLOT_INVENTORY_Y = 127;

  private static final int SLOT_HOTBAR_X = 8;
  private static final int SLOT_HOTBAR_Y = 185;

  public ContainerAlloyMixer(TileEntityAlloyMixer mixer, IInventory player_inventory)
  {
    te_alloymixer = mixer;
    te_alloymixer.openInventory();
    int i,j;

    
    addSlotToContainer(new SlotContainer(te_alloymixer,TileEntityAlloyMixer.INVENTORY_CONTAINER_INPUT_0_DRAIN,26,17));
    addSlotToContainer(new SlotContainer(te_alloymixer,TileEntityAlloyMixer.INVENTORY_CONTAINER_INPUT_0_FILL,26,92));
    addSlotToContainer(new SlotContainer(te_alloymixer,TileEntityAlloyMixer.INVENTORY_CONTAINER_INPUT_1_DRAIN,47,17));
    addSlotToContainer(new SlotContainer(te_alloymixer,TileEntityAlloyMixer.INVENTORY_CONTAINER_INPUT_1_FILL,47,92));
    addSlotToContainer(new SlotContainer(te_alloymixer,TileEntityAlloyMixer.INVENTORY_CONTAINER_INPUT_2_DRAIN,68,17));
    addSlotToContainer(new SlotContainer(te_alloymixer,TileEntityAlloyMixer.INVENTORY_CONTAINER_INPUT_2_FILL,68,92));
    addSlotToContainer(new SlotContainer(te_alloymixer,TileEntityAlloyMixer.INVENTORY_CONTAINER_INPUT_3_DRAIN,89,17));
    addSlotToContainer(new SlotContainer(te_alloymixer,TileEntityAlloyMixer.INVENTORY_CONTAINER_INPUT_3_FILL,89,92));
    addSlotToContainer(new SlotContainer(te_alloymixer,TileEntityAlloyMixer.INVENTORY_CONTAINER_OUTPUT_DRAIN,133,17));
    addSlotToContainer(new SlotContainer(te_alloymixer,TileEntityAlloyMixer.INVENTORY_CONTAINER_OUTPUT_FILL,133,92));

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
    return te_alloymixer.isUseableByPlayer(par1EntityPlayer);
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
        if (!mergeItemStack(stack, SLOTS_TE, SLOTS_TE + SLOTS_TE_SIZE, false))
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
    te_alloymixer.closeInventory();
  }
  
  @Override
  public void detectAndSendChanges()
  {
    super.detectAndSendChanges();

    for(int i = 0; i < crafters.size(); i++)
    {
      te_alloymixer.SendGUINetworkData(this, (ICrafting) crafters.get(i));
    }
  }

  @Override
  public void updateProgressBar(int i, int j)
  {
    te_alloymixer.GetGUINetworkData(i, j);
  }
}
