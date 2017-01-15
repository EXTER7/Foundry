package exter.foundry.container;

import exter.foundry.container.slot.SlotFluidContainer;
import exter.foundry.container.slot.SlotOutput;
import exter.foundry.tileentity.TileEntityMetalAtomizer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerMetalAtomizer extends Container
{
  

  private TileEntityMetalAtomizer te_atomizer;
  
  // Slot numbers
  public static final int SLOTS_TE = 0;
  public static final int SLOTS_TE_SIZE = 5;

  public static final int SLOTS_INVENTORY = 5;
  private static final int SLOTS_HOTBAR = 5 + 3 * 9;

  private static final int SLOT_INVENTORY_X = 8;
  private static final int SLOT_INVENTORY_Y = 84;

  private static final int SLOT_HOTBAR_X = 8;
  private static final int SLOT_HOTBAR_Y = 142;

  public ContainerMetalAtomizer(TileEntityMetalAtomizer caster, EntityPlayer player)
  {
    te_atomizer = caster;
    te_atomizer.openInventory(player);
    int i,j;

    addSlotToContainer(new SlotOutput(te_atomizer, TileEntityMetalAtomizer.INVENTORY_OUTPUT, 86, 37));
    addSlotToContainer(new SlotFluidContainer(te_atomizer, TileEntityMetalAtomizer.INVENTORY_CONTAINER_DRAIN, 11, 21));
    addSlotToContainer(new SlotFluidContainer(te_atomizer, TileEntityMetalAtomizer.INVENTORY_CONTAINER_FILL, 11, 51));
    addSlotToContainer(new SlotFluidContainer(te_atomizer, TileEntityMetalAtomizer.INVENTORY_CONTAINER_WATER_DRAIN, 151, 21));
    addSlotToContainer(new SlotFluidContainer(te_atomizer, TileEntityMetalAtomizer.INVENTORY_CONTAINER_WATER_FILL, 151, 51));

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
    return te_atomizer.isUsableByPlayer(par1EntityPlayer);
  }


  public ItemStack transferStackInSlot(EntityPlayer player, int slot_index)
  {
    ItemStack slot_stack = ItemStack.EMPTY;
    Slot slot = (Slot) inventorySlots.get(slot_index);

    if(slot != null && slot.getHasStack())
    {
      ItemStack stack = slot.getStack();
      slot_stack = stack.copy();

      if(slot_index >= SLOTS_HOTBAR && slot_index < SLOTS_HOTBAR + 9)
      {
        if(!mergeItemStack(stack, SLOTS_INVENTORY, SLOTS_INVENTORY + 3 * 9, false))
        {
          return ItemStack.EMPTY;
        }
      } else if(!mergeItemStack(stack, SLOTS_INVENTORY, SLOTS_HOTBAR + 9, false))
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
    te_atomizer.closeInventory(player);
  }
}
