package exter.foundry.container;

import exter.foundry.container.slot.SlotCasterMold;
import exter.foundry.container.slot.SlotContainer;
import exter.foundry.container.slot.SlotOutput;
import exter.foundry.recipes.manager.CastingRecipeManager;
import exter.foundry.tileentity.TileEntityMetalCaster;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerMetalCaster extends Container
{
  

  private TileEntityMetalCaster te_caster;
  
  // Slot numbers
  private static final int SLOTS_TE = 0;
  //private static final int SLOTS_TE_SIZE = 14;
  private static final int SLOTS_TE_MOLD_STORAGE = 5;
  private static final int SLOTS_TE_MOLD_STORAGE_SIZE = 9;
  private static final int SLOTS_INVENTORY = 14;
  private static final int SLOTS_HOTBAR = 14 + 3 * 9;

  private static final int SLOT_INVENTORY_X = 8;
  private static final int SLOT_INVENTORY_Y = 84;

  private static final int SLOT_HOTBAR_X = 8;
  private static final int SLOT_HOTBAR_Y = 142;

  private static final int SLOT_STORAGE_X = 116;
  private static final int SLOT_STORAGE_Y = 21;

  public ContainerMetalCaster(TileEntityMetalCaster caster, EntityPlayer player)
  {
    te_caster = caster;
    te_caster.openInventory(player);
    int i,j;

    addSlotToContainer(new SlotOutput(te_caster, TileEntityMetalCaster.INVENTORY_OUTPUT, 86, 51));
    addSlotToContainer(new SlotCasterMold(te_caster, TileEntityMetalCaster.INVENTORY_MOLD, 66, 21));
    addSlotToContainer(new Slot(te_caster, TileEntityMetalCaster.INVENTORY_EXTRA, 86, 21));
    addSlotToContainer(new SlotContainer(te_caster, TileEntityMetalCaster.INVENTORY_CONTAINER_DRAIN, 11, 21));
    addSlotToContainer(new SlotContainer(te_caster, TileEntityMetalCaster.INVENTORY_CONTAINER_FILL, 11, 51));
    for(i = 0; i < 3; ++i)
    {
      for(j = 0; j < 3; ++j)
      {
        addSlotToContainer(new SlotCasterMold(te_caster, TileEntityMetalCaster.INVENTORY_MOLD_STORAGE + i * 3 + j, SLOT_STORAGE_X + 18 * j, SLOT_STORAGE_Y + 18 * i));
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

      if (slot_index >= SLOTS_INVENTORY && slot_index < SLOTS_HOTBAR)
      {
        if(CastingRecipeManager.instance.isItemMold(stack))
        {
          int mold_slot = SLOTS_TE + TileEntityMetalCaster.INVENTORY_MOLD; 
          if(((SlotCasterMold)inventorySlots.get(mold_slot)).getStack() == null)
          {
            if(!mergeItemStack(stack, mold_slot, mold_slot + 1, false))
            {
              return null;
            }
          } else
          {
            mold_slot = SLOTS_TE + TileEntityMetalCaster.INVENTORY_MOLD_STORAGE;
            if(!mergeItemStack(stack, mold_slot, mold_slot + SLOTS_TE_MOLD_STORAGE_SIZE, false))
            {
              return null;
            }
          }
        } else
        {
          int merge_slot = SLOTS_TE + TileEntityMetalCaster.INVENTORY_EXTRA; 
          if(!mergeItemStack(stack, merge_slot, merge_slot + 1, false))
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
      } else if (slot_index == SLOTS_TE + TileEntityMetalCaster.INVENTORY_MOLD)
      {
        if (!mergeItemStack(stack, SLOTS_TE_MOLD_STORAGE, SLOTS_TE_MOLD_STORAGE + SLOTS_TE_MOLD_STORAGE_SIZE, false))
        {
          return null;
        }
      } else if(slot_index >= SLOTS_TE_MOLD_STORAGE && slot_index < SLOTS_TE_MOLD_STORAGE + SLOTS_TE_MOLD_STORAGE_SIZE)
      {
        SlotCasterMold storage_slot = (SlotCasterMold)inventorySlots.get(slot_index);
        SlotCasterMold output_slot = (SlotCasterMold)inventorySlots.get(SLOTS_TE + TileEntityMetalCaster.INVENTORY_MOLD);
        ItemStack tmp = storage_slot.getStack();
        if(tmp == null)
        {
          return null;
        }
        slot_stack = tmp;
        storage_slot.putStack(output_slot.getStack());
        output_slot.putStack(tmp);
        storage_slot.onSlotChanged();
        output_slot.onSlotChanged();
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
    te_caster.closeInventory(player);
  }
}
