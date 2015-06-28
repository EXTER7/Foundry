package exter.foundry.inventory;

import exter.foundry.api.firearms.IFirearmAmmo;
import exter.foundry.item.FoundryItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class InventoryRevolver implements IInventory
{
  ItemStack[] items;
  
  ItemStack revolver;

  InventoryPlayer player_inv;
  public InventoryRevolver(ItemStack revolver_item,InventoryPlayer player)
  {
    revolver = revolver_item;
    player_inv = player;
    items = new ItemStack[8];
    int i;
    for(i = 0; i < 8; i++)
    {
      items[i] = FoundryItems.item_revolver.GetAmmo(revolver,i);
    }      
  }

  @Override
  public int getSizeInventory()
  {
    return 8;
  }

  @Override
  public ItemStack getStackInSlot(int slot)
  {
    return items[slot];
  }

  @Override
  public String getInventoryName()
  {
    return "Revolver.Ammo";
  }


  @Override
  public ItemStack decrStackSize(int slot, int amount)
  {
    if(items[slot] != null)
    {
      ItemStack is;

      if(items[slot].stackSize <= amount)
      {
        is = items[slot];
        items[slot] = null;
        markDirty();
        return is;
      } else
      {
        is = items[slot].splitStack(amount);

        if(items[slot].stackSize == 0)
        {
          items[slot] = null;
        }

        markDirty();
        return is;
      }
    } else
    {
      return null;
    }
  }

  @Override
  public ItemStack getStackInSlotOnClosing(int slot)
  {
    ItemStack ammo = items[slot];
    if(ammo != null)
    {
      items[slot] = null;
      return ammo;
    } else
    {
      return null;
    }
  }

  public void setInventorySlotContents(int slot, ItemStack stack)
  {
    items[slot] = stack;

    if(stack != null && stack.stackSize > this.getInventoryStackLimit())
    {
      stack.stackSize = this.getInventoryStackLimit();
    }
    markDirty();
  }

  @Override
  public int getInventoryStackLimit()
  {
    return 1;
  }

  @Override
  public void markDirty()
  {
    player_inv.markDirty();
  }

  @Override
  public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
  {
    return true;
  }

  @Override
  public void openInventory()
  {

  }

  @Override
  public void closeInventory()
  {

  }

  @Override
  public boolean isItemValidForSlot(int slot, ItemStack stack)
  {
    return stack.getItem() instanceof IFirearmAmmo;
  }

  @Override
  public boolean hasCustomInventoryName()
  {
    return false;
  }

  public void Save()
  {
    int i;
    for(i = 0; i < 8; i++)
    {
      FoundryItems.item_revolver.SetAmmo(revolver,i,items[i]);
    }
    player_inv.setInventorySlotContents(player_inv.currentItem, revolver);
  }
}
