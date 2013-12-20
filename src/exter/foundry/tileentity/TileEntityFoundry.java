package exter.foundry.tileentity;

import java.util.ArrayList;
import java.util.List;

import cofh.api.energy.IEnergyHandler;

import com.google.common.io.ByteArrayDataInput;

import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import exter.foundry.ModFoundry;
import exter.foundry.block.FoundryBlocks;
import exter.foundry.item.FoundryItems;
import exter.foundry.item.ItemRefractoryFluidContainer;
import exter.foundry.network.FoundryPacketHandler;
import exter.foundry.tileentity.TileEntityMetalCaster.RedstoneMode;
import exter.foundry.tileentity.energy.EnergyManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidContainerItem;

/**
 * Base class for all machines.
 */
public abstract class TileEntityFoundry extends TileEntity implements IInventory,IPowerReceptor,IEnergyHandler
{
  
  /**
   * Links an item slot to a tank for filling/draining containers.
   */
  public class ContainerSlot
  {
    public final boolean fill;
    public final int tank_slot;
    public final int slot;
    
    public ContainerSlot(int container_tank,int container_slot,boolean container_fill)
    {
      tank_slot = container_tank;
      slot = container_slot;
      fill = container_fill;
    }
    
    public void Update()
    {
      ItemStack stack = getStackInSlot(slot);
      if(stack == null || !(stack.getItem() instanceof IFluidContainerItem))
      {
        return;
      }
      IFluidContainerItem fluid_cont = (IFluidContainerItem)stack.getItem();
      
      FluidTank tank = GetTank(tank_slot);
      if(fill)
      {
        FluidStack drained = tank.drain(25, false);
        if(drained == null || drained.amount == 0)
        {
          return;
        }
        int filled = fluid_cont.fill(stack, drained, false);
        if(filled == 0)
        {
          return;
        }
        drained = tank.drain(filled, true);
        fluid_cont.fill(stack, drained, true);
        UpdateTank(tank_slot);
        UpdateInventoryItem(slot);
      } else
      {
        FluidStack drained = fluid_cont.drain(stack, 25, false);
        if(drained == null || drained.amount == 0)
        {
          return;
        }
        
        int filled = tank.fill(drained, false);
        if(filled == 0)
        {
          return;
        }
        drained = fluid_cont.drain(stack, filled, true);
        tank.fill(drained, true);
        UpdateTank(tank_slot);
        UpdateInventoryItem(slot);
      }
    }
  }
  
  private List<ContainerSlot> conatiner_slots;
  private NBTTagCompound packet;
  private boolean do_update;
  private boolean initialized;
  
  private PowerHandler power_handler;
  
  protected EnergyManager energy_manager;
  
  protected boolean last_redstone_signal;
  protected boolean redstone_signal;

  protected boolean update_energy;

  protected boolean update_energy_tick;
  
  
  protected final void AddContainerSlot(ContainerSlot cs)
  {
    conatiner_slots.add(cs);
  }

  protected abstract void UpdateEntityClient();

  protected abstract void UpdateEntityServer();

  public abstract FluidTank GetTank(int slot);
  
  public abstract int GetTankCount();

  public abstract int GetMaxStoredEnergy();

  
  public TileEntityFoundry()
  {
    conatiner_slots = new ArrayList<ContainerSlot>();
    last_redstone_signal = false;
    redstone_signal = false;
    initialized = false;
    power_handler = new PowerHandler(this,PowerHandler.Type.MACHINE);
    
    power_handler.configure(1, 32, 1, 64);
    power_handler.configurePowerPerdition(0, 0);

    energy_manager = new EnergyManager(GetMaxStoredEnergy());
    
    update_energy = false;
    update_energy_tick = true;
  }
  
  @Override
  public void invalidate()
  {
    initialized = false;
    super.invalidate();
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
      tag.setBoolean("empty", true);
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
        }
        setInventorySlotContents(i, stack);
      }
    }
    
    energy_manager.ReadFromNBT(compound);
  }
  
  public void ReceivePacketData(INetworkManager manager, Packet250CustomPayload packet, EntityPlayer entityPlayer, ByteArrayDataInput data)
  {

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
    energy_manager.WriteToNBT(compound);
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
    if(!(initialized || isInvalid()))
    {
      UpdateRedstone();
      update_energy_tick = true;
    }
    
    
    power_handler.update();
    
    
    if(!worldObj.isRemote)
    {
      int last_energy = energy_manager.GetStoredEnergy();

      float mj = power_handler.useEnergy(0, 50, true);

      energy_manager.ReceiveMJ(mj,true);

      packet = new NBTTagCompound();
      super.writeToNBT(packet);
      do_update = false;
      for(ContainerSlot cs:conatiner_slots)
      {
        cs.Update();
      }
      UpdateEntityServer();
      if(update_energy && (update_energy_tick || energy_manager.GetStoredEnergy() != last_energy))
      {
        energy_manager.WriteToNBT(packet);
        do_update = true;
      }
      
      if(do_update)
      {
        FoundryPacketHandler.SendTileEntityPacketToPlayers(new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, packet), this);
      }
      packet = null;
      update_energy_tick = false;
    } else
    {
      UpdateEntityClient();
    }
    last_redstone_signal = redstone_signal;
  }

  @Override
  public final void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
  {
    super.onDataPacket(net, pkt);
    readFromNBT(pkt.data);
    //worldObj.markBlockForRenderUpdate(xCoord, yCoord, zCoord);
  }
  
  @Override
  public PowerReceiver getPowerReceiver(ForgeDirection side)
  {
    return power_handler.getPowerReceiver();
  }

  @Override
  public void doWork(PowerHandler workProvider)
  {
  }

  @Override
  public World getWorld()
  {
    return worldObj;
  }

  
  public void UpdateRedstone()
  {
    redstone_signal = worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
  }
  
  @Override
  public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
  {
    if(!simulate && update_energy && !worldObj.isRemote)
    {
      update_energy_tick = true;
    }
    return energy_manager.ReceiveRF(maxReceive, !simulate);
  }

  @Override
  public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
  {
    return 0;
  }

  @Override
  public boolean canInterface(ForgeDirection from)
  {
    return true;
  }

  @Override
  public int getEnergyStored(ForgeDirection from)
  {
    return 0;
  }

  public int getMaxEnergyStored(ForgeDirection from)
  {
    return 0;
  }
}
