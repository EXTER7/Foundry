package exter.foundry.tileentity;

import java.util.ArrayList;
import java.util.List;

import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemEmptyFoundryContainer;
import exter.foundry.item.ItemFoundryContainer;
import exter.foundry.network.FoundryPacketHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public abstract class TileEntityFoundry extends TileEntity implements IInventory
{
  public class ContainerSlot
  {
    public final boolean fill;
    public final FluidTank tank;
    public final int slot;
    
    public ContainerSlot(FluidTank container_tank,int container_slot,boolean container_fill)
    {
      tank = container_tank;
      slot = container_slot;
      fill = container_fill;
    }
    
    public boolean Update()
    {
      ItemStack stack = getStackInSlot(slot);
      boolean update_slot = false;
      if(stack == null)
      {
        return false;
      }
      Item item = stack.getItem();
      ItemFoundryContainer container = null;
      if(fill)
      {
        Fluid tank_fluid = tank.getFluid().getFluid();
        if(tank_fluid == null || tank.getFluidAmount() == 0)
        {
          return false;
        }
        if(item instanceof ItemFoundryContainer)
        {
          container = (ItemFoundryContainer)item;
          if(container.GetFluid().getID() != tank_fluid.getID() || stack.getItemDamage() == ItemFoundryContainer.AMOUNT_MAX)
          {
            return false;
          }
        } else if(item instanceof ItemEmptyFoundryContainer)
        {
          container = ItemFoundryContainer.GetContainerFromFluid(tank_fluid);
          if(container == null)
          {
            return false;
          }
          stack = new ItemStack(container,1,0);
          update_slot = true;
        }
        stack.setItemDamage(stack.getItemDamage() + 1);
        tank.drain(1, true);
      } else
      {
        Fluid tank_fluid = tank.getFluid().getFluid();
        if(tank_fluid != null && tank.getFluidAmount() > 0)
        {
          if(item instanceof ItemFoundryContainer)
          {
            container = (ItemFoundryContainer)item;
            if(container.GetFluid().getID() != tank_fluid.getID() || stack.getItemDamage() == 0)
            {
              return false;
            }
          } else if(item instanceof ItemEmptyFoundryContainer)
          {
            return false;
          }
        } else
        {
          if(item instanceof ItemFoundryContainer)
          {
            container = (ItemFoundryContainer)item;
            if(container.GetFluid().getID() != tank_fluid.getID() || stack.getItemDamage() == 0)
            {
              return false;
            }
          }
        }
        if(tank.getFluidAmount() == tank.getCapacity())
        {
          return false;
        }
        if(container != null)
        {
          tank.fill(new FluidStack(tank.getFluid(),1), true);
          int new_amount = stack.getItemDamage() - 1;
          if(new_amount == 0)
          {
            stack = new ItemStack(FoundryItems.item_container_empty,1);
            update_slot = true;
          } else
          {
            stack.setItemDamage(new_amount);
          }
        }
      }

      if(update_slot)
      {
        setInventorySlotContents(slot, stack);
      }
      return true;
    }
  }
  
  private List<ContainerSlot> conatiner_slots;
  private NBTTagCompound packet;
  private boolean do_update;
  
  
  protected final void AddContainerSlot(ContainerSlot cs)
  {
    conatiner_slots.add(cs);
  }

  protected abstract void UpdateEntityClient();

  protected abstract void UpdateEntityServer();

  public abstract FluidTank GetTank(int slot);
  
  public abstract int GetTankCount();
  
  public TileEntityFoundry()
  {
    conatiner_slots = new ArrayList<ContainerSlot>();
  }
  
  @Override
  public final Packet getDescriptionPacket()
  {
    NBTTagCompound nbt = new NBTTagCompound();
    writeToNBT(nbt);    
    return new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, nbt);
  }
  
  
  
  protected final void UpdateTank(int slot)
  {
    if(packet == null)
    {
      return;
    }
    WriteTankToNBT(packet,slot);
    do_update = true;
  }
  
  protected final void UpdateInventoryItem(int slot)
  {
    if(packet == null)
    {
      return;
    }
    WriteInventoryItemToNBT(packet,slot);
    do_update = true;
  }

  protected final void WriteTankToNBT(NBTTagCompound compound,int slot)
  {
    NBTTagCompound tag = new NBTTagCompound();
    GetTank(slot).writeToNBT(tag);
    compound.setTag("Tank_" + String.valueOf(slot), tag);
  }

  protected final void WriteInventoryItemToNBT(NBTTagCompound compound,int slot)
  {
    ItemStack is = getStackInSlot(slot);
    NBTTagCompound tag = new NBTTagCompound();
    if(is != null)
    {
      tag.setBoolean("empty", false);
      is.writeToNBT(tag);
    } else
    {
      tag.setBoolean("true", false);
    }
    compound.setTag("Item_" + String.valueOf(slot), tag);
  }


  @Override
  public void readFromNBT(NBTTagCompound compound)
  {
    super.readFromNBT(compound);
    
    int i;
    for(i = 0; i < GetTankCount(); i++)
    {
      NBTTagCompound tag = (NBTTagCompound)compound.getTag("Tank_" + String.valueOf(i));
      if(tag != null)
      {
        FluidTank tank = GetTank(i);
        tank.setFluid(null);
        tank.readFromNBT(tag);
      }
    }

    for(i = 0; i < getSizeInventory(); i++)
    {
      NBTTagCompound tag = (NBTTagCompound)compound.getTag("Item_" + String.valueOf(i));
      if(tag != null)
      {
        ItemStack stack = null;
        if(!tag.getBoolean("empty"))
        {
          stack = ItemStack.loadItemStackFromNBT(tag);
          setInventorySlotContents(i, stack);
        }
      }
    }
  }
  

  @Override
  public void writeToNBT(NBTTagCompound compound)
  {
    int i;
    super.writeToNBT(compound);
    for(i = 0; i < GetTankCount(); i++)
    {
      WriteTankToNBT(compound,i);
    }
    for(i = 0; i < getSizeInventory(); i++)
    {
      WriteInventoryItemToNBT(compound,i);
    }
  }

  protected final void UpdateValue(String name,int value)
  {
    if(packet == null)
    {
      return;
    }
    packet.setInteger(name, value);

    do_update = true;
  }
  
  protected final void UpdateNBTTag(String name,NBTTagCompound compound)
  {
    if(packet == null)
    {
      return;
    }
    packet.setTag(name, compound);
    do_update = true;
  }

  @Override
  public final void updateEntity()
  {
    if(this instanceof IPowerReceptor)
    {
      ((IPowerReceptor)this).getPowerReceiver(null).update();
    }

    if(!worldObj.isRemote)
    {
      packet = new NBTTagCompound();
      super.writeToNBT(packet);
      do_update = false;
      UpdateEntityServer();
      if(do_update)
      {
        FoundryPacketHandler.SendTileEntityPacketToPlayers(new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, packet), this);
      }
      packet = null;
    } else
    {
      UpdateEntityClient();
    }
  }

  @Override
  public final void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
  {
    super.onDataPacket(net, pkt);
    readFromNBT(pkt.data);
    //worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
  }
}
