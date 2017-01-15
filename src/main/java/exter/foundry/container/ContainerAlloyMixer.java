package exter.foundry.container;

import exter.foundry.container.slot.SlotFluidContainer;
import exter.foundry.tileentity.TileEntityAlloyMixer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerAlloyMixer extends Container
{
  

  private TileEntityAlloyMixer te_alloymixer;
  
  // Slot numbers
  public static final int SLOTS_TE = 0;
  public static final int SLOTS_TE_SIZE = 10;
  
  public static final int SLOTS_INVENTORY = 10;
  private static final int SLOTS_HOTBAR = 10 + 3 * 9;

  private static final int SLOT_INVENTORY_X = 8;
  private static final int SLOT_INVENTORY_Y = 127;

  private static final int SLOT_HOTBAR_X = 8;
  private static final int SLOT_HOTBAR_Y = 185;

  public ContainerAlloyMixer(TileEntityAlloyMixer mixer, EntityPlayer player)
  {
    te_alloymixer = mixer;
    te_alloymixer.openInventory(player);
    int i,j;

    
    addSlotToContainer(new SlotFluidContainer(te_alloymixer,TileEntityAlloyMixer.INVENTORY_CONTAINER_INPUT_0_DRAIN,26,17));
    addSlotToContainer(new SlotFluidContainer(te_alloymixer,TileEntityAlloyMixer.INVENTORY_CONTAINER_INPUT_0_FILL,26,92));
    addSlotToContainer(new SlotFluidContainer(te_alloymixer,TileEntityAlloyMixer.INVENTORY_CONTAINER_INPUT_1_DRAIN,47,17));
    addSlotToContainer(new SlotFluidContainer(te_alloymixer,TileEntityAlloyMixer.INVENTORY_CONTAINER_INPUT_1_FILL,47,92));
    addSlotToContainer(new SlotFluidContainer(te_alloymixer,TileEntityAlloyMixer.INVENTORY_CONTAINER_INPUT_2_DRAIN,68,17));
    addSlotToContainer(new SlotFluidContainer(te_alloymixer,TileEntityAlloyMixer.INVENTORY_CONTAINER_INPUT_2_FILL,68,92));
    addSlotToContainer(new SlotFluidContainer(te_alloymixer,TileEntityAlloyMixer.INVENTORY_CONTAINER_INPUT_3_DRAIN,89,17));
    addSlotToContainer(new SlotFluidContainer(te_alloymixer,TileEntityAlloyMixer.INVENTORY_CONTAINER_INPUT_3_FILL,89,92));
    addSlotToContainer(new SlotFluidContainer(te_alloymixer,TileEntityAlloyMixer.INVENTORY_CONTAINER_OUTPUT_DRAIN,133,17));
    addSlotToContainer(new SlotFluidContainer(te_alloymixer,TileEntityAlloyMixer.INVENTORY_CONTAINER_OUTPUT_FILL,133,92));

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
    return te_alloymixer.isUsableByPlayer(par1EntityPlayer);
  }

  public ItemStack transferStackInSlot(EntityPlayer player, int slot_index)
  {
    ItemStack slot_stack = ItemStack.EMPTY;
    Slot slot = (Slot) inventorySlots.get(slot_index);

    if (slot != null && slot.getHasStack())
    {
      ItemStack stack = slot.getStack();
      slot_stack = stack.copy();

      if (slot_index >= SLOTS_INVENTORY && slot_index < SLOTS_HOTBAR)
      {
        if (!mergeItemStack(stack, SLOTS_TE, SLOTS_TE + SLOTS_TE_SIZE, false))
        {
          return ItemStack.EMPTY;
        }
      } else if (slot_index >= SLOTS_HOTBAR && slot_index < SLOTS_HOTBAR + 9)
      {
        if (!mergeItemStack(stack, SLOTS_INVENTORY, SLOTS_INVENTORY + 3 * 9, false))
        {
          return ItemStack.EMPTY;
        }
      } else if (!mergeItemStack(stack, SLOTS_INVENTORY, SLOTS_HOTBAR + 9, false))
      {
        return ItemStack.EMPTY;
      }

      if (stack.isEmpty())
      {
        slot.putStack(stack);
      } else
      {
        slot.onSlotChanged();
      }

      if (stack.getCount() == slot_stack.getCount())
      {
        return ItemStack.EMPTY;
      }

      slot.onTake(player, stack);

    }

    return slot_stack;
  }

  @Override
  public void onContainerClosed(EntityPlayer player)
  {
    super.onContainerClosed(player);
    te_alloymixer.closeInventory(player);
  }
}
