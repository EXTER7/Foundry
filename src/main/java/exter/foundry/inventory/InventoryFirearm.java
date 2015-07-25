package exter.foundry.inventory;

import exter.foundry.api.firearms.IFirearmRound;
import exter.foundry.item.firearm.ItemFirearm;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class InventoryFirearm implements IInventory
{
  protected ItemStack[] items;
  
  protected ItemStack firearm;

  InventoryPlayer player_inv;
  public InventoryFirearm(ItemStack firearm_item,InventoryPlayer player,int size)
  {
    firearm = firearm_item;
    player_inv = player;
    items = new ItemStack[size];
    int i;
    for(i = 0; i < items.length; i++)
    {
      items[i] = ((ItemFirearm)firearm.getItem()).GetAmmo(firearm,i);
    }      
  }

  @Override
  public int getSizeInventory()
  {
    return items.length;
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
    return stack.getItem() instanceof IFirearmRound;
  }

  @Override
  public boolean hasCustomInventoryName()
  {
    return false;
  }

  public void Save()
  {
    int i;
    for(i = 0; i < items.length; i++)
    {
      ((ItemFirearm)firearm.getItem()).SetAmmo(firearm,i,items[i]);
    }
    player_inv.setInventorySlotContents(player_inv.currentItem, firearm);
  }
}
